package com.ifugle.rap.bigdata.task.service;

import java.util.Date;
import java.util.List;

import com.ifugle.rap.bigdata.task.EsTypeForm;
import com.ifugle.rap.bigdata.task.UserAllTag;

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

}
