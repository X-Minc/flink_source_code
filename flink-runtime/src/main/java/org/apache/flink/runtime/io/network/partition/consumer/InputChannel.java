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

package org.apache.flink.runtime.io.network.partition.consumer;

import org.apache.flink.metrics.Counter;
import org.apache.flink.runtime.checkpoint.CheckpointException;
import org.apache.flink.runtime.checkpoint.channel.InputChannelInfo;
import org.apache.flink.runtime.event.TaskEvent;
import org.apache.flink.runtime.execution.CancelTaskException;
import org.apache.flink.runtime.io.network.api.CheckpointBarrier;
import org.apache.flink.runtime.io.network.api.EndOfData;
import org.apache.flink.runtime.io.network.buffer.Buffer;
import org.apache.flink.runtime.io.network.partition.PartitionException;
import org.apache.flink.runtime.io.network.partition.ResultPartitionID;
import org.apache.flink.runtime.io.network.partition.ResultSubpartitionView;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.flink.util.Preconditions.checkArgument;
import static org.apache.flink.util.Preconditions.checkNotNull;

/**
 * 输入通道使用单个{@link ResultSubpartitionView}。
 *
 * <p>对于每个渠道，消费生命周期如下所示：
 *
 * <ol>
 *   <li>{@link #requestSubpartition(int)}
 *   <li>{@link #getNextBuffer()}
 *   <li>{@link #releaseAllResources()}
 * </ol>
 */
public abstract class InputChannel {
    /** 用于在任务中全局标识输入通道的信息。 */
    protected final InputChannelInfo channelInfo;

    protected final ResultPartitionID partitionId;

    protected final SingleInputGate inputGate;

    // - Asynchronous error notification --------------------------------------

    private final AtomicReference<Throwable> cause = new AtomicReference<Throwable>();

    // - Partition request backoff --------------------------------------------

    /** 初始退避（毫秒）。 */
    protected final int initialBackoff;

    /** 最大回退（毫秒）。 */
    protected final int maxBackoff;

    protected final Counter numBytesIn;

    protected final Counter numBuffersIn;

    /** The current backoff (in ms). */
    private int currentBackoff;

    protected InputChannel(
            SingleInputGate inputGate,
            int channelIndex,
            ResultPartitionID partitionId,
            int initialBackoff,
            int maxBackoff,
            Counter numBytesIn,
            Counter numBuffersIn) {

        checkArgument(channelIndex >= 0);

        int initial = initialBackoff;
        int max = maxBackoff;

        checkArgument(initial >= 0 && initial <= max);

        this.inputGate = checkNotNull(inputGate);
        this.channelInfo = new InputChannelInfo(inputGate.getGateIndex(), channelIndex);
        this.partitionId = checkNotNull(partitionId);

        this.initialBackoff = initial;
        this.maxBackoff = max;
        this.currentBackoff = initial == 0 ? -1 : 0;

        this.numBytesIn = numBytesIn;
        this.numBuffersIn = numBuffersIn;
    }

    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------

    /** 返回此通道在其{@link SingleInputGate}内的索引。 */
    public int getChannelIndex() {
        return channelInfo.getInputChannelIdx();
    }

    /**
     * 返回此通道的信息，该信息根据其运算符实例唯一标识通道。
     */
    public InputChannelInfo getChannelInfo() {
        return channelInfo;
    }

    public ResultPartitionID getPartitionId() {
        return partitionId;
    }

    /**
     * 在发送恰好一次模式的{@link org.apache.flink.runtime.io.network.api.CheckpointBarrier}后，
     * 上游将被阻塞并变得不可用。此方法尝试取消阻止相应的上游并恢复数据消耗。
     */
    public abstract void resumeConsumption() throws IOException;

    /**
     * 当从一个通道接收到{@link EndOfData}时，需要在处理此事件后进行确认。
     */
    public abstract void acknowledgeAllRecordsProcessed() throws IOException;

    /**
     * 通知拥有者{@link SingleInputGate}此通道变为非空。
     *
     * <p>只有在将缓冲区添加到以前为空的输入通道时，才能保证调用此函数。
     * 当从此通道轮询下一个缓冲区时，空的概念在原子上与标志{@link BufferAndAvailability #moreAvailable()}一致。
     *
     * <p><b>笔记:</b> 当输入通道观察到异常时，不管通道之前是否为空，都会调用此方法。这样可以确保父InputGate始终收到异常通知。
     */
    protected void notifyChannelNonEmpty() {
        inputGate.notifyChannelNonEmpty(this);
    }

    public void notifyPriorityEvent(int priorityBufferNumber) {
        inputGate.notifyPriorityEvent(this, priorityBufferNumber);
    }

    protected void notifyBufferAvailable(int numAvailableBuffers) throws IOException {
    }

    // ------------------------------------------------------------------------
    // Consume
    // ------------------------------------------------------------------------

    /**
     * 使用源中间结果分区的指定索引请求队列。
     *
     * <p>请求的队列索引取决于通道所属的子任务，并由该通道的使用者指定。
     */
    abstract void requestSubpartition(int subpartitionIndex)
            throws IOException, InterruptedException;

    /**
     * 返回已使用子分区的下一个缓冲区，
     * 如果没有要返回的数据，则返回{@code Optional.empty（）}。
     */
    abstract Optional<BufferAndAvailability> getNextBuffer()
            throws IOException, InterruptedException;

    /**
     * 启动检查点时由任务线程调用（例如，任何接收到的输入通道）。
     */
    public void checkpointStarted(CheckpointBarrier barrier) throws CheckpointException {
    }

    /** 在cancel/complete上由任务线程调用以清理临时数据。 */
    public void checkpointStopped(long checkpointId) {
    }

    public void convertToPriorityEvent(int sequenceNumber) throws IOException {
    }

    // ------------------------------------------------------------------------
    // Task events
    // ------------------------------------------------------------------------

    /**
     * 将{@link TaskEvent}发送回生成已使用结果分区的任务。
     *
     * <p><strong>重要的</strong>:
     * 生产任务必须运行才能接收向后的事件。
     * 这意味着结果类型需要管道化，任务逻辑必须确保生产者将等待所有向后的事件。
     * 否则，这将导致运行时出现异常。
     */
    abstract void sendTaskEvent(TaskEvent event) throws IOException;

    // ------------------------------------------------------------------------
    // Life cycle
    // ------------------------------------------------------------------------

    abstract boolean isReleased();

    /** 释放频道的所有资源。 */
    abstract void releaseAllResources() throws IOException;

    abstract void announceBufferSize(int newBufferSize);

    abstract int getBuffersInUseCount();

    // ------------------------------------------------------------------------
    // Error notification
    // ------------------------------------------------------------------------

    /**
     * 检查是否存在错误，如果报告了错误，则重新执行。
     *
     * <p>注意：不应转换任何{@link PartitionException}实例，
     * 并确保它们在任务失败原因中始终可见。
     */
    protected void checkError() throws IOException {
        final Throwable t = cause.get();

        if (t != null) {
            if (t instanceof CancelTaskException) {
                throw (CancelTaskException) t;
            }
            if (t instanceof IOException) {
                throw (IOException) t;
            } else {
                throw new IOException(t);
            }
        }
    }

    /**
     * 原子地为此通道设置错误，并将可用数据通知输入门，以触发任务线程查询此通道。
     */
    protected void setError(Throwable cause) {
        if (this.cause.compareAndSet(null, checkNotNull(cause))) {
            // 通知输入门。
            notifyChannelNonEmpty();
        }
    }

    // ------------------------------------------------------------------------
    // Partition request exponential backoff
    // ------------------------------------------------------------------------

    /** 以毫秒为单位返回当前回退。 */
    protected int getCurrentBackoff() {
        return currentBackoff <= 0 ? 0 : currentBackoff;
    }

    /**
     * 增加当前退避并返回操作是否成功。
     *
     * @return <code>true<code>，如果操作成功。否则，<code>false<code>。
     */
    protected boolean increaseBackoff() {
        // Backoff is disabled
        if (currentBackoff < 0) {
            return false;
        }

        // This is the first time backing off
        if (currentBackoff == 0) {
            currentBackoff = initialBackoff;

            return true;
        }

        // Continue backing off
        else if (currentBackoff < maxBackoff) {
            currentBackoff = Math.min(currentBackoff * 2, maxBackoff);

            return true;
        }

        // Reached maximum backoff
        return false;
    }

    // ------------------------------------------------------------------------
    // Metric related method
    // ------------------------------------------------------------------------

    public int unsynchronizedGetNumberOfQueuedBuffers() {
        return 0;
    }

    // ------------------------------------------------------------------------

    /**
     * {@link Buffer}和一个标志的组合，该标志指示进一步缓冲区的可用性，而backlog长度指示子分区中有多少非事件缓冲区可用。
     */
    public static final class BufferAndAvailability {

        private final Buffer buffer;
        private final Buffer.DataType nextDataType;
        private final int buffersInBacklog;
        private final int sequenceNumber;

        public BufferAndAvailability(
                Buffer buffer,
                Buffer.DataType nextDataType,
                int buffersInBacklog,
                int sequenceNumber) {
            this.buffer = checkNotNull(buffer);
            this.nextDataType = checkNotNull(nextDataType);
            this.buffersInBacklog = buffersInBacklog;
            this.sequenceNumber = sequenceNumber;
        }

        public Buffer buffer() {
            return buffer;
        }

        public boolean moreAvailable() {
            return nextDataType != Buffer.DataType.NONE;
        }

        public boolean morePriorityEvents() {
            return nextDataType.hasPriority();
        }

        public int buffersInBacklog() {
            return buffersInBacklog;
        }

        public boolean hasPriority() {
            return buffer.getDataType().hasPriority();
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }

        @Override
        public String toString() {
            return "BufferAndAvailability{"
                    + "buffer="
                    + buffer
                    + ", nextDataType="
                    + nextDataType
                    + ", buffersInBacklog="
                    + buffersInBacklog
                    + ", sequenceNumber="
                    + sequenceNumber
                    + '}';
        }
    }

    void setup() throws IOException {
    }
}
