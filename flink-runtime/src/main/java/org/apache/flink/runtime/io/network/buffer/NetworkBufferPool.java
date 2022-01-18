/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.runtime.io.network.buffer;

import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.api.common.time.Deadline;
import org.apache.flink.configuration.NettyShuffleEnvironmentOptions;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.core.memory.MemorySegment;
import org.apache.flink.core.memory.MemorySegmentFactory;
import org.apache.flink.core.memory.MemorySegmentProvider;
import org.apache.flink.runtime.io.AvailabilityProvider;
import org.apache.flink.util.ExceptionUtils;
import org.apache.flink.util.MathUtils;
import org.apache.flink.util.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.apache.flink.util.Preconditions.checkArgument;
import static org.apache.flink.util.Preconditions.checkNotNull;

/**
 * NetworkBufferPool 是网络堆栈的固定大小的 {@link MemorySegment} 实例池。
 *
 * <p>NetworkBufferPool 创建 {@link LocalBufferPool}，各个任务从中提取缓冲区以进行网络数据传输。
 * 当创建新的本地缓冲池时，NetworkBufferPool 会在池之间动态重新分配缓冲区。
 */
public class NetworkBufferPool implements BufferPoolFactory, MemorySegmentProvider, AvailabilityProvider {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkBufferPool.class);

    private final int totalNumberOfMemorySegments;

    private final int memorySegmentSize;

    private final ArrayDeque<MemorySegment> availableMemorySegments;

    private volatile boolean isDestroyed;

    // ---- Managed buffer pools ----------------------------------------------

    private final Object factoryLock = new Object();

    private final Set<LocalBufferPool> allBufferPools = new HashSet<>();

    private int numTotalRequiredBuffers;

    private final Duration requestSegmentsTimeout;

    private final AvailabilityHelper availabilityHelper = new AvailabilityHelper();

    @VisibleForTesting
    public NetworkBufferPool(int numberOfSegmentsToAllocate, int segmentSize) {
        this(numberOfSegmentsToAllocate, segmentSize, Duration.ofMillis(Integer.MAX_VALUE));
    }

    /** 分配此池管理的所有{@link MemorySegment}实例。 */
    public NetworkBufferPool(
            int numberOfSegmentsToAllocate,
            int segmentSize,
            Duration requestSegmentsTimeout) {
        this.totalNumberOfMemorySegments = numberOfSegmentsToAllocate;
        this.memorySegmentSize = segmentSize;

        Preconditions.checkNotNull(requestSegmentsTimeout);
        checkArgument(
                requestSegmentsTimeout.toMillis() > 0,
                "The timeout for requesting exclusive buffers should be positive.");
        this.requestSegmentsTimeout = requestSegmentsTimeout;

        final long sizeInLong = (long) segmentSize;

        try {
            this.availableMemorySegments = new ArrayDeque<>(numberOfSegmentsToAllocate);
        } catch (OutOfMemoryError err) {
            throw new OutOfMemoryError(
                    "Could not allocate buffer queue of length "
                            + numberOfSegmentsToAllocate
                            + " - "
                            + err.getMessage());
        }

        try {
            for (int i = 0; i < numberOfSegmentsToAllocate; i++) {
                availableMemorySegments.add(
                        MemorySegmentFactory.allocateUnpooledOffHeapMemory(segmentSize, null));
            }
        } catch (OutOfMemoryError err) {
            int allocated = availableMemorySegments.size();

            // 释放一些内存
            availableMemorySegments.clear();

            //用户请求的内存数量
            long requiredMb = (sizeInLong * numberOfSegmentsToAllocate) >> 20;
            //已经分配的内存数量
            long allocatedMb = (sizeInLong * allocated) >> 20;
            //当前缓冲池缺少得内存
            long missingMb = requiredMb - allocatedMb;

            throw new OutOfMemoryError(
                    "Could not allocate enough memory segments for NetworkBufferPool "
                            + "(required (Mb): "
                            + requiredMb
                            + ", allocated (Mb): "
                            + allocatedMb
                            + ", missing (Mb): "
                            + missingMb
                            + "). Cause: "
                            + err.getMessage());
        }

        availabilityHelper.resetAvailable();

        long allocatedMb = (sizeInLong * availableMemorySegments.size()) >> 20;

        LOG.info(
                "Allocated {} MB for network buffer pool (number of memory segments: {}, bytes per segment: {}).",
                allocatedMb,
                availableMemorySegments.size(),
                segmentSize);
    }

    @Nullable
    public MemorySegment requestMemorySegment() {
        synchronized (availableMemorySegments) {
            return internalRequestMemorySegment();
        }
    }

    public List<MemorySegment> requestMemorySegmentsBlocking(int numberOfSegmentsToRequest)
            throws IOException {
        return internalRequestMemorySegments(numberOfSegmentsToRequest);
    }

    public void recycle(MemorySegment segment) {
        // 将段添加回队列，但不会立即释放内存，因为在释放对全局池的引用时会发生这种情况，从而使availableMemorySegments队列及其包含的对象可回收
        internalRecycleMemorySegments(Collections.singleton(checkNotNull(segment)));
    }

    @Override
    public List<MemorySegment> requestMemorySegments(int numberOfSegmentsToRequest)
            throws IOException {
        checkArgument(
                numberOfSegmentsToRequest >= 0,
                "Number of buffers to request must be non-negative.");

        synchronized (factoryLock) {
            if (isDestroyed) {
                throw new IllegalStateException("Network buffer pool has already been destroyed.");
            }

            if (numberOfSegmentsToRequest == 0) {
                return Collections.emptyList();
            }

            tryRedistributeBuffers(numberOfSegmentsToRequest);
        }

        return internalRequestMemorySegments(numberOfSegmentsToRequest);
    }

    private List<MemorySegment> internalRequestMemorySegments(int numberOfSegmentsToRequest)
            throws IOException {
        final List<MemorySegment> segments = new ArrayList<>(numberOfSegmentsToRequest);
        try {
            final Deadline deadline = Deadline.fromNow(requestSegmentsTimeout);
            while (true) {
                if (isDestroyed) {
                    throw new IllegalStateException("Buffer pool is destroyed.");
                }

                MemorySegment segment;
                synchronized (availableMemorySegments) {
                    if ((segment = internalRequestMemorySegment()) == null) {
                        availableMemorySegments.wait(2000);
                    }
                }
                if (segment != null) {
                    segments.add(segment);
                }

                if (segments.size() >= numberOfSegmentsToRequest) {
                    break;
                }

                if (!deadline.hasTimeLeft()) {
                    throw new IOException(
                            String.format(
                                    "Timeout triggered when requesting exclusive buffers: %s, "
                                            + " or you may increase the timeout which is %dms by setting the key '%s'.",
                                    getConfigDescription(),
                                    requestSegmentsTimeout.toMillis(),
                                    NettyShuffleEnvironmentOptions
                                            .NETWORK_EXCLUSIVE_BUFFERS_REQUEST_TIMEOUT_MILLISECONDS
                                            .key()));
                }
            }
        } catch (Throwable e) {
            recycleMemorySegments(segments, numberOfSegmentsToRequest);
            ExceptionUtils.rethrowIOException(e);
        }

        return segments;
    }

    @Nullable
    private MemorySegment internalRequestMemorySegment() {
        assert Thread.holdsLock(availableMemorySegments);

        final MemorySegment segment = availableMemorySegments.poll();
        if (availableMemorySegments.isEmpty() && segment != null) {
            availabilityHelper.resetUnavailable();
        }
        return segment;
    }

    @Override
    public void recycleMemorySegments(Collection<MemorySegment> segments) {
        recycleMemorySegments(segments, segments.size());
    }

    private void recycleMemorySegments(Collection<MemorySegment> segments, int size) {
        internalRecycleMemorySegments(segments);

        synchronized (factoryLock) {
            numTotalRequiredBuffers -= size;

            // note: if this fails, we're fine for the buffer pool since we already recycled the
            // segments
            redistributeBuffers();
        }
    }

    private void internalRecycleMemorySegments(Collection<MemorySegment> segments) {
        CompletableFuture<?> toNotify = null;
        synchronized (availableMemorySegments) {
            //剩余内存块用尽，且还需请求内存块
            if (availableMemorySegments.isEmpty() && !segments.isEmpty()) {
                toNotify = availabilityHelper.getUnavailableToResetAvailable();
            }
            //将回收的内存块释放，并加入可用剩余内存块
            availableMemorySegments.addAll(segments);
            //当本地缓冲池中的所有内存块用尽，本地缓冲池处于阻塞状态
            availableMemorySegments.notifyAll();
        }

        if (toNotify != null) {
            toNotify.complete(null);
        }
    }

    //使用缓存池的内存
    public void destroy() {
        synchronized (factoryLock) {
            isDestroyed = true;
        }

        synchronized (availableMemorySegments) {
            MemorySegment segment;
            while ((segment = availableMemorySegments.poll()) != null) {
                segment.free();
            }
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public int getTotalNumberOfMemorySegments() {
        return isDestroyed() ? 0 : totalNumberOfMemorySegments;
    }

    public long getTotalMemory() {
        return (long) getTotalNumberOfMemorySegments() * memorySegmentSize;
    }

    public int getNumberOfAvailableMemorySegments() {
        synchronized (availableMemorySegments) {
            return availableMemorySegments.size();
        }
    }

    public long getAvailableMemory() {
        return (long) getNumberOfAvailableMemorySegments() * memorySegmentSize;
    }

    public int getNumberOfUsedMemorySegments() {
        return getTotalNumberOfMemorySegments() - getNumberOfAvailableMemorySegments();
    }

    public long getUsedMemory() {
        return (long) getNumberOfUsedMemorySegments() * memorySegmentSize;
    }

    public int getNumberOfRegisteredBufferPools() {
        synchronized (factoryLock) {
            return allBufferPools.size();
        }
    }

    public int countBuffers() {
        int buffers = 0;

        synchronized (factoryLock) {
            for (BufferPool bp : allBufferPools) {
                buffers += bp.getNumBuffers();
            }
        }

        return buffers;
    }

    /** Returns a future that is completed when there are free segments in this pool. */
    @Override
    public CompletableFuture<?> getAvailableFuture() {
        return availabilityHelper.getAvailableFuture();
    }

    // ------------------------------------------------------------------------
    // BufferPoolFactory
    // ------------------------------------------------------------------------

    @Override
    public BufferPool createBufferPool(int numRequiredBuffers, int maxUsedBuffers)
            throws IOException {
        return internalCreateBufferPool(numRequiredBuffers, maxUsedBuffers, 0, Integer.MAX_VALUE);
    }

    @Override
    public BufferPool createBufferPool(
            int numRequiredBuffers,
            int maxUsedBuffers,
            int numSubpartitions,
            int maxBuffersPerChannel)
            throws IOException {
        return internalCreateBufferPool(
                numRequiredBuffers, maxUsedBuffers, numSubpartitions, maxBuffersPerChannel);
    }

    private BufferPool internalCreateBufferPool(
            int numRequiredBuffers,
            int maxUsedBuffers,
            int numSubpartitions,
            int maxBuffersPerChannel)
            throws IOException {

        // 有必要使用与用于缓冲请求的锁不同的锁，以确保在失败情况下不会出现死锁。
        synchronized (factoryLock) {
            if (isDestroyed) {
                throw new IllegalStateException("Network buffer pool has already been destroyed.");
            }

            // 确保可以满足所需缓冲区的数量。使用动态内存管理，这应该会过时。
            if (numTotalRequiredBuffers + numRequiredBuffers > totalNumberOfMemorySegments) {
                throw new IOException(
                        String.format(
                                "Insufficient number of network buffers: "
                                        + "required %d, but only %d available. %s.",
                                numRequiredBuffers,
                                totalNumberOfMemorySegments - numTotalRequiredBuffers,
                                getConfigDescription()));
            }

            this.numTotalRequiredBuffers += numRequiredBuffers;

            // 我们很高兴，创建一个新的缓冲池并重新分配非固定大小的缓冲区。
            LocalBufferPool localBufferPool =
                    new LocalBufferPool(
                            this,
                            numRequiredBuffers,
                            maxUsedBuffers,
                            numSubpartitions,
                            maxBuffersPerChannel);

            allBufferPools.add(localBufferPool);

            redistributeBuffers();

            return localBufferPool;
        }
    }

    @Override
    public void destroyBufferPool(BufferPool bufferPool) {
        if (!(bufferPool instanceof LocalBufferPool)) {
            throw new IllegalArgumentException("bufferPool is no LocalBufferPool");
        }

        synchronized (factoryLock) {
            if (allBufferPools.remove(bufferPool)) {
                numTotalRequiredBuffers -= bufferPool.getNumberOfRequiredMemorySegments();

                redistributeBuffers();
            }
        }
    }

    /**
     * 销毁从该缓冲池（通过 {@link #createBufferPool(int, int)} 创建的）分配缓冲区的所有缓冲池。
     */
    public void destroyAllBufferPools() {
        synchronized (factoryLock) {
            // create a copy to avoid concurrent modification exceptions
            LocalBufferPool[] poolsCopy =
                    allBufferPools.toArray(new LocalBufferPool[allBufferPools.size()]);

            for (LocalBufferPool pool : poolsCopy) {
                pool.lazyDestroy();
            }

            // some sanity checks
            if (allBufferPools.size() > 0 || numTotalRequiredBuffers > 0) {
                throw new IllegalStateException(
                        "NetworkBufferPool is not empty after destroying all LocalBufferPools");
            }
        }
    }

    // 必须从同步块调用
    private void tryRedistributeBuffers(int numberOfSegmentsToRequest) throws IOException {
        assert Thread.holdsLock(factoryLock);

        if (numTotalRequiredBuffers + numberOfSegmentsToRequest > totalNumberOfMemorySegments) {
            throw new IOException(
                    String.format(
                            "Insufficient number of network buffers: "
                                    + "required %d, but only %d available. %s.",
                            numberOfSegmentsToRequest,
                            totalNumberOfMemorySegments - numTotalRequiredBuffers,
                            getConfigDescription()));
        }

        this.numTotalRequiredBuffers += numberOfSegmentsToRequest;

        try {
            redistributeBuffers();
        } catch (Throwable t) {
            this.numTotalRequiredBuffers -= numberOfSegmentsToRequest;

            redistributeBuffers();
            ExceptionUtils.rethrow(t);
        }
    }

    // 必须从同步块调用
    private void redistributeBuffers() {
        assert Thread.holdsLock(factoryLock);

        // 所有缓冲区，不在所需缓冲区中
        final int numAvailableMemorySegment = totalNumberOfMemorySegments - numTotalRequiredBuffers;

        if (numAvailableMemorySegment == 0) {
            // 在这种情况下，我们需要重新分配缓冲区，以便每个池都获得其最小值
            for (LocalBufferPool bufferPool : allBufferPools) {
                bufferPool.setNumBuffers(bufferPool.getNumberOfRequiredMemorySegments());
            }
            return;
        }

        /*
         * 由于缓冲池可能受到限制，让我们根据每个缓冲池的容量分配可用内存段，
         * 即无限缓冲池可以占用的最大段数为 numAvailableMemorySegment，对于有限缓冲池，
         * 它可能会更少。基于此以及所有这些值的总和（totalCapacity），我们构建了一个用于分配缓冲区的比率。
         */

        long totalCapacity = 0; // long 以避免 int 溢出

        for (LocalBufferPool bufferPool : allBufferPools) {
            int excessMax =
                    bufferPool.getMaxNumberOfMemorySegments()
                            - bufferPool.getNumberOfRequiredMemorySegments();
            totalCapacity += Math.min(numAvailableMemorySegment, excessMax);
        }

        // 没有能力接收额外的缓冲区？
        if (totalCapacity == 0) {
            return; // 有必要在没有重新分配的情况下避免 div 为零
        }

        //因为 'min(a,b)' 的参数之一是一个正整数，所以这实际上保证在 'int' 域内
        // （我们使用检查的 downCast 来更优雅地处理可能的错误）。
        final int memorySegmentsToDistribute =
                MathUtils.checkedDownCast(Math.min(numAvailableMemorySegment, totalCapacity));

        long totalPartsUsed = 0; // of totalCapacity
        int numDistributedMemorySegment = 0;
        for (LocalBufferPool bufferPool : allBufferPools) {
            int excessMax =
                    bufferPool.getMaxNumberOfMemorySegments()
                            - bufferPool.getNumberOfRequiredMemorySegments();

            // shortcut
            if (excessMax == 0) {
                continue;
            }

            totalPartsUsed += Math.min(numAvailableMemorySegment, excessMax);

            // avoid remaining buffers by looking at the total capacity that should have been
            // re-distributed up until here
            // the downcast will always succeed, because both arguments of the subtraction are in
            // the 'int' domain
            final int mySize =
                    MathUtils.checkedDownCast(
                            memorySegmentsToDistribute * totalPartsUsed / totalCapacity
                                    - numDistributedMemorySegment);

            numDistributedMemorySegment += mySize;
            bufferPool.setNumBuffers(bufferPool.getNumberOfRequiredMemorySegments() + mySize);
        }

        assert (totalPartsUsed == totalCapacity);
        assert (numDistributedMemorySegment == memorySegmentsToDistribute);
    }

    private String getConfigDescription() {
        return String.format(
                "The total number of network buffers is currently set to %d of %d bytes each. "
                        + "You can increase this number by setting the configuration keys '%s', '%s', and '%s'",
                totalNumberOfMemorySegments,
                memorySegmentSize,
                TaskManagerOptions.NETWORK_MEMORY_FRACTION.key(),
                TaskManagerOptions.NETWORK_MEMORY_MIN.key(),
                TaskManagerOptions.NETWORK_MEMORY_MAX.key());
    }
}
