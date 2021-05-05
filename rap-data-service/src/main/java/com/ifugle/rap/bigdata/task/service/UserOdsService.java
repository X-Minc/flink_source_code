package com.ifugle.rap.bigdata.task.service;

import java.util.Date;
import java.util.List;

import com.ifugle.rap.utils.UserOds;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 20:43
 */
public interface UserOdsService {


    /**
     * 根据修改时间查询(yhId != null)的第三方机构人员列表
     *
     * @param xnzzId
     * @param startId
     * @param startDate
     * @param endDate
     * @return
     */
    List<UserOds> listByDeleteTpcJgry(Long xnzzId, Long startId, Date startDate, Date endDate);

    /**
     * 根据修改时间查询(yhId != null)的第三方企业人员列表
     *
     * @param xnzzId
     * @param startId
     * @param startDate
     * @param endDate
     * @return
     */
    List<UserOds> listByDeleteTpcQyry(Long xnzzId, Long startId, Date startDate, Date endDate);
}
