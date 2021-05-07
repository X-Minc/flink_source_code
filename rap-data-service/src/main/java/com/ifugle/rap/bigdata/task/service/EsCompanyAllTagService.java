package com.ifugle.rap.bigdata.task.service;

import java.util.List;
import java.util.Map;

import com.ifugle.rap.bigdata.task.CompanyAllTag;
import com.ifugle.rap.bigdata.task.es.DslRequestBuild;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年07月26日 17:08
 */
public interface EsCompanyAllTagService {
    /**
     * 根据keyId列表，查询全量标签表的数据
     *
     * @param keyIds
     */
    List<CompanyAllTag> listByKeyIds(List<String> keyIds);

    /**
     * 快照分页查询，分页查询增量数据
     *
     * @param querySearch
     * @param scrollTime
     * @param page
     * @param size
     *
     * @return
     */
    SearchResponseEntity<CompanyAllTag> scrollQueryByPage(Map<String, Object> querySearch, int scrollTime, int page, int size);

    /**
     * 快照分页查询，分页查询用户数据(可排序)
     *
     * @param build
     * @param scrollTime
     * @param page
     * @param size
     *
     * @return
     */
    SearchResponseEntity<CompanyAllTag> scrollQueryByPage(DslRequestBuild build, int scrollTime, int page, int size);

    /**
     * 快照分页查询，后返回scrollId，通过scrollId查询
     *
     * @param scrollId
     * @param scrollTime
     * @param size
     *
     * @return
     */
    HitsResponseEntity<CompanyAllTag> scrollQueryByPage(String scrollId, int scrollTime, int size);

    /**
     * 快照分页查询，查询结束后，要删除释放缓存
     *
     * @param scrollId
     */
    void deleteScrollQueryByPage(String scrollId);


}
