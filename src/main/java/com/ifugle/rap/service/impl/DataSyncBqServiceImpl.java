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
import com.ifugle.rap.model.dsb.YhzxXnzzBqNsr;
import com.ifugle.rap.model.enums.TablesEnum;
import com.ifugle.rap.service.DataSyncBqService;
import com.ifugle.rap.utils.CommonUtils;
import com.ifugle.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private ElasticSearchBusinessService elasticSearchBusinessService;
    private AtomicBoolean isDo = new AtomicBoolean(false);
    private ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * 查询各个表的最新创建时间，根据最新的创建时间作为基点，同步之后的数据，同步完成之后，将最后创建时间刷新
     */
    @Override
    public void dataSyncInsertIncrementData() {
        if (isDo.getAndSet(true)) {
            logger.info("insertYhzxXnzzNsrBqForSync 处理中无需重复处理");
            return;
        }
        pool.submit(this::doSync);
    }

    private void doSync() {
        try {
            insertYhzxXnzzNsrBqForSync();
        } catch (Exception e) {
            logger.error("同步标签标记失败", e);
        } finally {
            isDo.set(false);
        }
    }

    private void insertYhzxXnzzNsrBqForSync() {
        Date lastCreateTime = DateUtil.dateOf(CommonUtils.readlocalTimeFile("YHZX_XNZZ_NSR_BQ"));
        if (null == lastCreateTime) {
            logger.info("insertYhzxXnzzNsrBqForSync lastCreateTime is null");
            lastCreateTime = DateUtil.dateAdd("mi", -30);
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
            YhzxXnzzBqNsr lastBq = yhzxXnzzBqNsrs.get(yhzxXnzzBqNsrs.size() - 1);
            lastEndTime = lastBq.getXgsj();
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
            CommonUtils.writeLocalTimeFile(DateUtil.format(DateUtil.dateAdd("mi", -30), DateUtil.ISO8601_DATEITME_LONG), "YHZX_XNZZ_NSR_BQ");
        }
        logger.info("init YHZX_XNZZ_NSR_BQ localhost file end");
    }

}
