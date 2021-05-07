package com.ifugle.rap.bigdata.task.service;

import java.util.List;

import com.ifugle.rap.bigdata.task.CompanyOds;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 13:58
 */
public interface AllDataOdsTaskService {
    /**
     * 按纳税人ID查询出用户标签表数据，并更新企业字段
     *
     * @param companyList
     */
    void updateUserAllTagCompanyInfo(List<CompanyOds> companyList);
}
