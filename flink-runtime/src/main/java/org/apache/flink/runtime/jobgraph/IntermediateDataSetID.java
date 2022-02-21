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

import org.apache.flink.runtime.topology.ResultID;
import org.apache.flink.util.AbstractID;

import org.apache.flink.shaded.netty4.io.netty.buffer.ByteBuf;

import java.util.UUID;

/** 标识 {@link IntermediateDataSet} 的 ID。*/
public class IntermediateDataSetID extends AbstractID implements ResultID {

    private static final long serialVersionUID = 1L;

    /** 创建一个新的随机中间数据集 ID。 */
    public IntermediateDataSetID() {
        super();
    }

    /**
     * 使用给定 ID 的字节创建一个新的中间数据集 ID。
     *
     * @param from The ID to create this ID from.
     */
    public IntermediateDataSetID(AbstractID from) {
        super(from);
    }

    /**
     * 使用给定 UUID 的字节创建一个新的中间数据集 ID。
     *
     * @param from The UUID to create this ID from.
     */
    public IntermediateDataSetID(UUID from) {
        super(from.getLeastSignificantBits(), from.getMostSignificantBits());
    }

    private IntermediateDataSetID(long lower, long upper) {
        super(lower, upper);
    }

    public void writeTo(ByteBuf buf) {
        buf.writeLong(lowerPart);
        buf.writeLong(upperPart);
    }

    public static IntermediateDataSetID fromByteBuf(ByteBuf buf) {
        final long lower = buf.readLong();
        final long upper = buf.readLong();
        return new IntermediateDataSetID(lower, upper);
    }
}