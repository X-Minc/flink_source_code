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

import org.apache.flink.core.memory.MemorySegment;
import org.apache.flink.util.ExceptionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.apache.flink.util.Preconditions.checkArgument;
import static org.apache.flink.util.Preconditions.checkState;
import static org.apache.flink.util.concurrent.FutureUtils.assertNoException;

/**
 * 一个缓冲池，用于管理来自 {@link NetworkBufferPool} 的多个 {@link Buffer} 实例。
 *
 * <p>缓冲区请求被调解到网络缓冲池，以通过限制每个本地缓冲池的缓冲区数量来确保网络堆栈的无死锁操作。
 * 它还实现了缓冲区回收的默认机制，确保每个缓冲区最终都返回到网络缓冲池。
 *
 * <p>此池的大小可以在运行时动态更改（{@link #setNumBuffers(int)}。
 * 然后它会延迟将所需数量的缓冲区返回给 {@link NetworkBufferPool} 以匹配其新大小。
 *
 * <p>可用性被定义为在后续的 {@link #requestBuffer()} {@link #requestBufferBuilder()} 上返回一个段并提升一个非阻塞 {@link #requestBufferBuilderBlocking(int)}。特别是，
 *
 * <ul>
 *   <li>至少有一个 {@link #availableMemorySegments}。
 *   <li>未达到任何子分区{@link #maxBuffersPerChannel}.
 * </ul>
 *
 * <p>为了确保这个契约，只要它没有达到 {@link #maxNumberOfMemorySegments} 或一个子分区达到配额，实现就会急切地从 {@link NetworkBufferPool} 获取额外的内存段。
 */
class LocalBufferPool implements BufferPool {
    private static final Logger LOG = LoggerFactory.getLogger(LocalBufferPool.class);

    private static final int UNKNOWN_CHANNEL = -1;

    /** 从中获取缓冲区的全局网络缓冲池。 */
    private final NetworkBufferPool networkBufferPool;

    /** 此池所需的最小段数。 初始化时为网络缓冲区的最小数量 */
    private final int numberOfRequiredMemorySegments;

    /**
     * 当前可用的内存段。这些是从网络缓冲池中请求的段，当前没有作为 Buffer 实例分发。
     *
     * <p><strong>当心:</strong> 特别注意这个锁和进入这个类之前获得的锁之间的交互，
     * 而不是在调用这个类内部的外部代码时获得的锁，
     * 例如通过{@link #registeredListeners}回调使用{@link org.apache.flink.runtime.io.network.partition.consumer.BufferManager bufferQueue}。
     */
    private final ArrayDeque<MemorySegment> availableMemorySegments =
            new ArrayDeque<MemorySegment>();

    /**
     * 缓冲区可用性侦听器，当缓冲区可用时需要通知。
     * 监听器只能在没有 Buffer 实例可用的时间状态下注册。
     */
    private final ArrayDeque<BufferListener> registeredListeners = new ArrayDeque<>();

    /** 要分配的最大网络缓冲区数。 */
    private final int maxNumberOfMemorySegments;

    /** 此池的当前大小。 初始化时网络缓冲区的最小数量 */
    @GuardedBy("availableMemorySegments")
    private int currentPoolSize;

    /**
     * 所有内存段的数量，
     * 已从网络缓冲池请求并通过此池以某种方式引用（例如，包装在 Buffer 实例中或作为可用段）。
     */
    @GuardedBy("availableMemorySegments")
    private int numberOfRequestedMemorySegments;

    private final int maxBuffersPerChannel;

    @GuardedBy("availableMemorySegments")
    private final int[] subpartitionBuffersCount;

    /** 子分区缓冲区回收器数组中具有n个LocalBufferPool。 */
    private final BufferRecycler[] subpartitionBufferRecyclers;

    @GuardedBy("availableMemorySegments")
    private int unavailableSubpartitionsCount = 0;

    @GuardedBy("availableMemorySegments")
    private boolean isDestroyed;

    @GuardedBy("availableMemorySegments")
    private final AvailabilityHelper availabilityHelper = new AvailabilityHelper();

    @GuardedBy("availableMemorySegments")
    private boolean requestingWhenAvailable;

    /**
     * 基于给定 <tt>networkBufferPool<tt> 的本地缓冲池，可用的网络缓冲区数量最少。
     *
     * @param networkBufferPool global network buffer pool to get buffers from
     * @param numberOfRequiredMemorySegments minimum number of network buffers
     */
    LocalBufferPool(NetworkBufferPool networkBufferPool, int numberOfRequiredMemorySegments)
            throws IOException {
        this(
                networkBufferPool,
                numberOfRequiredMemorySegments,
                Integer.MAX_VALUE,
                0,
                Integer.MAX_VALUE);
    }

    /**
     * Local buffer pool based on the given <tt>networkBufferPool</tt> with a minimal and maximal
     * number of network buffers being available.
     *
     * @param networkBufferPool global network buffer pool to get buffers from
     * @param numberOfRequiredMemorySegments minimum number of network buffers
     * @param maxNumberOfMemorySegments maximum number of network buffers to allocate
     */
    LocalBufferPool(
            NetworkBufferPool networkBufferPool,
            int numberOfRequiredMemorySegments,
            int maxNumberOfMemorySegments)
            throws Exception {
        this(
                networkBufferPool,
                numberOfRequiredMemorySegments,
                maxNumberOfMemorySegments,
                0,
                Integer.MAX_VALUE);
    }

    /**
     * Local buffer pool based on the given <tt>networkBufferPool</tt> and <tt>bufferPoolOwner</tt>
     * with a minimal and maximal number of network buffers being available.
     *
     * @param networkBufferPool 从中获取缓冲区的全局网络缓冲池
     * @param numberOfRequiredMemorySegments 网络缓冲区的最小数量
     * @param maxNumberOfMemorySegments 要分配的最大网络缓冲区数
     * @param numberOfSubpartitions 子分区数
     * @param maxBuffersPerChannel 每个通道使用的最大缓冲区数
     */
    LocalBufferPool(
            NetworkBufferPool networkBufferPool,
            int numberOfRequiredMemorySegments,
            int maxNumberOfMemorySegments,
            int numberOfSubpartitions,
            int maxBuffersPerChannel)
            throws IOException {
        checkArgument(
                numberOfRequiredMemorySegments > 0,
                "Required number of memory segments (%s) should be larger than 0.",
                numberOfRequiredMemorySegments);

        checkArgument(
                maxNumberOfMemorySegments >= numberOfRequiredMemorySegments,
                "Maximum number of memory segments (%s) should not be smaller than minimum (%s).",
                maxNumberOfMemorySegments,
                numberOfRequiredMemorySegments);

        LOG.debug(
                "Using a local buffer pool with {}-{} buffers",
                numberOfRequiredMemorySegments,
                maxNumberOfMemorySegments);

        this.networkBufferPool = networkBufferPool;
        this.numberOfRequiredMemorySegments = numberOfRequiredMemorySegments;
        this.currentPoolSize = numberOfRequiredMemorySegments;
        this.maxNumberOfMemorySegments = maxNumberOfMemorySegments;

        if (numberOfSubpartitions > 0) {
            checkArgument(
                    maxBuffersPerChannel > 0,
                    "Maximum number of buffers for each channel (%s) should be larger than 0.",
                    maxBuffersPerChannel);
        }

        this.subpartitionBuffersCount = new int[numberOfSubpartitions];
        subpartitionBufferRecyclers = new BufferRecycler[numberOfSubpartitions];
        for (int i = 0; i < subpartitionBufferRecyclers.length; i++) {
            subpartitionBufferRecyclers[i] = new SubpartitionBufferRecycler(i, this);
        }
        this.maxBuffersPerChannel = maxBuffersPerChannel;

        // 由于checkAvailability断言了锁，所以只获取锁。这是对线程安全的一个小小的惩罚。
        synchronized (this.availableMemorySegments) {
            if (checkAvailability()) {
                availabilityHelper.resetAvailable();
            }

            checkConsistentAvailability();
        }
    }

    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------

    @Override
    public void reserveSegments(int numberOfSegmentsToReserve) throws IOException {
        checkArgument(
                numberOfSegmentsToReserve <= numberOfRequiredMemorySegments,
                "Can not reserve more segments than number of required segments.");

        CompletableFuture<?> toNotify = null;
        synchronized (availableMemorySegments) {
            checkState(!isDestroyed, "Buffer pool has been destroyed.");

            if (numberOfRequestedMemorySegments < numberOfSegmentsToReserve) {
                availableMemorySegments.addAll(
                        networkBufferPool.requestMemorySegmentsBlocking(
                                numberOfSegmentsToReserve - numberOfRequestedMemorySegments));
                toNotify = availabilityHelper.getUnavailableToResetAvailable();
            }
        }
        mayNotifyAvailable(toNotify);
    }

    @Override
    public boolean isDestroyed() {
        synchronized (availableMemorySegments) {
            return isDestroyed;
        }
    }

    @Override
    public int getNumberOfRequiredMemorySegments() {
        return numberOfRequiredMemorySegments;
    }

    @Override
    public int getMaxNumberOfMemorySegments() {
        return maxNumberOfMemorySegments;
    }

    @Override
    public int getNumberOfAvailableMemorySegments() {
        synchronized (availableMemorySegments) {
            return availableMemorySegments.size();
        }
    }

    @Override
    public int getNumBuffers() {
        synchronized (availableMemorySegments) {
            return currentPoolSize;
        }
    }

    @Override
    public int bestEffortGetNumOfUsedBuffers() {
        return Math.max(0, numberOfRequestedMemorySegments - availableMemorySegments.size());
    }

    @Override
    public Buffer requestBuffer() {
        return toBuffer(requestMemorySegment());
    }

    @Override
    public BufferBuilder requestBufferBuilder() {
        return toBufferBuilder(requestMemorySegment(UNKNOWN_CHANNEL), UNKNOWN_CHANNEL);
    }

    @Override
    public BufferBuilder requestBufferBuilder(int targetChannel) {
        return toBufferBuilder(requestMemorySegment(targetChannel), targetChannel);
    }

    @Override
    public BufferBuilder requestBufferBuilderBlocking() throws InterruptedException {
        return toBufferBuilder(requestMemorySegmentBlocking(), UNKNOWN_CHANNEL);
    }

    @Override
    public MemorySegment requestMemorySegmentBlocking() throws InterruptedException {
        return requestMemorySegmentBlocking(UNKNOWN_CHANNEL);
    }

    @Override
    public BufferBuilder requestBufferBuilderBlocking(int targetChannel)
            throws InterruptedException {
        return toBufferBuilder(requestMemorySegmentBlocking(targetChannel), targetChannel);
    }

    private Buffer toBuffer(MemorySegment memorySegment) {
        if (memorySegment == null) {
            return null;
        }
        return new NetworkBuffer(memorySegment, this);
    }

    private BufferBuilder toBufferBuilder(MemorySegment memorySegment, int targetChannel) {
        if (memorySegment == null) {
            return null;
        }

        if (targetChannel == UNKNOWN_CHANNEL) {
            return new BufferBuilder(memorySegment, this);
        } else {
            return new BufferBuilder(memorySegment, subpartitionBufferRecyclers[targetChannel]);
        }
    }

    private MemorySegment requestMemorySegmentBlocking(int targetChannel)
            throws InterruptedException {
        MemorySegment segment;
        while ((segment = requestMemorySegment(targetChannel)) == null) {
            try {
                // wait until available
                getAvailableFuture().get();
            } catch (ExecutionException e) {
                LOG.error("The available future is completed exceptionally.", e);
                ExceptionUtils.rethrow(e);
            }
        }
        return segment;
    }

    @Nullable
    private MemorySegment requestMemorySegment(int targetChannel) {
        MemorySegment segment;
        synchronized (availableMemorySegments) {
            if (isDestroyed) {
                throw new IllegalStateException("Buffer pool is destroyed.");
            }

            // target channel over quota; do not return a segment
            if (targetChannel != UNKNOWN_CHANNEL
                    && subpartitionBuffersCount[targetChannel] >= maxBuffersPerChannel) {
                return null;
            }

            segment = availableMemorySegments.poll();

            if (segment == null) {
                return null;
            }

            if (targetChannel != UNKNOWN_CHANNEL) {
                if (++subpartitionBuffersCount[targetChannel] == maxBuffersPerChannel) {
                    unavailableSubpartitionsCount++;
                }
            }

            if (!checkAvailability()) {
                availabilityHelper.resetUnavailable();
            }

            checkConsistentAvailability();
        }
        return segment;
    }

    @Override
    public MemorySegment requestMemorySegment() {
        return requestMemorySegment(UNKNOWN_CHANNEL);
    }

    /**
     * 从全局请求内存段
     *
     * @return 是否请求到内存块
     */
    private boolean requestMemorySegmentFromGlobal() {
        assert Thread.holdsLock(availableMemorySegments);

        /**
         *  如果所拥有内存段的数量{@link #numberOfRequestedMemorySegments} 大于等于当前缓冲池最小数量{@link #currentPoolSize}，
         *  则不需要向{@link  NetworkBufferPool}请求内存段
         */
        if (isRequestedSizeReached()) {
            return false;
        }

        /**
         * 如果所拥有内存段的数量未达到当前缓冲池最小数量，
         * 则需要向{@link NetworkBufferPool}请求内存段直至所用有的内存段达到当前缓冲池最小的内存段数量
         */
        checkState(
                !isDestroyed,
                "Destroyed buffer pools should never acquire segments - this will lead to buffer leaks.");

        MemorySegment segment = networkBufferPool.requestMemorySegment();
        if (segment != null) {
            availableMemorySegments.add(segment);
            numberOfRequestedMemorySegments++;
            return true;
        }
        return false;
    }

    /**
     * 一旦有一个池可用，就会尝试从全局池中获取缓冲区。
     * 请注意，多个 {@link LocalBufferPool} 可能会等待全局池的未来，
     * 因此此方法会在新缓冲区可用时仔细检查是否真的需要新缓冲区。
     */
    private void requestMemorySegmentFromGlobalWhenAvailable() {
        assert Thread.holdsLock(availableMemorySegments);

        if (requestingWhenAvailable) {
            return;
        }
        requestingWhenAvailable = true;

        assertNoException(
                networkBufferPool.getAvailableFuture().thenRun(this::onGlobalPoolAvailable));
    }

    private void onGlobalPoolAvailable() {
        CompletableFuture<?> toNotify = null;
        synchronized (availableMemorySegments) {
            requestingWhenAvailable = false;
            if (isDestroyed || availabilityHelper.isApproximatelyAvailable()) {
                // 目前没有从global获得缓冲的好处；先例
                return;
            }

            // Check availability and potentially request the memory segment. The call may also
            // result in invoking
            // #requestMemorySegmentFromGlobalWhenAvailable again if no segment could be fetched
            // because of
            // concurrent requests from different LocalBufferPools.
            if (checkAvailability()) {
                toNotify = availabilityHelper.getUnavailableToResetAvailable();
            }
        }
        mayNotifyAvailable(toNotify);
    }

    private boolean shouldBeAvailable() {
        assert Thread.holdsLock(availableMemorySegments);

        return !availableMemorySegments.isEmpty() && unavailableSubpartitionsCount == 0;
    }

    //检查内存块是否可用
    private boolean checkAvailability() {
        /**
         *    断言{@link #availableMemorySegments}存在锁
         *    若不存在则抛出异常
         */
        assert Thread.holdsLock(availableMemorySegments);

        //当前可用的有效内存块集合不为空
        if (!availableMemorySegments.isEmpty()) {
            return unavailableSubpartitionsCount == 0;
        }
        /**
         * 如果所拥有内存段的数量未达到当前缓冲池最小数量，
         * 则需要向{@link NetworkBufferPool}请求内存段直至所用有的内存段达到当前缓冲池最小的内存段数量
         */
        if (!isRequestedSizeReached()) {
            if (requestMemorySegmentFromGlobal()) {
                return unavailableSubpartitionsCount == 0;
            } else {
                requestMemorySegmentFromGlobalWhenAvailable();
                return shouldBeAvailable();
            }
        }
        return false;
    }

    private void checkConsistentAvailability() {
        assert Thread.holdsLock(availableMemorySegments);

        final boolean shouldBeAvailable = shouldBeAvailable();
        checkState(
                availabilityHelper.isApproximatelyAvailable() == shouldBeAvailable,
                "Inconsistent availability: expected " + shouldBeAvailable);
    }

    @Override
    public void recycle(MemorySegment segment) {
        recycle(segment, UNKNOWN_CHANNEL);
    }

    private void recycle(MemorySegment segment, int channel) {
        BufferListener listener;
        CompletableFuture<?> toNotify = null;
        do {
            synchronized (availableMemorySegments) {
                if (channel != UNKNOWN_CHANNEL) {
                    if (subpartitionBuffersCount[channel]-- == maxBuffersPerChannel) {
                        unavailableSubpartitionsCount--;
                    }
                }

                if (isDestroyed || hasExcessBuffers()) {
                    returnMemorySegment(segment);
                    return;
                } else {
                    listener = registeredListeners.poll();
                    if (listener == null) {
                        availableMemorySegments.add(segment);
                        // only need to check unavailableSubpartitionsCount here because
                        // availableMemorySegments is not empty
                        if (!availabilityHelper.isApproximatelyAvailable()
                                && unavailableSubpartitionsCount == 0) {
                            toNotify = availabilityHelper.getUnavailableToResetAvailable();
                        }
                        break;
                    }
                }

                checkConsistentAvailability();
            }
        } while (!fireBufferAvailableNotification(listener, segment));

        mayNotifyAvailable(toNotify);
    }

    private boolean fireBufferAvailableNotification(
            BufferListener listener, MemorySegment segment) {
        // We do not know which locks have been acquired before the recycle() or are needed in the
        // notification and which other threads also access them.
        // -> call notifyBufferAvailable() outside of the synchronized block to avoid a deadlock
        // (FLINK-9676)
        return listener.notifyBufferAvailable(new NetworkBuffer(segment, this));
    }

    /** Destroy is called after the produce or consume phase of a task finishes. */
    @Override
    public void lazyDestroy() {
        // 注意：如果您更改此逻辑，请务必更新 recycle() ！
        CompletableFuture<?> toNotify = null;
        synchronized (availableMemorySegments) {
            if (!isDestroyed) {
                MemorySegment segment;
                while ((segment = availableMemorySegments.poll()) != null) {
                    returnMemorySegment(segment);
                }

                BufferListener listener;
                while ((listener = registeredListeners.poll()) != null) {
                    listener.notifyBufferDestroyed();
                }

                if (!isAvailable()) {
                    toNotify = availabilityHelper.getAvailableFuture();
                }

                isDestroyed = true;
            }
        }

        mayNotifyAvailable(toNotify);

        networkBufferPool.destroyBufferPool(this);
    }

    @Override
    public boolean addBufferListener(BufferListener listener) {
        synchronized (availableMemorySegments) {
            if (!availableMemorySegments.isEmpty() || isDestroyed) {
                return false;
            }

            registeredListeners.add(listener);
            return true;
        }
    }

    @Override
    public void setNumBuffers(int numBuffers) {
        CompletableFuture<?> toNotify = null;
        synchronized (availableMemorySegments) {
            checkArgument(
                    numBuffers >= numberOfRequiredMemorySegments,
                    "Buffer pool needs at least %s buffers, but tried to set to %s",
                    numberOfRequiredMemorySegments,
                    numBuffers);

            currentPoolSize = Math.min(numBuffers, maxNumberOfMemorySegments);

            returnExcessMemorySegments();

            if (isDestroyed) {
                // FLINK-19964: when two local buffer pools are released concurrently, one of them
                // gets buffers assigned
                // make sure that checkAvailability is not called as it would pro-actively acquire
                // one buffer from NetworkBufferPool
                return;
            }

            if (checkAvailability()) {
                toNotify = availabilityHelper.getUnavailableToResetAvailable();
            } else {
                availabilityHelper.resetUnavailable();
            }

            checkConsistentAvailability();
        }

        mayNotifyAvailable(toNotify);
    }

    @Override
    public CompletableFuture<?> getAvailableFuture() {
        return availabilityHelper.getAvailableFuture();
    }

    @Override
    public String toString() {
        synchronized (availableMemorySegments) {
            return String.format(
                    "[size: %d, required: %d, requested: %d, available: %d, max: %d, listeners: %d,"
                            + "subpartitions: %d, maxBuffersPerChannel: %d, destroyed: %s]",
                    currentPoolSize,
                    numberOfRequiredMemorySegments,
                    numberOfRequestedMemorySegments,
                    availableMemorySegments.size(),
                    maxNumberOfMemorySegments,
                    registeredListeners.size(),
                    subpartitionBuffersCount.length,
                    maxBuffersPerChannel,
                    isDestroyed);
        }
    }

    // ------------------------------------------------------------------------

    /**
     * Notifies the potential segment consumer of the new available segments by completing the
     * previous uncompleted future.
     */
    private void mayNotifyAvailable(@Nullable CompletableFuture<?> toNotify) {
        if (toNotify != null) {
            toNotify.complete(null);
        }
    }

    private void returnMemorySegment(MemorySegment segment) {
        assert Thread.holdsLock(availableMemorySegments);

        numberOfRequestedMemorySegments--;
        networkBufferPool.recycle(segment);
    }

    private void returnExcessMemorySegments() {
        assert Thread.holdsLock(availableMemorySegments);

        while (hasExcessBuffers()) {
            MemorySegment segment = availableMemorySegments.poll();
            if (segment == null) {
                return;
            }

            returnMemorySegment(segment);
        }
    }

    private boolean hasExcessBuffers() {
        return numberOfRequestedMemorySegments > currentPoolSize;
    }

    private boolean isRequestedSizeReached() {
        return numberOfRequestedMemorySegments >= currentPoolSize;
    }

    private static class SubpartitionBufferRecycler implements BufferRecycler {

        private int channel;
        private LocalBufferPool bufferPool;

        SubpartitionBufferRecycler(int channel, LocalBufferPool bufferPool) {
            this.channel = channel;
            this.bufferPool = bufferPool;
        }

        @Override
        public void recycle(MemorySegment memorySegment) {
            bufferPool.recycle(memorySegment, channel);
        }
    }
}
