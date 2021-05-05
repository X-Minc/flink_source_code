package com.ifugle.rap.bigdata.task.service;

import java.io.IOException;
import java.util.Map;

import com.ifugle.rap.bigdata.task.es.AggsResponseEntity;

/**
 * @author XuWeigang
 * @since 2019年07月17日 15:36
 */
public interface BulkBusinessCommonService {
    /**
     * 批量插入或者更新
     *
     * @param index
     * @param paramBody
     *
     * @throws IOException
     */
    String postInsertOrUpdateBulk(String index, String paramBody) throws IOException;

    /**
     * 根据keyId列表，批量查询
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    String mget(String index, String paramBody) throws IOException;

    /**
     * 搜索查询（可分页查询）
     *
     * @param index
     * @param request
     * @param scrollTime
     *         分钟数 0：浅分页，大于0：快照分页
     *
     * @return
     */
    String queryListByDSL(String index, String request, int scrollTime) throws IOException;

    /**
     * 快照分页时，使用scrollId查询
     *
     * @param request
     *
     * @return
     */
    String scrollQuery(String request) throws IOException;

    /**
     * 删除，快照分页是，缓存的scrollId
     *
     * @param scrollId
     *
     * @return
     */
    void deleteScrollQuery(String scrollId) throws IOException;

    /**
     * 查询ES的Type是否存在
     *
     * @param index
     *
     * @return
     */
    boolean getIndexExist(String index);

    /**
     * 根据查询条件更新数据
     *
     * @param index
     * @param paramBody
     * @param paramMap
     *
     * @return
     *
     * @throws IOException
     */
    String updateByQuery(String index, String paramBody, Map<String, String> paramMap) throws IOException;

    /**
     * ES执行根据条件异步修改数据
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    String updateByWaitForCompletion(String index, String paramBody) throws IOException;

    /**
     * 根据task查询后台执行结果
     *
     * @param task
     *
     * @return
     */
    String tasks(String task) throws IOException;

    /**
     * 设置ES参数
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    String settings(String index, String paramBody) throws IOException;

    /**
     * 刷新
     *
     * @param index
     *
     * @return
     *
     * @throws IOException
     */
    String refresh(String index) throws IOException;

    /**
     * 聚合查询
     *
     * @param index
     * @param buid
     *
     * @return
     */
    AggsResponseEntity aggregateQuery(String index, String buid);

    /**
     * 根据查询条件删除数据
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    String deleteByQuery(String index, String paramBody) throws IOException;

    /**
     * 根据ID查询数据
     *
     * @param index
     * @param keyId
     *
     * @return
     */
    Map<String, Object> getById(String index, String keyId);

    /**
     * 批量新增或更新，（不处理返回的内容错误）
     *
     * @param index
     * @param paramBody
     *
     * @return
     *
     * @throws IOException
     */
    String postInsertOrUpdateBulkNotHandleInternalError(String index, String paramBody) throws IOException;

    /**
     * 创建索引和mapping
     * @param oldIndexName
     * @param newIndexName
     * @return
     */
    boolean createIndexAndMapping(String oldIndexName, String newIndexName);
}
