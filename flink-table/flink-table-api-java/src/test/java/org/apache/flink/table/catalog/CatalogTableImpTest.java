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

package org.apache.flink.table.catalog;

import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.descriptors.DescriptorProperties;
import org.apache.flink.table.descriptors.Schema;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/** Test for {@link CatalogTableImpl}. */
public class CatalogTableImpTest {
    private static final String TEST = "test";

    @Test
    public void testToProperties() {
        TableSchema schema = createTableSchema();
        Map<String, String> prop = createProperties();
        CatalogTable table = new CatalogTableImpl(schema, createPartitionKeys(), prop, TEST);

        DescriptorProperties descriptorProperties = new DescriptorProperties(false);
        descriptorProperties.putProperties(table.toProperties());

        assertEquals(schema, descriptorProperties.getTableSchema(Schema.SCHEMA));
    }

    @Test
    public void testFromProperties() {
        TableSchema schema = createTableSchema();
        Map<String, String> prop = createProperties();
        CatalogTable table = new CatalogTableImpl(schema, createPartitionKeys(), prop, TEST);

        CatalogTableImpl tableFromProperties =
                CatalogTableImpl.fromProperties(table.toProperties());

        assertEquals(tableFromProperties.getOptions(), table.getOptions());
        assertEquals(tableFromProperties.getPartitionKeys(), table.getPartitionKeys());
        assertEquals(tableFromProperties.getSchema(), table.getSchema());
    }

    @Test
    public void testNullComment() {
        TableSchema schema = createTableSchema();
        Map<String, String> prop = createProperties();
        CatalogTable table = new CatalogTableImpl(schema, createPartitionKeys(), prop, null);

        assertEquals("", table.getComment());
        assertEquals(Optional.of(""), table.getDescription());
    }

    private static Map<String, String> createProperties() {
        return new HashMap<String, String>() {
            {
                put("k", "v");
                put("K1", "V1"); // for test case-sensitive
            }
        };
    }

    private static TableSchema createTableSchema() {
        return TableSchema.builder()
                .field("first", DataTypes.STRING())
                .field("second", DataTypes.INT())
                .field("third", DataTypes.DOUBLE())
                .field("Fourth", DataTypes.BOOLEAN()) // for test case-sensitive
                .build();
    }

    private static List<String> createPartitionKeys() {
        return Arrays.asList("second", "third");
    }
}
