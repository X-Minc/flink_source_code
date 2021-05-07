package com.ifugle.rap.bigdata.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ifugle.rap.bigdata.task.CompanyOds;
import com.ifugle.rap.bigdata.task.service.CompanyOdsService;
import com.ifugle.rap.constants.SjtjCode;
import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrMapper;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 13:25
 */
public class CompanyOdsServiceImpl implements CompanyOdsService {

    public static final int PAGE_NUM = SjtjCode.PAGE_NUM;

    @Autowired
    private YhzxXnzzNsrMapper yhzxXnzzNsrMapper;

    @Override
    public List<CompanyOds> listByIds(Long xnzzId, List<Long> nsrIds) {
        return yhzxXnzzNsrMapper.listByIds(xnzzId, nsrIds);
    }
}
