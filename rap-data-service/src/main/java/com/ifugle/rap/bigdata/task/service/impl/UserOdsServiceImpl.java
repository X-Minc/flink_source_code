package com.ifugle.rap.bigdata.task.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifugle.rap.bigdata.task.service.UserOdsService;
import com.ifugle.rap.constants.SjtjCode;
import com.ifugle.rap.mapper.bigdata.YhzxXnzzTpcJgryMapper;
import com.ifugle.rap.mapper.bigdata.YhzxXnzzTpcQyryMapper;
import com.ifugle.rap.mapper.bigdata.YhzxXnzzYhNsrMapper;
import com.ifugle.rap.utils.UserOds;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 20:45
 */
@Service
@Slf4j
public class UserOdsServiceImpl implements UserOdsService {

    public static final int PAGE_NUM = SjtjCode.PAGE_NUM;
    public static final int PAGE_NUM_TPC = SjtjCode.PAGE_NUM_TPC;

    @Autowired
    private YhzxXnzzTpcJgryMapper yhzxXnzzTpcJgryMapper;

    @Autowired
    private YhzxXnzzTpcQyryMapper yhzxXnzzTpcQyryMapper;

    @Autowired
    YhzxXnzzYhNsrMapper yhzxXnzzYhNsrMapper;

    @Override
    public List<UserOds> listByDeleteTpcJgry(Long xnzzId, Long startId, Date startDate, Date endDate) {
        List<UserOds> list = yhzxXnzzTpcJgryMapper.listByDeleteTpcJgry(xnzzId, startDate, endDate, startId, PAGE_NUM_TPC);
        return list;
    }

    @Override
    public List<UserOds> listByDeleteTpcQyry(Long xnzzId, Long startId, Date startDate, Date endDate) {
        List<UserOds> list = yhzxXnzzTpcQyryMapper.listByDeleteTpcQyry(xnzzId, startDate, endDate, startId, PAGE_NUM_TPC);
        return list;
    }

    @Override
    public List<UserOds> listByUpdateUser(Long xnzzId, Long startId, Date startDate, Date endDate) {
        List<UserOds> list = yhzxXnzzYhNsrMapper.listByUpdateUser(xnzzId, startDate, endDate, startId, PAGE_NUM);
        return list;
    }

    @Override
    public List<UserOds> listByUpdateTpcJgry(Long xnzzId, Long startId, Date startDate, Date endDate) {
        List<UserOds> list = yhzxXnzzTpcJgryMapper.listByAddTpcJgry(xnzzId, startDate, endDate, startId, PAGE_NUM);
        return list;
    }

    @Override
    public List<UserOds> listByUpdateTpcQyry(Long xnzzId, Long startId, Date startDate, Date endDate) {
        return yhzxXnzzTpcJgryMapper.listByAddTpcJgry(xnzzId, startDate, endDate, startId, PAGE_NUM);
    }

    @Override
    public List<UserOds> listByUpdateTpcJgryByBmys(Long xnzzId, Long startId, Date startDate, Date endDate) {
        List<UserOds> list = yhzxXnzzTpcJgryMapper.listByUpdateTpcJgryByBmys(xnzzId, startDate, endDate, startId, PAGE_NUM);
        return list;
    }
}
