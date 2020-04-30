package com.ifugle.rap.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import com.google.common.collect.Maps;
import com.ifugle.rap.common.lang.util.DateUtils;
import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrBqMapper;
import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrMapper;
import com.ifugle.rap.mapper.dsb.YhzxXnzzTpcQyMapper;
import com.ifugle.rap.model.dsb.YhzxXnzzBqNsr;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.dsb.YhzxXnzzTpcQy;
import com.ifugle.rap.model.enums.TablesEnum;
import com.ifugle.rap.service.DataSyncBqService;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.utils.CommonUtils;
import com.ifugle.util.DateUtil;
import com.ifugle.util.NullUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author LiuZhengyang
 * @version $Id$
 * @since 2018年10月16日 17:17
 */
@Service
public class DataSyncBqServiceImpl implements DataSyncBqService {
    private final static Logger logger = LoggerFactory.getLogger(DataSyncBqServiceImpl.class);

    @Value("${pageSize}")
    private Integer pageSize = 1000;
    @Autowired
    private YhzxXnzzNsrBqMapper yhzxXnzzNsrBqMapper;
    @Autowired
    private YhzxXnzzNsrMapper yhzxXnzzNsrMapper;
    @Autowired
    private YhzxXnzzTpcQyMapper yhzxXnzzTpcQyMapper;
    @Autowired
    private SyncService syncService;

    @Autowired
    private ElasticSearchBusinessService elasticSearchBusinessService;
    private AtomicBoolean isDoBq = new AtomicBoolean(false);
    private AtomicBoolean isDoNsr = new AtomicBoolean(false);
    private AtomicBoolean isDoTpc = new AtomicBoolean(false);

    private ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * 查询各个表的最新创建时间，根据最新的创建时间作为基点，同步之后的数据，同步完成之后，将最后创建时间刷新
     */
    @Override
    public void dataSyncInsertIncrementData() {
        dataSync(this::doSyncBq, isDoBq);
        dataSync(this::doSyncNsr, isDoNsr);
        dataSync(this::doSyncTpc, isDoTpc);
    }

    private void dataSync(Runnable runnable, AtomicBoolean isDo) {
        if (isDo.getAndSet(true)) {
            logger.info("insertYhzxXnzzNsrBqForSync 处理中无需重复处理");
            return;
        }
        pool.submit(runnable);
    }

    private void doSyncBq() {
        try {
            insertYhzxXnzzNsrBqForSync();
        } catch (Exception e) {
            logger.error("同步标签信息失败", e);
        } finally {
            isDoBq.set(false);
        }
    }

    private void doSyncNsr() {
        try {
            insertYhzxXnzzNsrForSync();
        } catch (Exception e) {
            logger.error("同步纳税人信息失败", e);
        } finally {
            isDoBq.set(false);
        }
    }

    private void doSyncTpc() {
        try {
            insertYhzxXnzzTpcQyForSync();
        } catch (Exception e) {
            logger.error("同步Tpc信息失败", e);
        } finally {
            isDoBq.set(false);
        }
    }

    /**
     * YhzxXnzzTpcQy,数据同步增量导入时调用
     */
    private void insertYhzxXnzzTpcQyForSync() {
        Date lastCreateTime = DateUtil.dateOf(CommonUtils.readlocalTimeFile("YHZX_XNZZ_TPC_QY"));
        if (lastCreateTime == null) {
            logger.info("insertYhzxXnzzNsrForSync lastCreateTime is null");
            lastCreateTime = DateUtil.dateAdd("dd", -7);
        }
        int pageIndex = 1;
        while (true) {
            logger.info(MessageFormat.format("#### [YHZX_XNZZ_TPC_QY] 开始同步表 YHZX_XNZZ_TPC_QY 获取本地偏移时间 updateTime : {0}", lastCreateTime));
            Integer first = (pageIndex - 1) * pageSize;
            List<YhzxXnzzTpcQy> yhzxXnzzTpcQyList = yhzxXnzzTpcQyMapper.selectYhzxXnzzTpcQyForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(yhzxXnzzTpcQyList)) {
                logger.info("[YHZX_XNZZ_TPC_QY] 查询该表的列表的size，size=" + yhzxXnzzTpcQyList.size());
                syncService.insertYhzxXnzzTpcQyAndCheckListSize(yhzxXnzzTpcQyList, pageSize);
                Date modificationDate = yhzxXnzzTpcQyList.get(yhzxXnzzTpcQyList.size() - 1).getXgsj();
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG), "YHZX_XNZZ_NSR");
                /*
                 * 该逻辑是处理大范围修改时间是相同值的情况，减少循环offset的偏移量，start
                 */
                if (modificationDate.compareTo(lastCreateTime) > 0 || yhzxXnzzTpcQyList.size() < pageSize) {
                    break;
                }
            } else {
                break;
            }
            pageIndex++;
        }
        logger.info("#### [YHZX_XNZZ_TPC_QY] 同步表数据单次结束");
    }

    /***
     * 插入yhzx_xnzz_nsr表,数据同步增量导入时调用
     */
    private void insertYhzxXnzzNsrForSync() {
        Date lastCreateTime = DateUtil.dateOf(CommonUtils.readlocalTimeFile("YHZX_XNZZ_NSR"));
        if (lastCreateTime == null) {
            logger.info("insertYhzxXnzzNsrForSync lastCreateTime is null");
            lastCreateTime = DateUtil.dateAdd("dd", -7);
        }
        logger.info(MessageFormat.format("#### [YHZX_XNZZ_NSR] 开始同步表 YHZX_XNZZ_NSR 获取本地偏移时间 updateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<YhzxXnzzNsr> yhzxXnzzNsrs = yhzxXnzzNsrMapper.selectYhzxXnzzNsrForSync(lastCreateTime, first, pageSize);
            if (NullUtil.isNull(yhzxXnzzNsrs)) {
                break;
            }

            logger.info("[YHZX_XNZZ_NSR] 查询该表的列表的size，size=" + yhzxXnzzNsrs.size());
            syncService.insertYhzxXnzzNsrAndCheckListSize(yhzxXnzzNsrs, pageSize);
            Date modifyDate = yhzxXnzzNsrs.get(yhzxXnzzNsrs.size() - 1).getXgsj();
            //注意该本地时间一定要在break之前写入。
            CommonUtils.writeLocalTimeFile(DateUtil.format(modifyDate, DateUtil.ISO8601_DATEITME_LONG), "YHZX_XNZZ_NSR");
            /***
             * 该逻辑是处理大范围修改时间是相同值的情况，减少循环offset的偏移量，start
             */
            if (modifyDate.compareTo(lastCreateTime) > 0 || yhzxXnzzNsrs.size() < pageSize) {
                break;
            }
            //end
            pageIndex++;
        }
        logger.info("#### [YHZX_XNZZ_NSR] 同步表数据单次结束");
    }

    private void insertYhzxXnzzNsrBqForSync() {
        Date lastCreateTime = DateUtil.dateOf(CommonUtils.readlocalTimeFile("YHZX_XNZZ_NSR_BQ"));
        if (null == lastCreateTime) {
            logger.info("insertYhzxXnzzNsrBqForSync lastCreateTime is null");
            lastCreateTime = DateUtil.dateAdd("dd", -1);
        }
        logger.info(MessageFormat.format("#### [YHZX_XNZZ_NSR_BQ] 开始同步表 YHZX_XNZZ_NSR_BQ 获取本地偏移时间 updateTime : {0}", lastCreateTime));
        int pageNum = 1;
        Date lastEndTime = null;
        while (true) {
            int first = getStartNum(pageNum, pageSize);
            List<YhzxXnzzBqNsr> yhzxXnzzBqNsrs = yhzxXnzzNsrBqMapper.listChange(lastCreateTime, first, pageSize);
            if (yhzxXnzzBqNsrs.isEmpty()) {
                break;
            }
            lastEndTime = yhzxXnzzBqNsrs.get(yhzxXnzzBqNsrs.size() - 1).getXgsj();
            Set<Long> nsrIds = yhzxXnzzBqNsrs.stream().map(YhzxXnzzBqNsr::getNsrId).collect(Collectors.toSet());
            Map<Long, Set<Long>> bqIdMap = Maps.newHashMap();
            List<YhzxXnzzBqNsr> changes = yhzxXnzzNsrBqMapper.listByNsrId(nsrIds);
            for (YhzxXnzzBqNsr change : changes) {
                bqIdMap.computeIfAbsent(change.getNsrId(), k -> new HashSet<>()).add(change.getBqId());
            }
            for (Long nsrId : nsrIds) {
                bqIdMap.computeIfAbsent(nsrId, k -> new HashSet<>());
            }
            String dsl = getDsl(bqIdMap);
            elasticSearchBusinessService.bulkOperation2(dsl);
            logger.info("[yhzx_xnzz_nsr_bq] sync data to es success,index = dingtax,size =" + bqIdMap.size());
            pageNum++;
        }
        if (lastEndTime != null) {
            CommonUtils.writeLocalTimeFile(DateUtil.format(lastEndTime, DateUtil.ISO8601_DATEITME_LONG), "YHZX_XNZZ_NSR_BQ");
        }
        logger.info("#### [YHZX_XNZZ_NSR_BQ] 同步表数据单次结束");
    }

    public String getDsl(Map<Long, Set<Long>> bqIdMap) {
        StringBuilder dsl = new StringBuilder(32);
        for (Map.Entry<Long, Set<Long>> entry : bqIdMap.entrySet()) {
            DataRequest request = new DataRequest();
            request.setCatalogType(TablesEnum.YHZX_XNZZ_NSR.getTableName());
            Map<String, Object> hashMap = Maps.newHashMap();
            hashMap.put("ID", entry.getKey());
            hashMap.put("BQ_IDS", entry.getValue());
            request.setMap(hashMap);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.DINGTAX.getCode(), request));
        }

        return dsl.toString();
    }

    /**
     * 获取分页起始下标
     *
     * @param curPage
     * @param pageSize
     *
     * @return
     */
    public int getStartNum(int curPage, int pageSize) {
        return (curPage <= 1) ? 0 : (curPage - 1) * pageSize;
    }

    @Override
    public void initLocalTime() {
        logger.info("init YHZX_XNZZ_NSR_BQ localhost file start");
        if (!CommonUtils.isExistDir("YHZX_XNZZ_NSR_BQ")) {
            logger.info("开始写入本地时间:YHZX_XNZZ_NSR_BQ,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtil.format(DateUtil.dateAdd("dd", -1), DateUtil.ISO8601_DATEITME_LONG), "YHZX_XNZZ_NSR_BQ");
        }

        logger.info("init YHZX_XNZZ_NSR_BQ localhost file end");
        if (!CommonUtils.isExistDir("YHZX_XNZZ_NSR")) {
            logger.info("开始写入本地时间:YHZX_XNZZ_NSR,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtil.format(DateUtil.dateAdd("dd", -7), DateUtil.ISO8601_DATEITME_LONG), "YHZX_XNZZ_NSR");
        }

        if (!CommonUtils.isExistDir("YHZX_XNZZ_TPC_QY")) {
            logger.info("开始写入本地时间:YHZX_XNZZ_TPC_QY,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtil.format(DateUtil.dateAdd("dd", -7), DateUtil.ISO8601_DATEITME_LONG), "YHZX_XNZZ_TPC_QY");
        }
    }

}
