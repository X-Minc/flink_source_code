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

package org.apache.flink.runtime.jobgraph;

import org.apache.flink.runtime.io.network.partition.ResultPartitionType;

import java.util.ArrayList;
import java.util.List;

import static org.apache.flink.util.Preconditions.checkNotNull;

/**
 * 中间数据集是由操作员生成的数据集-源或任何中间操作。
 *
 * <p>中间数据集可由其他运算符读取、物化或丢弃。
 */
public class IntermediateDataSet implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private final IntermediateDataSetID id; // the identifier

    private final JobVertex producer; // 产生这个数据集的操作（前一个顶点）

    private final List<JobEdge> consumers = new ArrayList<JobEdge>();

    // 运行时要使用的分区类型
    private final ResultPartitionType resultType;

    // --------------------------------------------------------------------------------------------

    public IntermediateDataSet(
            IntermediateDataSetID id, ResultPartitionType resultType, JobVertex producer) {
        this.id = checkNotNull(id);
        this.producer = checkNotNull(producer);
        this.resultType = checkNotNull(resultType);
    }

    // --------------------------------------------------------------------------------------------

    public IntermediateDataSetID getId() {
        return id;
    }

    public JobVertex getProducer() {
        return producer;
    }

    public List<JobEdge> getConsumers() {
        return this.consumers;
    }

    public ResultPartitionType getResultType() {
        return resultType;
    }

    // --------------------------------------------------------------------------------------------

    public void addConsumer(JobEdge edge) {
        this.consumers.add(edge);
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Intermediate Data Set (" + id + ")";
    }
}
