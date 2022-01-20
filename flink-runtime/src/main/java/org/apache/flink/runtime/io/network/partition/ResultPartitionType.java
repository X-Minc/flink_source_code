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

package org.apache.flink.runtime.io.network.partition;

/** Type of a result partition. */
public enum ResultPartitionType {

    /**
     * 阻塞分区表示阻塞数据交换，数据流首先被完全生成，然后被消耗。这是一个仅适用于有界流的选项，可用于有界流运行时和恢复。
     *
     * <p>阻塞分区可以被多次并发使用。
     *
     * <p>分区在被使用后不会自动释放（例如{@link #PIPELINED}分区），但只有在调度程序确定不再需要该分区时，才会通过调度程序释放。
     */
    BLOCKING(false, false, false, false, true),

    /**
     * BLOCKING_持久性分区类似于{@link #BLOCKING}分区，但具有用户指定的生命周期。
     *
     * <p>在对JobManager或ResourceManager进行显式API调用时，而不是由调度程序删除持久性分区。
     *
     * <p>否则，只有在故障处理场景中，如TaskManager退出或TaskManager长时间断开与JobManager ResourceManager的连接时，安全网才会删除分区。
     */
    BLOCKING_PERSISTENT(false, false, false, true, true),

    /**
     * 流水线流式数据交换。这适用于有界流和无界流。
     *
     * <p>管道化结果只能由单个使用者使用一次，并在使用流时自动进行处理。
     *
     * <p>与{@link #PIPELINED_BOUNDED}变量不同，此结果分区类型可以保留任意数量的数据。
     */
    PIPELINED(true, true, false, false, false),

    /**
     * 具有有界（本地）缓冲池的流水线分区。
     *
     * <p>对于流作业，缓冲池大小的固定限制应有助于避免缓冲过多数据和延迟检查点屏障。
     * 但是，与限制整个网络缓冲池大小不同，这仍然允许通过选择适当大的网络缓冲池大小来灵活地控制分区总数。
     *
     * <p>对于批处理作业，最好保持此无限制（{@link #PIPELINED}），因为没有检查点障碍。
     */
    PIPELINED_BOUNDED(true, true, true, false, false),

    /**
     * 具有有界（本地）缓冲池的流水线分区，支持下游任务在近似本地恢复中重新连接后继续使用数据。
     *
     * <p>流水线结果一次只能由单个使用者使用一次。{@link #PIPELINED_APPROXIMATE}不同于{@link #PIPELINED}和{@link #PIPELINED_BOUNDED}，因为{@link #PIPELINED_APPROXIMATE}分区可以在下游任务失败后重新连接。
     */
    PIPELINED_APPROXIMATE(true, true, true, false, true);

    /** 分区是否可以在生成时使用？ */
    private final boolean isPipelined;

    /** Does the partition produce back pressure when not consumed? */
    private final boolean hasBackPressure;

    /** Does this partition use a limited number of (network) buffers? */
    private final boolean isBounded;

    /** This partition will not be released after consuming if 'isPersistent' is true. */
    private final boolean isPersistent;

    /**
     * Can the partition be reconnected.
     *
     * <p>Attention: this attribute is introduced temporally for
     * ResultPartitionType.PIPELINED_APPROXIMATE It will be removed afterwards: TODO: 1. Approximate
     * local recovery has its won failover strategy to restart the failed set of tasks instead of
     * restarting downstream of failed tasks depending on {@code
     * RestartPipelinedRegionFailoverStrategy} 2. FLINK-19895: Unify the life cycle of
     * ResultPartitionType Pipelined Family
     */
    private final boolean isReconnectable;

    /** Specifies the behaviour of an intermediate result partition at runtime. */
    ResultPartitionType(
            boolean isPipelined,
            boolean hasBackPressure,
            boolean isBounded,
            boolean isPersistent,
            boolean isReconnectable) {
        this.isPipelined = isPipelined;
        this.hasBackPressure = hasBackPressure;
        this.isBounded = isBounded;
        this.isPersistent = isPersistent;
        this.isReconnectable = isReconnectable;
    }

    public boolean hasBackPressure() {
        return hasBackPressure;
    }

    public boolean isBlocking() {
        return !isPipelined;
    }

    public boolean isPipelined() {
        return isPipelined;
    }

    public boolean isReconnectable() {
        return isReconnectable;
    }

    /**
     * Whether this partition uses a limited number of (network) buffers or not.
     *
     * @return <tt>true</tt> if the number of buffers should be bound to some limit
     */
    public boolean isBounded() {
        return isBounded;
    }

    public boolean isPersistent() {
        return isPersistent;
    }
}
