package com.ifugle.rap.bigdata.task.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ifugle.rap.bigdata.task.es.AggsParam;
import com.ifugle.rap.bigdata.task.es.AggsResponseEntity;
import com.ifugle.rap.utils.GsonUtil;
import com.ifugle.util.NullUtil;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年08月02日 23:14
 */
public class EsAggsUtil {
    public static final String DOC_COUNT = "doc_count";

    /**
     * es 聚合 aggs拼接
     *
     * @param aggsParams
     *
     * @return
     */
    public static String aggsRecursion(List<AggsParam> aggsParams) {
        if (NullUtil.isNull(aggsParams)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String suffix = "";
        for (AggsParam aggsParam : aggsParams) {
            suffix += "}}";
            sb.append("\"aggs\" : {");
            sb.append("\"" + aggsParam.getNickname() + "\" : {");
            sb.append("\"" + aggsParam.getFunction() + "\" : {");
            sb = mapParamsPlicing(sb, aggsParam.getParams());
            sb.append("},");
        }
        if (sb.lastIndexOf(",") == sb.length() - 1) {
            // 去除末尾的逗号
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        }
        sb.append(suffix);
        return sb.toString();
    }

    public static StringBuilder mapParamsPlicing(StringBuilder sb, Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value instanceof String) {
                value = "\"" + value + "\"";
            }
            sb.append("\"" + entry.getKey() + "\":" + value + ",");
        }
        if (sb.lastIndexOf(",") == sb.length() - 1) {
            // 去除末尾的逗号
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        }
        return sb;
    }

    /**
     * 对聚合返回结果进行解析
     * @param aggregations
     * @param aggsParams
     * @return
     */
    public static List<Map<String, Object>> getAggregationsResult(AggsResponseEntity aggregations, List<AggsParam> aggsParams) {
        if (NullUtil.isNull(aggregations)) {
            return Lists.newArrayList();
        }
        List<String> groupByFields = aggsParams.stream().map(aggsParam -> aggsParam.getNickname()).collect(Collectors.toList());
        return getAggsResult(aggregations, groupByFields);
    }

    /**
     * 对聚合返回结果进行解析
     * @param aggregations
     * @param groupByFields 汇总字段集
     * @return
     */
    public static List<Map<String, Object>> getAggsResult(AggsResponseEntity aggregations, List<String> groupByFields) {
        if (NullUtil.isNull(aggregations)) {
            return Lists.newArrayList();
        }
        List<Map<String, Object>> resultList = Lists.newArrayList();
        JsonObject aggs = GsonUtil.fromJson(GsonUtil.toJson(aggregations.getAggregations()), JsonObject.class);
        // 确认是否有返回数据，如果返回的第一层buckets为空，则为没有查询到满足条件的数据
        JsonObject jsonObject = aggs.get(groupByFields.get(0)).getAsJsonObject();
        JsonElement buckets = jsonObject.get("buckets");
        if (NullUtil.isNotNull(buckets)) {
            JsonArray jsonArray = buckets.getAsJsonArray();
            if (jsonArray.size() == 0) {
                return resultList;
            }
        } else {
            Map<String, Object> data = Maps.newHashMap();
            data.put(groupByFields.get(0), jsonObject.get("value"));
            resultList.add(data);
            return resultList;
        }
        getGroupByResult(aggs, groupByFields, 0, Maps.newHashMap(), resultList);
        return resultList;
    }

    /**
     * 递归整理汇总字段
     * @param aggs 当前汇总层的数据对象
     * @param groupByFields 汇总的Group by字段数组
     * @param index 汇总的Group by字段的下标
     * @param supData 上一层汇总字段集合
     * @param resultList 整合后的结果集
     */
    private static void getGroupByResult(JsonObject aggs, List<String> groupByFields, int index, Map<String, Object> supData,
            List<Map<String, Object>> resultList) {
        if (index == groupByFields.size() - 1) {
            int lastIndex = groupByFields.size() - 1;
            JsonObject element = aggs.get(groupByFields.get(lastIndex)).getAsJsonObject();
            JsonElement buckets = element.get("buckets");
            if (NullUtil.isNotNull(buckets)) {
                // 最后一层包含buckets时
                JsonArray jsonArray = buckets.getAsJsonArray();
                Iterator<JsonElement> iterator = jsonArray.iterator();
                while (iterator.hasNext()) {
                    JsonObject item = iterator.next().getAsJsonObject();
                    Map<String, Object> data = Maps.newHashMap();
                    if (NullUtil.isNotNull(supData)) {
                        data.putAll(supData);
                    }
                    data.put(groupByFields.get(index),  item.get("key"));
                    data.put("doc_count",  item.get("doc_count"));
                    resultList.add(data);
                }
            } else {
                // 最后一层只有一个值时
                supData.put(groupByFields.get(lastIndex), element.get("value"));
                resultList.add(supData);
            }
        } else {
            JsonObject jsonObject = aggs.get(groupByFields.get(index)).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("buckets").getAsJsonArray();
            Iterator<JsonElement> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                int i = index;
                Map<String, Object> data = Maps.newHashMap();
                if (NullUtil.isNotNull(supData)) {
                    data.putAll(supData);
                }
                JsonObject element = iterator.next().getAsJsonObject();
                data.put(groupByFields.get(index),  element.get("key"));
                data.put(groupByFields.get(index) + "_doc_count",  element.get("doc_count"));
                // 递归下一层
                getGroupByResult(element, groupByFields, ++i, data, resultList);
            }
        }
    }
}
