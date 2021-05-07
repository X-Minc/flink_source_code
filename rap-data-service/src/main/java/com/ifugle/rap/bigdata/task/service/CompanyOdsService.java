package com.ifugle.rap.bigdata.task.service;

import java.util.List;

import com.ifugle.rap.bigdata.task.CompanyOds;

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

}
