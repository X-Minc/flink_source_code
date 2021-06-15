package com.ifugle.rap.data.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.EsTypeForm;
import com.ifugle.rap.bigdata.task.service.BiDmSwjgService;
import com.ifugle.rap.bigdata.task.service.EsCompanyRealtimeService;
import com.ifugle.rap.bigdata.task.service.EsDepartOdsService;
import com.ifugle.rap.bigdata.task.service.EsUserAllTagService;
import com.ifugle.rap.bigdata.task.service.EsUserRealtimeService;
import com.ifugle.rap.bigdata.task.service.YhzxXnzzBmService;
import com.ifugle.rap.bigdata.task.service.impl.RealTimeUpdateTaskServiceImpl;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.rap.utils.CommonUtils;
import com.ifugle.util.DateUtil;
import com.ifugle.util.NullUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 21:09
 */
@Component
@Slf4j
@Conditional(TaskCondition.class)
public class RealtimeDataTask {


    @Autowired
    private EsUserAllTagService esUserAllTagService;

    @Autowired
    private BiDmSwjgService biDmSwjgService;

    @Autowired
    private YhzxXnzzBmService yhzxXnzzBmService;

    @Autowired
    private EsDepartOdsService esDepartOdsService;

    @Autowired
    private EsUserRealtimeService esUserRealtimeService;

    @Autowired
    EsCompanyRealtimeService esCompanyRealtimeService;

    /**
     * 定时器运行状态
     */
    private static boolean running = false;


    @Scheduled(cron = "0 0/2 * * * ?")
    public void run() {
        if (isUndoTime()) {
            // 每日增量更新期间不做实时更新
            log.info("系统每天0：30~5:00之间不运行更新....");
            return;
        }
        Date currentDate = null;
        try {
            RealTimeUpdateTaskServiceImpl.taskLock.lock();
            running = true;
            log.info("增量更新实时标签表及部门汇总表数据开始STARTING");
            // 本次执行开始时间
            currentDate = yhzxXnzzBmService.getDbCurrentDate();
            // 往前20秒
            currentDate.setTime(currentDate.getTime() - 20000);

            // 从缓存中取开始时间
            String startTime = CommonUtils.readlocalTimeFile("USER_ALL_TAG"); //esRealTimeStartTimeCache.getCacha();
            if (NullUtil.isNull(startTime)) {
                // 如果没有，默认取当天0点
                startTime = DateUtil.toISO8601Date(yhzxXnzzBmService.getDbCurrentDate()) + " 00:00:00";
            }
            // 上次执行开始时间
            Date startDate = DateUtil.dateOf(startTime);

            // 获取需要进行增量抽取的虚拟组织
            List<BiDmSwjg> xnzzList = biDmSwjgService.listXnzzForUpdate();
            if(CollectionUtils.isNotEmpty(xnzzList)) {
                log.info("获取需要进行增量抽取的虚拟组织,数量为："+xnzzList.size());
            }
            for (BiDmSwjg xnzz : xnzzList) {
                // 增量抽取部门数据到ES
                log.info("开始同步depart数据：xnzz = {}, startTime = {}", JSON.toJSONString(xnzz),DateUtil.toISO8601DateTime(startDate));
                esDepartOdsService.insertOrUpdateDepartToEsByXnzz(xnzz, startDate);
                // 更新增量实时用户标签数据
                log.info("更新增量实时用户标签数据：xnzz = {}, startTime = {}", JSON.toJSONString(xnzz),DateUtil.toISO8601DateTime(startDate));
                esUserRealtimeService.updateUserRealTimeByAdd(xnzz, startDate);
                // 更新增量实时企业标签数据
                log.info("更新增量实时企业标签数据：xnzz = {}, startTime = {}", JSON.toJSONString(xnzz),DateUtil.toISO8601DateTime(startDate));
                esCompanyRealtimeService.updateCompanyRealTimeByAdd(xnzz, startDate);
                // 删除用户全量实时标签表中无效数据
                EsTypeForm all = new EsTypeForm(EsIndexConstant.USER_ALL_TAG, null);
                log.info("删除用户全量实时标签表中无效数据：xnzz = {}, startTime = {}", JSON.toJSONString(xnzz),DateUtil.toISO8601DateTime(startDate));
                esUserAllTagService.deleteInvalidUserAllTagByXnzzId(xnzz.getXnzzId(), startDate, null, all);

            }
        } finally {
            RealTimeUpdateTaskServiceImpl.taskLock.unlock();
        }

        // 执行结束后更新下次更新开始时间到缓存
        log.info("执行结束后更新下次更新开始时间到缓存,时间为："+DateUtil.toISO8601DateTime(currentDate));
        CommonUtils.writeLocalTimeFile(DateUtil.toISO8601DateTime(currentDate),"USER_ALL_TAG");

        log.info("增量更新实时标签表及部门汇总表数据结束");
        running = false;
    }


    /**
     * 每天0：30~5:00之间不运行更新，防止与每日增量和全量数据更新冲突
     * @return
     */
    private boolean isUndoTime() {
        Calendar currentTime = Calendar.getInstance();
        Long currentMillis = currentTime.getTimeInMillis();
        // 不运行开始时间
        currentTime.set(Calendar.HOUR_OF_DAY, 0);
        currentTime.set(Calendar.MINUTE, 30);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);
        Long undoStartMillis = currentTime.getTimeInMillis();
        // 不运行结束时间
        currentTime.set(Calendar.HOUR_OF_DAY, 5);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);
        Long undoEndMillis = currentTime.getTimeInMillis();
        if (currentMillis >= undoStartMillis && currentMillis <= undoEndMillis) {
            return true;
        }
        return false;
    }

}