package com.ifugle.rap.bigdata.task.service;

import java.util.Date;
import java.util.List;

import com.ifugle.rap.bigdata.task.CompanyOds;
import com.ifugle.rap.bigdata.task.YhzxXnzzNsrBq;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 13:24
 */
public interface CompanyOdsService {

    /**
     * 根据ID查询企业信息
     *
     * @param xnzzId
     * @param nsrIds
     * @return
     */
    List<CompanyOds> listByIds(Long xnzzId, List<Long> nsrIds);


    /**
     * 根据修改时间查询时间范围内的企业
     *
     * @param xnzzId
     * @param startId
     * @param startDate
     * @param endDate
     * @return
     */
    List<CompanyOds> listByUpdateCompany(Long xnzzId, Long startId, Date startDate, Date endDate);

    /**
     * 根据ID查询企业标签
     *
     * @param xnzzId
     * @param nsrIds
     * @return
     */
    List<YhzxXnzzNsrBq> listBqByNsrId(Long xnzzId, List<Long> nsrIds);
}
