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

package org.apache.flink.api.common.io;

import org.apache.flink.annotation.Public;
import org.apache.flink.api.common.io.statistics.BaseStatistics;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.io.InputSplit;
import org.apache.flink.core.io.InputSplitAssigner;
import org.apache.flink.core.io.InputSplitSource;

import java.io.IOException;
import java.io.Serializable;

/**
 * 生成记录的数据源的基本接口。
 *
 * <p>输入格式处理以下内容：
 *
 * <ul>
 * <li>它描述了如何将输入拆分为可以并行处理的拆分。
 * <li>它描述了如何从输入拆分中读取记录。
 * <li>它描述了如何从输入中收集基本统计信息。
 * <ul>
 *
 * <p>输入格式的生命周期如下所示：
 *
 * <ol>
 * <li>在实例化（无参数）之后，它被配置为一个{@link Configuration}对象。如果格式将文件描述为输入，则从配置中读取基本字段，例如文件路径。
 * <li>可选：编译器调用它以生成有关输入的基本统计信息。
 * <li>调用它来创建输入拆分
 * <li>每个并行输入任务都会创建一个实例，对其进行配置并将其打开以进行特定拆分
 * <li>从输入读取所有记录
 * <li>输入格式关闭
 * <ol>
 *
 * <p>重要提示：输入格式的编写必须确保实例关闭后可以再次打开。
 * 这是因为输入格式用于潜在的多个拆分。
 * 分割完成后，将调用格式的close函数，如果另一个分割可用，则随后将调用open函数进行下一次分割。
 *
 * @param <OT> 生成的记录的类型。
 * @param <T> 输入拆分的类型。
 *         ·
 *
 * @see InputSplit
 * @see BaseStatistics
 */
@Public
public interface InputFormat<OT, T extends InputSplit> extends InputSplitSource<T>, Serializable {

    /**
     * 配置此输入格式。由于输入格式是通用实例化的，
     * 因此无参数，因此该方法是输入格式基于配置值设置其基本字段的地方。
     *
     * <p>对于新实例化的输入格式，总是首先调用此方法。
     *
     * @param parameters 包含所有参数的配置（注意：不是Flink配置，而是TaskConfig）。
     */
    void configure(Configuration parameters);

    /**
     * Gets the basic statistics from the input described by this format. If the input format does
     * not know how to create those statistics, it may return null. This method optionally gets a
     * cached version of the statistics. The input format may examine them and decide whether it
     * directly returns them without spending effort to re-gather the statistics.
     *
     * <p>When this method is called, the input format is guaranteed to be configured.
     *
     * @param cachedStatistics The statistics that were cached. May be null.
     *
     * @return The base statistics for the input, or null, if not available.
     */
    BaseStatistics getStatistics(BaseStatistics cachedStatistics) throws IOException;

    // --------------------------------------------------------------------------------------------

    @Override
    T[] createInputSplits(int minNumSplits) throws IOException;

    @Override
    InputSplitAssigner getInputSplitAssigner(T[] inputSplits);

    // --------------------------------------------------------------------------------------------

    /**
     * Opens a parallel instance of the input format to work on a split.
     *
     * <p>When this method is called, the input format it guaranteed to be configured.
     *
     * @param split The split to be opened.
     *
     * @throws IOException Thrown, if the spit could not be opened due to an I/O problem.
     */
    void open(T split) throws IOException;

    /**
     * Method used to check if the end of the input is reached.
     *
     * <p>When this method is called, the input format it guaranteed to be opened.
     *
     * @return True if the end is reached, otherwise false.
     *
     * @throws IOException Thrown, if an I/O error occurred.
     */
    boolean reachedEnd() throws IOException;

    /**
     * Reads the next record from the input.
     *
     * <p>When this method is called, the input format it guaranteed to be opened.
     *
     * @param reuse Object that may be reused.
     *
     * @return Read record.
     *
     * @throws IOException Thrown, if an I/O error occurred.
     */
    OT nextRecord(OT reuse) throws IOException;

    /**
     * Method that marks the end of the life-cycle of an input split. Should be used to close
     * channels and streams and release resources. After this method returns without an error, the
     * input is assumed to be correctly read.
     *
     * <p>When this method is called, the input format it guaranteed to be opened.
     *
     * @throws IOException Thrown, if the input could not be closed properly.
     */
    void close() throws IOException;
}
