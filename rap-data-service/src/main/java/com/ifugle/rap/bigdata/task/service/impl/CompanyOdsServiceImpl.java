package com.ifugle.rap.bigdata.task.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifugle.rap.bigdata.task.CompanyOds;
import com.ifugle.rap.bigdata.task.YhzxXnzzNsrBq;
import com.ifugle.rap.bigdata.task.service.CompanyOdsService;
import com.ifugle.rap.constants.SjtjCode;
import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 13:25
 */
@Service
@Slf4j
public class CompanyOdsServiceImpl implements CompanyOdsService {

    public static final int PAGE_NUM = SjtjCode.PAGE_NUM;

    @Autowired
    private YhzxXnzzNsrMapper yhzxXnzzNsrMapper;

    @Override
    public List<CompanyOds> listByIds(Long xnzzId, List<Long> nsrIds) {
        return yhzxXnzzNsrMapper.listByIds(xnzzId, nsrIds);
    }

    /**
     * 根据修改时间查询时间范围内的企业
     *
     * @param xnzzId
     * @param startId
     * @param startDate
     * @param endDate
     *
     * @return
     */
    @Override
    public List<CompanyOds> listByUpdateCompany(Long xnzzId, Long startId, Date startDate, Date endDate) {
        List<CompanyOds> list = yhzxXnzzNsrMapper.listByUpdateCompany(xnzzId, startDate, endDate, startId, PAGE_NUM);
        return list;
    }

    @Override
    public List<YhzxXnzzNsrBq> listBqByNsrId(Long xnzzId, List<Long> nsrIds) {
        return yhzxXnzzNsrMapper.listBqByNsrId(xnzzId, nsrIds);
    }
}
