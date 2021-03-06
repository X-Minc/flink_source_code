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

package org.apache.flink.connectors.hive;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.connector.source.Boundedness;
import org.apache.flink.api.connector.source.SplitEnumerator;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.connector.file.src.AbstractFileSource;
import org.apache.flink.connector.file.src.ContinuousEnumerationSettings;
import org.apache.flink.connector.file.src.PendingSplitsCheckpoint;
import org.apache.flink.connector.file.src.assigners.FileSplitAssigner;
import org.apache.flink.connector.file.src.enumerate.FileEnumerator;
import org.apache.flink.connector.file.src.reader.BulkFormat;
import org.apache.flink.connector.file.table.ContinuousPartitionFetcher;
import org.apache.flink.connectors.hive.read.HiveSourceSplit;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.table.catalog.ObjectPath;
import org.apache.flink.table.data.RowData;
import org.apache.flink.util.Preconditions;

import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.hadoop.mapred.JobConf;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A unified data source that reads a hive table. HiveSource works on {@link HiveSourceSplit} and
 * uses {@link BulkFormat} to read the data. A built-in BulkFormat is provided to return records in
 * type of {@link RowData}. It's also possible to implement a custom BulkFormat to return data in
 * different types. Use {@link HiveSourceBuilder} to build HiveSource instances.
 *
 * @param <T> the type of record returned by this source
 */
@PublicEvolving
public class HiveSource<T> extends AbstractFileSource<T, HiveSourceSplit> {

    private static final long serialVersionUID = 1L;

    private final ReadableConfig flinkConf;
    private final JobConfWrapper jobConfWrapper;
    private final List<String> partitionKeys;
    private final ContinuousPartitionFetcher<Partition, ?> fetcher;
    private final HiveTableSource.HiveContinuousPartitionFetcherContext<?> fetcherContext;
    private final ObjectPath tablePath;

    HiveSource(
            Path[] inputPaths,
            FileEnumerator.Provider fileEnumerator,
            FileSplitAssigner.Provider splitAssigner,
            BulkFormat<T, HiveSourceSplit> readerFormat,
            @Nullable ContinuousEnumerationSettings continuousEnumerationSettings,
            ReadableConfig flinkConf,
            JobConf jobConf,
            ObjectPath tablePath,
            List<String> partitionKeys,
            @Nullable ContinuousPartitionFetcher<Partition, ?> fetcher,
            @Nullable HiveTableSource.HiveContinuousPartitionFetcherContext<?> fetcherContext) {
        super(
                inputPaths,
                fileEnumerator,
                splitAssigner,
                readerFormat,
                continuousEnumerationSettings);
        Preconditions.checkArgument(
                flinkConf.get(HiveOptions.TABLE_EXEC_HIVE_LOAD_PARTITION_SPLITS_THREAD_NUM) >= 1,
                HiveOptions.TABLE_EXEC_HIVE_LOAD_PARTITION_SPLITS_THREAD_NUM.key()
                        + " cannot be less than 1");
        this.flinkConf = flinkConf;
        this.jobConfWrapper = new JobConfWrapper(jobConf);
        this.tablePath = tablePath;
        this.partitionKeys = partitionKeys;
        this.fetcher = fetcher;
        this.fetcherContext = fetcherContext;
    }

    @Override
    public SimpleVersionedSerializer<HiveSourceSplit> getSplitSerializer() {
        return HiveSourceSplitSerializer.INSTANCE;
    }

    @Override
    public SimpleVersionedSerializer<PendingSplitsCheckpoint<HiveSourceSplit>>
            getEnumeratorCheckpointSerializer() {
        if (continuousPartitionedEnumerator()) {
            return new ContinuousHivePendingSplitsCheckpointSerializer(getSplitSerializer());
        } else {
            return super.getEnumeratorCheckpointSerializer();
        }
    }

    @Override
    public SplitEnumerator<HiveSourceSplit, PendingSplitsCheckpoint<HiveSourceSplit>>
            createEnumerator(SplitEnumeratorContext<HiveSourceSplit> enumContext) {
        if (continuousPartitionedEnumerator()) {
            return createContinuousSplitEnumerator(
                    enumContext,
                    fetcherContext.getConsumeStartOffset(),
                    Collections.emptyList(),
                    Collections.emptyList());
        } else {
            return super.createEnumerator(enumContext);
        }
    }

    @Override
    public SplitEnumerator<HiveSourceSplit, PendingSplitsCheckpoint<HiveSourceSplit>>
            restoreEnumerator(
                    SplitEnumeratorContext<HiveSourceSplit> enumContext,
                    PendingSplitsCheckpoint<HiveSourceSplit> checkpoint) {
        if (continuousPartitionedEnumerator()) {
            Preconditions.checkState(
                    checkpoint instanceof ContinuousHivePendingSplitsCheckpoint,
                    "Illegal type of splits checkpoint %s for streaming read partitioned table",
                    checkpoint.getClass().getName());
            ContinuousHivePendingSplitsCheckpoint hiveCheckpoint =
                    (ContinuousHivePendingSplitsCheckpoint) checkpoint;
            return createContinuousSplitEnumerator(
                    enumContext,
                    hiveCheckpoint.getCurrentReadOffset(),
                    hiveCheckpoint.getSeenPartitionsSinceOffset(),
                    hiveCheckpoint.getSplits());
        } else {
            return super.restoreEnumerator(enumContext, checkpoint);
        }
    }

    private boolean continuousPartitionedEnumerator() {
        return getBoundedness() == Boundedness.CONTINUOUS_UNBOUNDED && !partitionKeys.isEmpty();
    }

    private SplitEnumerator<HiveSourceSplit, PendingSplitsCheckpoint<HiveSourceSplit>>
            createContinuousSplitEnumerator(
                    SplitEnumeratorContext<HiveSourceSplit> enumContext,
                    Comparable<?> currentReadOffset,
                    Collection<List<String>> seenPartitions,
                    Collection<HiveSourceSplit> splits) {
        return new ContinuousHiveSplitEnumerator(
                enumContext,
                currentReadOffset,
                seenPartitions,
                getAssignerFactory().create(new ArrayList<>(splits)),
                getContinuousEnumerationSettings().getDiscoveryInterval().toMillis(),
                flinkConf,
                jobConfWrapper.conf(),
                tablePath,
                fetcher,
                fetcherContext);
    }
}
