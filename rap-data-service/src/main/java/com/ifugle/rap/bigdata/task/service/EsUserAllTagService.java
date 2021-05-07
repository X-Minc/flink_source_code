package com.ifugle.rap.bigdata.task.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ifugle.rap.bigdata.task.CompanyOds;
import com.ifugle.rap.bigdata.task.EsTypeForm;
import com.ifugle.rap.bigdata.task.UserAllTag;
import com.ifugle.rap.bigdata.task.es.DslRequestBuild;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 20:31
 */
public interface EsUserAllTagService {

    /**
     * 删除用户全量标签表中的无效用户
     *
     * @param type
     * @param startDate
     * @param endDate
     * @param xnzzId
     */
    void deleteInvalidUserAllTagByXnzzId(Long xnzzId, Date startDate, Date endDate, EsTypeForm... type);

    void deleteEsAlreadyExistJgry(Date startDate, Date endDate, Long xnzzId, EsTypeForm... type);

    void deleteEsAlreadyExistQyry(Date startDate, Date endDate, Long xnzzId, EsTypeForm... type);

    /**
     * 根据keyId列表，查询全量标签表的数据
     *
     * @param keyIds
     */
    List<UserAllTag> listByKeyIds(List<String> keyIds);

    /**
     * 更新有部门修改的企业的中间表企业用户的部门ID
     *
     * @param companyMap
     */
    void updateTpcQyryBm(Map<Long, CompanyOds> companyMap);
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
    SearchResponseEntity<UserAllTag> scrollQueryByPage(Map<String, Object> querySearch, int scrollTime, int page, int size);

    /**
     * 快照分页查询，分页查询增量数据(指定索引)
     *
     * @param index
     * @param querySearch
     * @param scrollTime
     * @param page
     * @param size
     *
     * @return
     */
    SearchResponseEntity<UserAllTag> scrollQueryByPage(String index, Map<String, Object> querySearch, int scrollTime, int page, int size);

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
    SearchResponseEntity<UserAllTag> scrollQueryByPage(DslRequestBuild build, int scrollTime, int page, int size);

    /**
     * 快照分页查询，后返回scrollId，通过scrollId查询
     *
     * @param scrollId
     * @param scrollTime
     * @param size
     *
     * @return
     */
    HitsResponseEntity<UserAllTag> scrollQueryByPage(String scrollId, int scrollTime, int size);

    /**
     * 快照分页查询，查询结束后，要删除释放缓存
     *
     * @param scrollId
     */
    void deleteScrollQueryByPage(String scrollId);
}
