package com.ifugle.rap.bigdata.task.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.ifugle.rap.bigdata.task.service.*;
import com.ifugle.rap.constants.EsCode;
import com.ifugle.rap.utils.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.EsTypeForm;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.util.DateUtil;
import com.ifugle.util.NullUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:14
 */
@Service
@Slf4j
public class RealTimeUpdateTaskServiceImpl implements RealTimeUpdateTaskService {

    public static Lock taskLock = new ReentrantLock();

    @Autowired
    private BiDmSwjgService biDmSwjgService;

    @Autowired
    private EsDepartOdsService esDepartOdsService;

    @Autowired
    private EsUserAllTagService esUserAllTagService;

    @Autowired
    EsUserRealtimeService esUserRealtimeService;

    @Autowired
    EsCompanyRealtimeService esCompanyRealtimeService;

    @Autowired
    private EsDepartAggDwService esDepartAggDwService;


    @Override
    public void getUpdateDataToEs(String startTime) {
        log.info("增量抽取实时数据开始");
        Date startDate = DateUtil.dateOf(startTime);
        try {
            RealTimeUpdateTaskServiceImpl.taskLock.lock();
            // 获取需要进行增量抽取的虚拟组织
            List<BiDmSwjg> xnzzList = biDmSwjgService.listXnzzForUpdate();

            for (BiDmSwjg xnzz : xnzzList) {
                // 增量抽取部门数据
                esDepartOdsService.insertOrUpdateDepartToEsByXnzz(xnzz, startDate);
                // 更新增量实时用户标签数据
                esUserRealtimeService.updateUserRealTimeByAdd(xnzz, startDate);
                // 更新增量实时企业标签数据
                esCompanyRealtimeService.updateCompanyRealTimeByAdd(xnzz, startDate);
                // 删除用户全量实时标签表中无效数据
                EsTypeForm all = new EsTypeForm(EsIndexConstant.USER_ALL_TAG, null);
                esUserAllTagService.deleteInvalidUserAllTagByXnzzId(xnzz.getXnzzId(), startDate, null, all);
            }
        } finally {
            RealTimeUpdateTaskServiceImpl.taskLock.unlock();
        }
        log.info("增量抽取实时数据结束");
    }

    @Override
    public void getUpdateDataToEs(Long xnzzId) {
        log.info("全量抽取实时数据开始");
        try {
            RealTimeUpdateTaskServiceImpl.taskLock.lock();
            // 获取需要进行增量抽取的虚拟组织
            BiDmSwjg xnzz = biDmSwjgService.getXnzzForUpdate(xnzzId);
            if(xnzz==null){
                return;
            }
            // 增量抽取部门数据
            Set<Long> bmForDept =esDepartOdsService.insertOrUpdateDepartToEsByXnzz(xnzz, null);
            // 更新增量实时用户标签数据
            Set<Long> bmForUser =esUserRealtimeService.updateUserRealTimeByAdd(xnzz, null);
            // // 更新增量实时企业标签数据
            Set<Long> bmForCompany=esCompanyRealtimeService.updateCompanyRealTimeByAdd(xnzz, null);
            // 删除用户全量实时标签表中无效数据
            EsTypeForm all = new EsTypeForm(EsIndexConstant.USER_ALL_TAG, null);
            esUserAllTagService.deleteInvalidUserAllTagByXnzzId(xnzz.getXnzzId(), null, null, all);



            // 按虚拟组织ID去重部门ID
            Set<Long> bmIdsSet = Sets.newHashSet();
            if (NullUtil.isNotNull(bmForDept)) {
                bmIdsSet.addAll(bmForDept);
            }
            if (NullUtil.isNotNull(bmForUser)) {
                bmIdsSet.addAll(bmForUser);
            }
            if (NullUtil.isNotNull(bmForCompany)) {
                bmIdsSet.addAll(bmForCompany);
            }

            if (NullUtil.isNotNull(bmIdsSet)) {
                List<Long> list = Lists.newArrayList(bmIdsSet);
                List<List<Long>> bmIdsList = ListUtil.split(list, EsCode.BM_BATCH_NUM);
                for (List<Long> bmIds : bmIdsList) {
                    esDepartAggDwService.aggregateStatisticsByBmIds(bmIds);
                }
            }
        } finally {
            RealTimeUpdateTaskServiceImpl.taskLock.unlock();
        }
        log.info("全量抽取实时数据结束");
    }
}
