/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.bigdata.task.service.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.ifugle.rap.bigdata.task.es.AggsResponseEntity;
import com.ifugle.rap.bigdata.task.es.BulkEntity;
import com.ifugle.rap.bigdata.task.es.BulkResponseEntity;
import com.ifugle.rap.bigdata.task.es.DeleteRangeRequest;
import com.ifugle.rap.bigdata.task.es.DslRequestBuild;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.MgetResponseEntity;
import com.ifugle.rap.bigdata.task.es.QueryTermsRequest;
import com.ifugle.rap.bigdata.task.es.RangeBean;
import com.ifugle.rap.bigdata.task.es.ResponseEntity;
import com.ifugle.rap.bigdata.task.es.ResponseUtil;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;
import com.ifugle.rap.bigdata.task.es.SetResultResponseEntity;
import com.ifugle.rap.bigdata.task.es.SpecificRealtimeSearchResponse;
import com.ifugle.rap.bigdata.task.es.TaskResponseEntity;
import com.ifugle.rap.bigdata.task.es.TaskResultResponseEntity;
import com.ifugle.rap.bigdata.task.es.UpdateRequestBuild;
import com.ifugle.rap.bigdata.task.service.BulkBusinessCommonService;
import com.ifugle.rap.bigdata.task.service.EsCommonService;
import com.ifugle.rap.bigdata.task.service.EsService;
import com.ifugle.rap.constants.EsCode;
import com.ifugle.rap.elasticsearch.model.RealtimeSearchResponse;
import com.ifugle.rap.exception.DsbServiceException;
import com.ifugle.rap.exception.ExceptionUtil;
import com.ifugle.rap.utils.GsonUtil;
import com.ifugle.rap.utils.PageUtils;
import com.ifugle.util.JSONUtil;
import com.ifugle.util.NullUtil;

/**
 * @author XingZhe
 * @version $Id$
 * @since 2019年07月22日 16:16
 */
@Service
public class EsServiceImpl<T> implements EsService<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EsServiceImpl.class);
    @Autowired
    private EsCommonService esCommonService;
    @Autowired
    private BulkBusinessCommonService bulkBusinessCommonService;

    @Value("${conflict.retry.count}")
    private Integer retryCount=0;

    @Override
    public Map<String, Object> singleInsertOrUpdate(String index, String keyId, Map<String, Object> dataMap) {
        Map<String, Object> result = Maps.newHashMap();
        String keyIdStr = esCommonService.insertOrUpdate(index, keyId, dataMap);
        result.put("keyId", keyIdStr);
        return result;
    }

    @Override
    public List<BulkEntity> multiInsertOrUpdate(String indexName, Map<String, T> data) {
        if (NullUtil.isNull(data)) {
            throw new DsbServiceException("ElasticSearch 批量插入或覆盖，传入的data为空");
        }

        List<BulkEntity> result = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, T>> entries = data.entrySet();
            int i = 0;
            for (Map.Entry<String, T> entry : entries) {
                String key = entry.getKey();
                T value = entry.getValue();
                String json = GsonUtil.toJson(value);
                if (NullUtil.isNotNull(key)) {
                    sb.append("{\"index\":{\"_id\": \"" + key + "\"}}");
                } else {
                    sb.append("{\"index\":{}}");
                }
                sb.append("\n");
                sb.append(json);
                sb.append("\n");
                // 5000条执行一次
                if ((++i) % EsCode.ES_PAGE_NUM == 0) {
                    String bulkResult = bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
                    BulkResponseEntity bulkResponseEntity = GsonUtil.fromJson(bulkResult, BulkResponseEntity.class);
                    List<BulkEntity> items = bulkResponseEntity.getItems();
                    result.addAll(items);
                    sb = new StringBuilder();
                }
            }

            if (sb.length() > 0) {
                String bulkResult = bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
                BulkResponseEntity bulkResponseEntity = GsonUtil.fromJson(bulkResult, BulkResponseEntity.class);
                List<BulkEntity> items = bulkResponseEntity.getItems();
                result.addAll(items);
            }
        } catch (Exception e) {
            LOGGER.error("批量插入或覆盖，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量插入或覆盖失败", e);
        }
        return result;
    }

    /**
     * 子文档批量新增或覆盖
     * 数据key值包含子文档ID和父文档ID，以逗号分隔
     *
     * @param indexName
     * @param data
     *
     * @return
     */
    @Override
    public List<BulkEntity> multiInsertOrUpdateByParent(String indexName, Map<String, T> data) {
        if (NullUtil.isNull(data)) {
            throw new DsbServiceException("ElasticSearch 批量插入或覆盖，传入的data为空");
        }

        List<BulkEntity> result = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, T>> entries = data.entrySet();
            int i = 0;
            for (Map.Entry<String, T> entry : entries) {
                String key = entry.getKey();
                T value = entry.getValue();
                String json = GsonUtil.toJson(value);
                if (NullUtil.isNotNull(key)) {
                    String[] keys = key.split(",");
                    sb.append("{\"index\":{\"_id\": \"" + keys[0] + "\", \"_parent\": \"" + keys[1] + "\"}}");
                } else {
                    sb.append("{\"index\":{}}");
                }
                sb.append("\n");
                sb.append(json);
                sb.append("\n");
                // 2500条执行一次
                if ((++i) % EsCode.ES_PAGE_NUM_BY_PARENT == 0) {
                    String bulkResult = bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
                    BulkResponseEntity bulkResponseEntity = GsonUtil.fromJson(bulkResult, BulkResponseEntity.class);
                    List<BulkEntity> items = bulkResponseEntity.getItems();
                    result.addAll(items);
                    sb = new StringBuilder();
                }
            }

            if (sb.length() > 0) {
                String bulkResult = bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
                BulkResponseEntity bulkResponseEntity = GsonUtil.fromJson(bulkResult, BulkResponseEntity.class);
                List<BulkEntity> items = bulkResponseEntity.getItems();
                result.addAll(items);
            }
        } catch (Exception e) {
            LOGGER.error("批量插入或覆盖，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量插入或覆盖失败", e);
        }
        return result;
    }

    @Override
    public void multiUpdate(String indexName, Map<String, T> data) {
        if (NullUtil.isNull(data)) {
            throw new DsbServiceException("ElasticSearch 批量更新，传入的data为空");
        }

        try {
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, T>> entries = data.entrySet();
            int i = 0;
            for (Map.Entry<String, T> entry : entries) {
                String key = entry.getKey();
                T value = entry.getValue();
                String json = GsonUtil.toJson(value);
//                sb.append("{\"update\":{\"_id\": \"" + key + "\"}}");
                sb.append("{\"update\":{\"_id\": \"" + key + "\",\"retry_on_conflict\":" + retryCount + "}}");
                sb.append("\n");
                sb.append("{\"doc\":").append(json).append("}");
                sb.append("\n");
                // 5000条执行一次
                if ((++i) % EsCode.ES_PAGE_NUM == 0) {
                    bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
                    sb = new StringBuilder();
                }
            }

            if (sb.length() > 0) {
                bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
            }
        } catch (Exception e) {
            LOGGER.error("批量更新，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量更新失败", e);
        }
    }

    /**
     * 批量更新子文档数据，含父文档ID
     * 数据key值包含子文档ID和父文档ID，以逗号分隔
     *
     * @param indexName
     * @param data
     */
    @Override
    public void multiUpdateByParent(String indexName, Map<String, T> data) {
        if (NullUtil.isNull(data)) {
            throw new DsbServiceException("ElasticSearch 批量更新，传入的data为空");
        }

        try {
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, T>> entries = data.entrySet();
            int i = 0;
            for (Map.Entry<String, T> entry : entries) {
                String key = entry.getKey();
                T value = entry.getValue();
                String json = GsonUtil.toJson(value);
                String[] keys = key.split(",");
                sb.append("{\"update\":{\"_id\": \"" + keys[0] + "\", \"_parent\": \"" + keys[1] + "\"}}");
                sb.append("\n");
                sb.append("{\"doc\":").append(json).append("}");
                sb.append("\n");
                // 2500条执行一次
                if ((++i) % EsCode.ES_PAGE_NUM_BY_PARENT == 0) {
                    bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
                    sb = new StringBuilder();
                }
            }

            if (sb.length() > 0) {
                bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
            }
        } catch (Exception e) {
            LOGGER.error("批量更新，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量更新失败", e);
        }
    }

    @Override
    public List<ResponseEntity<T>> multiGet(String indexName, List<String> keyIds, Type typeCla) {
        if (NullUtil.isNull(keyIds)) {
            throw new DsbServiceException("ElasticSearch 批量查询，传入的keyIds为空");
        }

        try {
            StringBuilder sb = new StringBuilder("{\"docs\":[");
            for (String keyId : keyIds) {
                sb.append("{\"_id\":\"" + keyId + "\"},");
            }

            if (NullUtil.isNotNull(sb) && sb.length() > 0) {
                sb.delete(sb.length() - 1, sb.length());
            }
            sb.append("]}");
            String json = sb.toString();
            String resultJson = bulkBusinessCommonService.mget(indexName, json);
            MgetResponseEntity<T> responseEntity = GsonUtil.fromJson(resultJson, typeCla);
            List<ResponseEntity<T>> result = responseEntity.getDocs();
            result = result.stream().filter(ResponseEntity::isFound).collect(Collectors.toList());
            return result;
        } catch (Exception e) {
            LOGGER.error("批量keyId查询，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量keyId查询失败", e);
        }
    }

    /**
     * 子文档批量获取
     * 数据key值包含子文档ID和父文档ID，以逗号分隔
     *
     * @param indexName
     * @param keyIds
     * @param typeCla
     *
     * @return
     */
    @Override
    public List<ResponseEntity<T>> multiGetByParent(String indexName, List<String> keyIds, Type typeCla) {
        if (NullUtil.isNull(keyIds)) {
            throw new DsbServiceException("ElasticSearch 批量查询，传入的keyIds为空");
        }

        try {
            StringBuilder sb = new StringBuilder("{\"docs\":[");
            for (String keyId : keyIds) {
                String[] keys = keyId.split(",");
                sb.append("{\"_id\": \"" + keys[0] + "\", \"_parent\": \"" + keys[1] + "\"},");
            }

            if (NullUtil.isNotNull(sb) && sb.length() > 0) {
                sb.delete(sb.length() - 1, sb.length());
            }
            sb.append("]}");
            String json = sb.toString();
            String resultJson = bulkBusinessCommonService.mget(indexName, json);
            MgetResponseEntity<T> responseEntity = GsonUtil.fromJson(resultJson, typeCla);
            List<ResponseEntity<T>> result = responseEntity.getDocs();
            result = result.stream().filter(ResponseEntity::isFound).collect(Collectors.toList());
            return result;
        } catch (Exception e) {
            LOGGER.error("批量keyId查询，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量keyId查询失败", e);
        }
    }

    @Override
    public void multiDelete(String indexName, List<String> keyIds) {
        if (NullUtil.isNull(keyIds)) {
            throw new DsbServiceException("ElasticSearch 批量删除，传入的keyIds为空");
        }

        try {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String keyId : keyIds) {
                sb.append("{\"delete\":{\"_id\": \"" + keyId + "\"}}");
                sb.append("\n");
                if ((++i) % EsCode.ES_PAGE_NUM == 0) {
                    bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
                    sb = new StringBuilder();
                }
            }

            if (sb.length() > 0) {
                bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
            }
        } catch (Exception e) {
            LOGGER.error("批量删除，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量删除失败", e);
        }
    }

    /**
     * 根据上级文档ID及其本级ID批量删除
     * 数据key值包含子文档ID和父文档ID，以逗号分隔
     *
     * @param indexName
     * @param keyIds
     */
    @Override
    public void multiDeleteByParent(String indexName, List<String> keyIds) {
        if (NullUtil.isNull(keyIds)) {
            throw new DsbServiceException("ElasticSearch 批量删除，传入的keyIds为空");
        }

        try {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (String keyId : keyIds) {
                String[] keys = keyId.split(",");
                sb.append("{\"delete\":{\"_id\": \"" + keys[0] + "\", \"_parent\": \"" + keys[1] + "\"}}");
                sb.append("\n");
                if ((++i) % EsCode.ES_PAGE_NUM_BY_PARENT == 0) {
                    bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
                    sb = new StringBuilder();
                }
            }

            if (sb.length() > 0) {
                bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
            }
        } catch (Exception e) {
            LOGGER.error("批量删除，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量删除失败", e);
        }
    }

    @Override
    public HitsResponseEntity<T> queryByPage(String index, Map<String, Object> querySearch, Type typeCla, int page, int size) {
        DslRequestBuild build = new DslRequestBuild(page, size, new HashMap<>());
        String request = null;
        if (NullUtil.isNotNull(querySearch)) {
            for (String key : querySearch.keySet()) {
                build.setParam(key, querySearch.get(key));
            }
        }
        request = build.build();

        SearchResponseEntity<T> responseEntity = query(index, request, typeCla, 0);
        HitsResponseEntity<T> result = responseEntity.getHits();
        return result;
    }

    @Override
    public HitsResponseEntity<T> queryByPage(String index, Type typeCla, DslRequestBuild build, int page,
            int size) {
        build.setPage(page);
        build.setSize(size);

        SearchResponseEntity<T> responseEntity = query(index, build.build(), typeCla, 0);
        HitsResponseEntity<T> result = responseEntity.getHits();
        return result;
    }

    private SearchResponseEntity<T> query(String index, String request, Type typeCla, int scrollTime) {
        try {
            String resultJson = bulkBusinessCommonService.queryListByDSL(index, request, scrollTime);
            SearchResponseEntity<T> responseEntity = GsonUtil.fromJson(resultJson, typeCla);
            return responseEntity;
        } catch (Exception e) {
            LOGGER.error("分页查询，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("分页查询失败", e);
        }
    }

    @Override
    public SearchResponseEntity<T> scrollQueryByPage(String index, Map<String, Object> querySearch, Type typeCla, int scrollTime, int page,
            int size) {
        DslRequestBuild build = new DslRequestBuild(page, size, new HashMap<>());
        String request = null;
        if (NullUtil.isNotNull(querySearch)) {
            for (String key : querySearch.keySet()) {
                build.setParam(key, querySearch.get(key));
            }
        }
        LOGGER.debug("scrollQueryByPage: {}", GsonUtil.toJson(querySearch));
        request = build.build();

        SearchResponseEntity<T> responseEntity = query(index, request, typeCla, scrollTime);
        return responseEntity;
    }

    @Override
    public SearchResponseEntity<T> scrollQueryByPage(String index, Type typeCla, DslRequestBuild build,
            int scrollTime, int page,
            int size) {
        build.setPage(page);
        build.setSize(size);

        SearchResponseEntity<T> responseEntity = query(index, build.build(), typeCla, scrollTime);
        return responseEntity;
    }

    @Override
    public SearchResponseEntity<T> scrollQueryByPage(String scrollId, int scrollTime, Type typeCla) {
        if (NullUtil.isNull(scrollId)) {
            throw new DsbServiceException("ElasticSearch 快照分页，传入的scrollId为空");
        }
        SearchResponseEntity<T> responseEntity = scrollQuery(scrollId, scrollTime, typeCla);
        return responseEntity;
    }

    @Override
    public void deleteScrollQueryByPage(String scrollId) {
        if (NullUtil.isNull(scrollId)) {
            throw new DsbServiceException("ElasticSearch 删除快照分页，传入的scrollId为空");
        }
        try {
            bulkBusinessCommonService.deleteScrollQuery(scrollId);
        } catch (Exception e) {
            LOGGER.error("删除快照分页，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("删除快照分页失败", e);
        }
    }

    private SearchResponseEntity<T> scrollQuery(String scrollId, int scrollTime, Type typeCla) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"scroll_id\": \"").append(scrollId).append("\"");
            if (scrollTime > 0) {
                sb.append(",\"scroll\": \"").append(scrollTime).append("m\"");
            }
            sb.append("}");
            String request = sb.toString();
            String resultJson = bulkBusinessCommonService.scrollQuery(request);
            SearchResponseEntity<T> responseEntity = GsonUtil.fromJson(resultJson, typeCla);
            return responseEntity;
        } catch (Exception e) {
            LOGGER.error("快照分页查询，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("快照分页查询失败", e);
        }
    }

    /**
     * 查询ES中是否存在对应的index
     *
     * @param index
     *
     * @return
     */
    @Override
    public boolean getIndexExist(String index) {
        return bulkBusinessCommonService.getIndexExist(index);
    }

    /**
     * 根据查询条件更新数据
     *
     * @param index
     * @param queryMap
     * @param updateMap
     */
    @Override
    public void updateByQueryForCommon(String index, Map<String, Object> queryMap, Map<String, Object> updateMap) {
        if (NullUtil.isNull(queryMap) || NullUtil.isNull(updateMap)) {
            throw new DsbServiceException("ElasticSearch 根据查询条件更新：查询条件或更新语句为空");
        }

        try {
            StringBuilder paramBody = getUpdateParamJson(queryMap, updateMap);
            bulkBusinessCommonService.updateByQuery(index, paramBody.toString(), null);
        } catch (IOException e) {
            LOGGER.error("根据查询条件更新，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("根据查询条件更新失败", e);
        }
    }

    /**
     * 根据查询条件异步更新数据
     *
     * @param index
     * @param queryMap
     * @param updateMap
     */
    @Override
    public String updateByQueryForAsyn(String index, Map<String, Object> queryMap, Map<String, Object> updateMap) {
        if (NullUtil.isNull(queryMap) || NullUtil.isNull(updateMap)) {
            throw new DsbServiceException("ElasticSearch 根据查询条件异步更新：查询条件或更新语句为空");
        }

        UpdateRequestBuild build = new UpdateRequestBuild();
        build.setUpdateParams(updateMap);
        build.setParams(queryMap);
        return updateByQueryForAsyn(index, build);
    }

    /**
     * 根据查询条件异步更新数据
     *
     * @param index
     * @param build
     */
    @Override
    public String updateByQueryForAsyn(String index, UpdateRequestBuild build) {
        try {
            String result = bulkBusinessCommonService.updateByWaitForCompletion(index, build.build());
            TaskResponseEntity taskResponseEntity = GsonUtil.fromJson(result, TaskResponseEntity.class);
            String task = taskResponseEntity.getTask();
            return task;
        } catch (IOException e) {
            LOGGER.error("根据查询条件异步更新，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("根据查询条件异步更新失败", e);
        }
    }

    @Override
    public Boolean tasks(String task) {
        if (NullUtil.isNull(task)) {
            throw new DsbServiceException("ElasticSearch 异步查询执行结果task为空");
        }
        try {
            String result = bulkBusinessCommonService.tasks(task);
            TaskResultResponseEntity taskResultResponseEntity = GsonUtil.fromJson(result, TaskResultResponseEntity.class);
            return taskResultResponseEntity.getCompleted();
        } catch (IOException e) {
            LOGGER.error("异步查询执行结果，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("异步查询执行结果失败", e);
        }
    }

    @Override
    public Boolean settings(String index, Map<String, Object> updateMap) {
        if (NullUtil.isNull(updateMap)) {
            throw new DsbServiceException("ElasticSearch 设置参数为空");
        }

        try {
            StringBuilder paramBody = new StringBuilder();
            paramBody.append("{\"index\": {");
            for (String key : updateMap.keySet()) {
                Object o = updateMap.get(key);
                paramBody.append("\"");
                paramBody.append(key);
                paramBody.append("\": ");
                paramBody.append(GsonUtil.toJson(o));
                paramBody.append(",");
            }
            if (paramBody.lastIndexOf(",") == paramBody.length() - 1) {
                // 去除末尾的逗号
                paramBody = new StringBuilder(paramBody.substring(0, paramBody.length() - 1));
            }
            paramBody.append("}}");
            String result = bulkBusinessCommonService.settings(index, paramBody.toString());
            SetResultResponseEntity setResultResponseEntity = GsonUtil.fromJson(result, SetResultResponseEntity.class);
            Boolean acknowledged = setResultResponseEntity.getAcknowledged();
            return acknowledged;
        } catch (IOException e) {
            LOGGER.error("设置ES参数异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("设置ES参数失败", e);
        }
    }

    @Override
    public void refresh(String index) {
        try {
            bulkBusinessCommonService.refresh(index);
        } catch (IOException e) {
            LOGGER.error("刷新数据异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("刷新数据失败", e);
        }
    }

    /**
     * 拼装更新数据json
     *
     * @param queryMap
     * @param updateMap
     *
     * @return
     */
    private StringBuilder getUpdateParamJson(Map<String, Object> queryMap, Map<String, Object> updateMap) {
        StringBuilder paramBody = new StringBuilder();
        paramBody.append("{");
        paramBody.append("\"script\": {\"inline\": \"");
        for (String key : updateMap.keySet()) {
            paramBody.append("ctx._source['");
            paramBody.append(key);
            paramBody.append("'] = ");
            String value = GsonUtil.toJson(updateMap.get(key));
            if (value.startsWith("\"")) {
                // 如果是字符串格式，需要将"改为'
                value = value.replaceFirst("\"", "'");
                value = value.substring(0, value.length() - 1) + "'";
            }
            paramBody.append(value);
            paramBody.append(";");
        }
        paramBody.append("\"},");
        paramBody.append("\"query\": {\"bool\": {\"must\": [");
        for (String key : queryMap.keySet()) {
            Object o = queryMap.get(key);
            if (o instanceof List) {
                paramBody.append("{\"terms\": {\"");
                paramBody.append(key);
                paramBody.append("\":");
                paramBody.append(GsonUtil.toJson(o));
                paramBody.append("}},");
            } else if (o instanceof RangeBean) {
                paramBody.append("{\"range\": {\"");
                paramBody.append(key);
                paramBody.append("\":");
                paramBody.append(GsonUtil.toJson(o));
                paramBody.append("}},");
            } else {
                paramBody.append("{\"term\": {\"");
                paramBody.append(key);
                paramBody.append("\": {\"value\": ");
                paramBody.append(GsonUtil.toJson(o));
                paramBody.append("}}},");
            }
        }
        if (paramBody.lastIndexOf(",") == paramBody.length() - 1) {
            // 去除末尾的逗号
            paramBody = new StringBuilder(paramBody.substring(0, paramBody.length() - 1));
        }
        paramBody.append("]}}");
        paramBody.append("}");
        return paramBody;
    }

    /**
     * 根据KeyId获取单条数据信息
     *
     * @param index
     * @param keyId
     * @param clazz
     *
     * @return
     */
    @Override
    public T get(String index, String keyId, Class<T> clazz) {
        Map<String, Object> resultMap = bulkBusinessCommonService.getById(index, keyId);
        return GsonUtil.fromJson(GsonUtil.toJson(resultMap), clazz);
    }

    /**
     * 分页查询，但最多只可以查到第10000条
     *
     * @param index
     * @param paramMap
     * @param page
     * @param size
     * @param clazz
     *
     * @return
     */
    @Override
    public List<T> queryByPage(String index, Map<String, Object> paramMap, int page, int size, Class<T> clazz) {
        DslRequestBuild build = new DslRequestBuild(page, size, paramMap);
        String request = JSONUtil.toJSON(build.build());
        RealtimeSearchResponse rep = esCommonService.searchByDSL(index, request);
        SpecificRealtimeSearchResponse<T> result = ResponseUtil.build(rep, clazz);
        List<T> data = result.getData();
        return data;
    }

    @Override
    public AggsResponseEntity aggregateQuery(String index, DslRequestBuild dslRequest) {
        return bulkBusinessCommonService.aggregateQuery(index, dslRequest.build());
    }

    @Override
    public SearchResponseEntity<T> termsQuery(String index, QueryTermsRequest queryTermsRequest, Type typeCla) {
        String resultJson = null;
        try {
            resultJson = bulkBusinessCommonService.queryListByDSL(index, queryTermsRequest.buid(), -1);
            SearchResponseEntity<T> responseEntity = GsonUtil.fromJson(resultJson, typeCla);
            return responseEntity;
        } catch (IOException e) {
            LOGGER.error("单字段多值匹配查询，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("单字段多值匹配查询失败", e);
        }
    }

    /**
     * 删除Type下的所有数据
     *
     * @param index
     */
    @Override
    public void deleteByType(String index) {
        try {
            String queryBody = "{\"query\": {\"match_all\": {}}}";
            bulkBusinessCommonService.deleteByQuery(index, queryBody);
        } catch (IOException e) {
            LOGGER.error("删除Type下的所有数据，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("删除Type下的所有数据失败", e);
        }
    }

    /**
     * 删除Type下的所有数据
     *
     * @param index
     */
    @Override
    public void deleteByQuery(String index, DeleteRangeRequest request) {
        try {
            bulkBusinessCommonService.deleteByQuery(index, request.buid());
        } catch (IOException e) {
            LOGGER.error("根据查询条件删除，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("根据查询条件删除失败", e);
        }
    }

    /**
     * 根据条件查询出待更新的数据，根据数据的ID更新数据
     *
     * @param index
     * @param queryMap
     * @param updateMap
     */
    @Override
    public void updateByQuery(String index, Map<String, Object> queryMap, Map<String, Object> updateMap) {
        if (NullUtil.isNull(queryMap) || NullUtil.isNull(updateMap)) {
            throw new DsbServiceException("ElasticSearch 根据查询条件更新：查询条件或更新语句为空");
        }

        LOGGER.info("执行根据查询条件修改数据，query = {}, update = {}", queryMap, updateMap);
        int page = 1;
        int size = EsCode.ES_FIND_PAGE_NUM;
        Type typeCla = new TypeToken<SearchResponseEntity<Map<String, Object>>>() {
        }.getType();
        // 分页查询出满足条件的数据
        SearchResponseEntity<Map<String, Object>> entity = scrollQueryForUpdate(index, queryMap, 3, page, size);
        HitsResponseEntity<Map<String, Object>> hits = entity.getHits();
        String scrollId = entity.getScrollId();
        int total = hits.getTotal().getValue();
        int totalPage = PageUtils.getTotalPage(total, size);
        // 更新查询出的数据
        updateQueryData(index, updateMap, hits, false);

        try {
            while (totalPage > page) {
                page++;
                entity = scrollQueryForUpdate(scrollId, 3, typeCla);
                hits = entity.getHits();
                // 更新查询出的数据
                updateQueryData(index, updateMap, hits, false);
            }
            deleteScrollQueryByPage(scrollId);
        } catch (DsbServiceException e) {
            deleteScrollQueryByPage(scrollId);
            throw new DsbServiceException(e);
        }
    }

    /**
     * 子文档更新：根据条件查询出待更新的数据，根据数据的ID更新数据
     * 数据key值包含子文档ID和父文档ID，以逗号分隔
     *
     * @param index
     * @param queryMap
     * @param updateMap
     */
    @Override
    public void updateByQueryParent(String index, Map<String, Object> queryMap, Map<String, Object> updateMap) {
        if (NullUtil.isNull(queryMap) || NullUtil.isNull(updateMap)) {
            throw new DsbServiceException("ElasticSearch 根据查询条件更新：查询条件或更新语句为空");
        }

        LOGGER.info("执行根据查询条件修改数据，query = {}, update = {}", queryMap, updateMap);
        int page = 1;
        int size = EsCode.ES_FIND_PAGE_NUM;
        Type typeCla = new TypeToken<SearchResponseEntity<Map<String, Object>>>() {
        }.getType();
        // 分页查询出满足条件的数据
        SearchResponseEntity<Map<String, Object>> entity = scrollQueryForUpdate(index, queryMap, 3, page, size);
        HitsResponseEntity<Map<String, Object>> hits = entity.getHits();
        String scrollId = entity.getScrollId();
        int total = hits.getTotal().getValue();
        int totalPage = PageUtils.getTotalPage(total, size);
        // 更新查询出的数据
        updateQueryData(index, updateMap, hits, true);

        try {
            while (totalPage > page) {
                page++;
                entity = scrollQueryForUpdate(scrollId, 3, typeCla);
                hits = entity.getHits();
                // 更新查询出的数据
                updateQueryData(index, updateMap, hits, true);
            }
            deleteScrollQueryByPage(scrollId);
        } catch (DsbServiceException e) {
            deleteScrollQueryByPage(scrollId);
            throw new DsbServiceException(e);
        }
    }

    /**
     * 用于根据查询更新的分页查询，首页查询
     *
     * @param index
     * @param querySearch
     * @param scrollTime
     * @param page
     * @param size
     *
     * @return
     */
    private SearchResponseEntity<Map<String, Object>> scrollQueryForUpdate(String index, Map<String, Object> querySearch, int scrollTime, int page,
            int size) {
        DslRequestBuild build = new DslRequestBuild(page, size, new HashMap<>());
        String request = null;

        try {
            if (NullUtil.isNotNull(querySearch)) {
                for (String key : querySearch.keySet()) {
                    build.setParam(key, querySearch.get(key));
                }
            }
            request = build.build();
            String resultJson = bulkBusinessCommonService.queryListByDSL(index, request, scrollTime);
            SearchResponseEntity<Map<String, Object>> responseEntity = GsonUtil
                    .fromJson(resultJson, new SearchResponseEntity<Map<String, Object>>().getClass());
            return responseEntity;
        } catch (Exception e) {
            LOGGER.error("分页查询，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("分页查询失败", e);
        }
    }

    /**
     * 用于根据查询更新的分页查询，后续页查询
     *
     * @param scrollId
     * @param scrollTime
     * @param typeCla
     *
     * @return
     */
    private SearchResponseEntity<Map<String, Object>> scrollQueryForUpdate(String scrollId, int scrollTime, Type typeCla) {
        if (NullUtil.isNull(scrollId)) {
            throw new DsbServiceException("ElasticSearch 快照分页，传入的scrollId为空");
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\"scroll_id\": \"").append(scrollId).append("\"");
            if (scrollTime > 0) {
                sb.append(",\"scroll\": \"").append(scrollTime).append("m\"");
            }
            sb.append("}");
            String request = sb.toString();
            String resultJson = bulkBusinessCommonService.scrollQuery(request);
            SearchResponseEntity<Map<String, Object>> responseEntity = GsonUtil.fromJson(resultJson, typeCla);
            return responseEntity;
        } catch (Exception e) {
            LOGGER.error("快照分页查询，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("快照分页查询失败", e);
        }
    }

    /**
     * 根据查询出的数据ID进行更新
     *
     * @param index
     * @param updateMap
     * @param hits
     * @param hasParent
     */
    private void updateQueryData(String index, Map<String, Object> updateMap,
            HitsResponseEntity<Map<String, Object>> hits, Boolean hasParent) {
        Map<String, Map<String, Object>> updateDataMap = Maps.newHashMap();
        List<ResponseEntity<Map<String, Object>>> list = hits.getHits();
        if (hasParent) {
            for (ResponseEntity responseEntity : list) {
                updateDataMap.put(responseEntity.getId() + "," + responseEntity.getParent(), updateMap);
            }
        } else {
            for (ResponseEntity responseEntity : list) {
                updateDataMap.put(responseEntity.getId(), updateMap);
            }
        }
        if (updateDataMap.size() > 0) {
            multiUpdateByQuery(index, updateDataMap, hasParent);
        }
    }

    /**
     * 用于根据查询更新的分批更新
     *
     * @param indexName
     * @param data
     * @param hasParent
     */
    private void multiUpdateByQuery(String indexName, Map<String, Map<String, Object>> data, Boolean hasParent) {
        if (NullUtil.isNull(data)) {
            throw new DsbServiceException("ElasticSearch 批量更新，传入的data为空");
        }

        int esPageNum = EsCode.ES_PAGE_NUM;
        if (hasParent) {
            esPageNum = EsCode.ES_PAGE_NUM_BY_PARENT;
        }

        try {
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, Map<String, Object>>> entries = data.entrySet();
            int i = 0;
            for (Map.Entry<String, Map<String, Object>> entry : entries) {
                String key = entry.getKey();
                Map<String, Object> value = entry.getValue();
                String json = GsonUtil.toJson(value);
                if (hasParent) {
                    String[] keys = key.split(",");
                    sb.append("{\"update\":{\"_id\": \"" + keys[0] + "\", \"_parent\": \"" + keys[1] + "\"}}");
                } else {
                    sb.append("{\"update\":{\"_id\": \"" + key + "\"}}");
                }
                sb.append("\n");
                sb.append("{\"doc\":").append(json).append("}");
                sb.append("\n");
                // 5000条执行一次
                if ((++i) % esPageNum == 0) {
                    bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
                    sb = new StringBuilder();
                }
            }

            if (sb.length() > 0) {
                bulkBusinessCommonService.postInsertOrUpdateBulk(indexName, sb.toString());
            }
        } catch (Exception e) {
            LOGGER.error("批量更新，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量更新失败", e);
        }
    }

    @Override
    public void multiUpdateNotHandleInternalError(String indexName, Map<String, T> data) {
        if (NullUtil.isNull(data)) {
            throw new DsbServiceException("ElasticSearch 批量更新，传入的data为空");
        }

        try {
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, T>> entries = data.entrySet();
            int i = 0;
            for (Map.Entry<String, T> entry : entries) {
                String key = entry.getKey();
                T value = entry.getValue();
                String json = GsonUtil.toJson(value);
                sb.append("{\"update\":{\"_id\": \"" + key + "\"}}");
                sb.append("\n");
                sb.append("{\"doc\":").append(json).append("}");
                sb.append("\n");
                // 5000条执行一次
                if ((++i) % EsCode.ES_PAGE_NUM == 0) {
                    bulkBusinessCommonService.postInsertOrUpdateBulkNotHandleInternalError(indexName, sb.toString());
                    sb = new StringBuilder();
                }
            }

            if (sb.length() > 0) {
                bulkBusinessCommonService.postInsertOrUpdateBulkNotHandleInternalError(indexName, sb.toString());
            }
        } catch (Exception e) {
            LOGGER.error("批量更新，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量更新失败", e);
        }
    }

    /**
     * 子文档批量更新数据，（不处理返回的内容错误）
     *
     * @param indexName
     * @param data
     */
    @Override
    public void multiUpdateNotHandleInternalErrorByParent(String indexName, Map<String, T> data) {
        if (NullUtil.isNull(data)) {
            throw new DsbServiceException("ElasticSearch 批量更新，传入的data为空");
        }

        try {
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, T>> entries = data.entrySet();
            int i = 0;
            for (Map.Entry<String, T> entry : entries) {
                String key = entry.getKey();
                T value = entry.getValue();
                String json = GsonUtil.toJson(value);
                String[] keys = key.split(",");
                sb.append("{\"update\":{\"_id\": \"" + keys[0] + "\", \"_parent\": \"" + keys[1] + "\"}}");
                sb.append("\n");
                sb.append("{\"doc\":").append(json).append("}");
                sb.append("\n");
                // 5000条执行一次
                if ((++i) % EsCode.ES_PAGE_NUM == 0) {
                    bulkBusinessCommonService.postInsertOrUpdateBulkNotHandleInternalError(indexName, sb.toString());
                    sb = new StringBuilder();
                }
            }

            if (sb.length() > 0) {
                bulkBusinessCommonService.postInsertOrUpdateBulkNotHandleInternalError(indexName, sb.toString());
            }
        } catch (Exception e) {
            LOGGER.error("批量更新，异常：e={}", ExceptionUtil.getExceptionDetail(e));
            throw new DsbServiceException("批量更新失败", e);
        }
    }
    /**
     * 创建索引和mapping
     * @param oldIndexName
     * @param newIndexName
     * @return
     */
    @Override
    public boolean createIndexAndMapping(String oldIndexName, String newIndexName) {
        return bulkBusinessCommonService.createIndexAndMapping(oldIndexName,newIndexName);
    }
}