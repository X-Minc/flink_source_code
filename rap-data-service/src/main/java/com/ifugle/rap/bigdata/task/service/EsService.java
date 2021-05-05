/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.bigdata.task.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.ifugle.rap.bigdata.task.es.AggsResponseEntity;
import com.ifugle.rap.bigdata.task.es.BulkEntity;
import com.ifugle.rap.bigdata.task.es.DeleteRangeRequest;
import com.ifugle.rap.bigdata.task.es.DslRequestBuild;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.QueryTermsRequest;
import com.ifugle.rap.bigdata.task.es.ResponseEntity;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;
import com.ifugle.rap.bigdata.task.es.UpdateRequestBuild;

/**
 * @author XingZhe
 * @version $Id$
 * @since 2019年07月22日 16:13
 */
public interface EsService<T> {

    /**
     * 单数据插入、更新
     *
     * @return
     */
    Map<String, Object> singleInsertOrUpdate(String index, String keyId, Map<String, Object> dataMap);

    /**
     * 批量插入、覆盖数据
     *
     * @param indexName
     * @param data
     */
    List<BulkEntity> multiInsertOrUpdate(String indexName, Map<String, T> data);

    List<BulkEntity> multiInsertOrUpdateByParent(String indexName, Map<String, T> data);

    /**
     * 批量更新数据
     *
     * @param indexName
     * @param updateMap
     */
    void multiUpdate(String indexName, Map<String, T> updateMap);

    /**
     * 批量更新子文档数据，含父文档ID
     *
     * @param indexName
     * @param data
     */
    void multiUpdateByParent(String indexName, Map<String, T> data);

    /**
     * 批量获取数据
     *
     * @param indexName
     * @param keyIds
     * @param typeCla
     *
     * @return
     */
    List<ResponseEntity<T>> multiGet(String indexName, List<String> keyIds, Type typeCla);

    /**
     * 子文档批量获取数据
     *
     * @param indexName
     * @param keyIds
     * @param typeCla
     *
     * @return
     */
    List<ResponseEntity<T>> multiGetByParent(String indexName, List<String> keyIds, Type typeCla);

    /**
     * 批量删除数据
     *
     * @param indexName
     * @param keyIds
     */
    void multiDelete(String indexName, List<String> keyIds);

    /**
     * 根据上级文档ID及其本级ID批量删除
     *
     * @param indexName
     * @param keyIds
     */
    void multiDeleteByParent(String indexName, List<String> keyIds);

    /**
     * 浅分页查询数据
     *
     * @param index
     * @param querySearch
     * @param typeCla
     * @param page
     * @param size
     *
     * @return
     */
    HitsResponseEntity<T> queryByPage(String index, Map<String, Object> querySearch, Type typeCla, int page, int size);

    /**
     * 浅分页查询数据(可排序)
     *
     * @param index
     * @param build
     * @param typeCla
     * @param page
     * @param size
     *
     * @return
     */
    HitsResponseEntity<T> queryByPage(String index, Type typeCla, DslRequestBuild build, int page, int size);

    /**
     * 快照分页查询数据
     *
     * @param index
     * @param querySearch
     * @param typeCla
     * @param scrollTime
     *         快照分页是缓存分钟数
     * @param page
     * @param size
     *
     * @return
     */
    SearchResponseEntity<T> scrollQueryByPage(String index, Map<String, Object> querySearch, Type typeCla,
            int scrollTime, int page, int size);

    /**
     * 快照分页查询数据(可排序)
     *
     * @param index
     * @param build
     * @param typeCla
     * @param scrollTime
     *         快照分页是缓存分钟数
     * @param page
     * @param size
     *
     * @return
     */
    SearchResponseEntity<T> scrollQueryByPage(String index, Type typeCla, DslRequestBuild build, int scrollTime,
            int page, int size);

    /**
     * 快照分页查询，通过_scroll_id，来查询下一页数据
     *
     * @param scrollId
     * @param scrollTime
     * @param typeCla
     *
     * @return
     */
    SearchResponseEntity<T> scrollQueryByPage(String scrollId, int scrollTime, Type typeCla);

    /**
     * 删除，快照分页是，缓存的scrollId
     *
     * @param scrollId
     */
    void deleteScrollQueryByPage(String scrollId);

    /**
     * 查询ES中是否存在对应的Type
     *
     * @return
     */
    boolean getIndexExist(String index);

    /**
     * 根据查询条件更新数据
     *
     * @param index
     * @param queryMap
     * @param updateMap
     */
    void updateByQueryForCommon(String index, Map<String, Object> queryMap, Map<String, Object> updateMap);

    /**
     * 根据查询条件异步更新数据
     *
     * @param index
     * @param queryMap
     * @param updateMap
     *
     * @return
     */
    String updateByQueryForAsyn(String index, Map<String, Object> queryMap, Map<String, Object> updateMap);

    /**
     * 根据查询条件异步更新数据
     *
     * @param index
     * @param build
     *
     * @return
     */
    String updateByQueryForAsyn(String index, UpdateRequestBuild build);

    /**
     * 异步查询执行结果
     *
     * @param task
     *
     * @return
     */
    Boolean tasks(String task);

    /**
     * 设置ES参数
     *
     * @param index
     * @param updateMap
     *
     * @return
     */
    Boolean settings(String index, Map<String, Object> updateMap);

    /**
     * 刷新
     *
     * @param index
     *
     * @return
     */
    void refresh(String index);

    /**
     * 根据KeyId获取单条数据信息
     *
     * @param index
     * @param keyId
     * @param clazz
     *
     * @return
     */
    T get(String index, String keyId, Class<T> clazz);

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
    List<T> queryByPage(String index, Map<String, Object> paramMap, int page, int size, Class<T> clazz);

    /**
     * 聚合查询
     *
     * @param index
     * @param dslRequest
     *
     * @return
     */
    AggsResponseEntity aggregateQuery(String index, DslRequestBuild dslRequest);

    /**
     * 根据单字段多值匹配查询
     *
     * @param index
     * @param queryTermsRequest
     *
     * @return
     */
    SearchResponseEntity<T> termsQuery(String index, QueryTermsRequest queryTermsRequest, Type typeCla);

    /**
     * 删除Type下的所有数据
     *
     * @param index
     */
    void deleteByType(String index);

    /**
     * 根据条件删除
     *
     * @param index
     * @param request
     */
    void deleteByQuery(String index, DeleteRangeRequest request);

    /**
     * 根据条件查询出待更新的数据，根据数据的ID更新数据
     *
     * @param index
     * @param queryMap
     * @param updateMap
     */
    void updateByQuery(String index, Map<String, Object> queryMap, Map<String, Object> updateMap);

    /**
     * 子文档更新：根据条件查询出待更新的数据，根据数据的ID更新数据
     *
     * @param index
     * @param queryMap
     * @param updateMap
     */
    void updateByQueryParent(String index, Map<String, Object> queryMap, Map<String, Object> updateMap);

    /**
     * 批量更新数据，（不处理返回的内容错误）
     *
     * @param indexName
     * @param data
     */
    void multiUpdateNotHandleInternalError(String indexName, Map<String, T> data);

    /**
     * 子文档批量更新数据，（不处理返回的内容错误）
     *
     * @param indexName
     * @param data
     */
    void multiUpdateNotHandleInternalErrorByParent(String indexName, Map<String, T> data);

    /**
     * 创建索引和mapping
     * @param oldIndexName
     * @param newIndexName
     * @return
     */
    boolean createIndexAndMapping(String oldIndexName, String newIndexName);
}
