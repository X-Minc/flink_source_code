package com.ifugle.rap.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ifugle.rap.constants.SystemConstants;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.mapper.BizDataMapper;
import com.ifugle.rap.mapper.BotChatResponseMessageDOMapper;
import com.ifugle.rap.mapper.BotConfigServerMapper;
import com.ifugle.rap.mapper.BotMediaDOMapper;
import com.ifugle.rap.mapper.BotTrackDetailDOMapper;
import com.ifugle.rap.mapper.BotUnawareDetailDOMapper;
import com.ifugle.rap.mapper.KbsArticleDOMapper;
import com.ifugle.rap.mapper.KbsKeywordDOMapper;
import com.ifugle.rap.mapper.KbsQuestionArticleDOMapper;
import com.ifugle.rap.mapper.KbsQuestionDOMapper;
import com.ifugle.rap.mapper.KbsReadingDOMapper;
import com.ifugle.rap.mapper.KbsTagDTOMapper;
import com.ifugle.rap.mapper.YhzxxnzzcyDOMapper;
import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrMapper;
import com.ifugle.rap.mapper.zhcs.ZxArticleMapper;
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.shuixiaomi.BizData;
import com.ifugle.rap.model.shuixiaomi.BotChatResponseMessageDO;
import com.ifugle.rap.model.shuixiaomi.BotConfigServer;
import com.ifugle.rap.model.shuixiaomi.BotMediaDO;
import com.ifugle.rap.model.shuixiaomi.BotTrackDetailDO;
import com.ifugle.rap.model.shuixiaomi.BotUnawareDetailDO;
import com.ifugle.rap.model.shuixiaomi.KbsArticleDOWithBLOBs;
import com.ifugle.rap.model.shuixiaomi.KbsKeywordDO;
import com.ifugle.rap.model.shuixiaomi.KbsQuestionArticleDO;
import com.ifugle.rap.model.shuixiaomi.KbsQuestionDO;
import com.ifugle.rap.model.shuixiaomi.KbsReadingDOWithBLOBs;
import com.ifugle.rap.model.zhcs.ZxArticle;
import com.ifugle.rap.security.crypto.CryptBase36;
import com.ifugle.rap.security.crypto.CryptBase62;
import com.ifugle.rap.security.crypto.CryptSimple;
import com.ifugle.rap.service.DataSyncService;
import com.ifugle.rap.service.thread.BotBizDataInitThread;
import com.ifugle.rap.service.thread.BotChatResponseMessageInitThread;
import com.ifugle.rap.service.thread.BotConfigServerInitThread;
import com.ifugle.rap.service.thread.BotMediaInitThread;
import com.ifugle.rap.service.thread.BotTrackDetailInitThread;
import com.ifugle.rap.service.thread.BotUnawareDetailInitThread;
import com.ifugle.rap.service.thread.KbsArticleInitThread;
import com.ifugle.rap.service.thread.KbsKeywordInitThread;
import com.ifugle.rap.service.thread.KbsQuestionArticleInitThread;
import com.ifugle.rap.service.thread.KbsQuestionInitThread;
import com.ifugle.rap.service.thread.KbsReadingInitThread;
import com.ifugle.rap.service.thread.KbsTagInitThread;
import com.ifugle.rap.service.thread.YhzxXnzzNsrInitThread;
import com.ifugle.rap.service.thread.ZxArticleInitThread;
import com.ifugle.rap.service.utils.BizListCheckUtils;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.utils.TimeDelayUtils;
import com.ifugle.rap.utils.CommonUtils;
import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.security.crypto.CryptZip;
import com.ifugle.rap.utils.DecodeUtils;

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
public class DataSyncServiceImpl implements DataSyncService {

    @Value("${pageSize}")
    Integer pageSize = 1000;

    private static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("demo-POOL-%d").build();
    private static final ExecutorService POOL = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), NAMED_THREAD_FACTORY);

    @Autowired
    private BotChatResponseMessageDOMapper botChatResponseMessageDOMapper;

    @Autowired
    private BotTrackDetailDOMapper botTrackDetailDOMapper;

    @Autowired
    private BotUnawareDetailDOMapper botUnawareDetailDOMapper;

    @Autowired
    private KbsArticleDOMapper kbsArticleDOMapper;

    @Autowired
    private KbsQuestionArticleDOMapper kbsQuestionArticleDOMapper;

    @Autowired
    private KbsQuestionDOMapper kbsQuestionDOMapper;

    @Autowired
    private KbsReadingDOMapper kbsReadingDOMapper;

    @Autowired
    private KbsKeywordDOMapper kbsKeywordDOMapper;

    @Autowired
    private YhzxxnzzcyDOMapper yhzxxnzzcyDOMapper;

    @Autowired
    private BotMediaDOMapper botMediaDOMapper;

    @Autowired
    private KbsTagDTOMapper kbsTagDTOMapper;

    @Autowired
    private ZxArticleMapper zxArticleMapper;

    @Autowired
    private YhzxXnzzNsrMapper yhzxXnzzNsrMapper;

    @Autowired
    private BotConfigServerMapper botConfigServerMapper;

    @Autowired
    private ElasticSearchBusinessService elasticSearchBusinessService;

    @Autowired
    private SyncService syncService;

    @Autowired
    private CryptZip cryptZip;

    @Autowired
    private CompriseUtils compriseUtils;

    @Autowired
    private BizDataMapper bizDataMapper;

    private final static Logger logger = LoggerFactory.getLogger(DataSyncServiceImpl.class);

    /**
     * 数据全量同步
     */
    @Override
    public void dataSyncInit() {
        logger.info("[DataSyncServiceImpl] dataSyncInit start");
        KbsArticleInitThread kbsArticleInitThread = new KbsArticleInitThread(500, this.syncService, this.kbsArticleDOMapper);
        BotUnawareDetailInitThread botUnawareDetailInitThread = new BotUnawareDetailInitThread(pageSize, this.syncService, this.botUnawareDetailDOMapper);
        BotTrackDetailInitThread botTrackDetailInitThread = new BotTrackDetailInitThread(pageSize, this.syncService, this.botTrackDetailDOMapper);
        BotChatResponseMessageInitThread botChatResponseMessageInitThread = new BotChatResponseMessageInitThread(pageSize, this.syncService, this.botChatResponseMessageDOMapper);
        KbsQuestionArticleInitThread kbsQuestionArticleInitThread = new KbsQuestionArticleInitThread(pageSize, this.syncService, this.kbsQuestionArticleDOMapper);
        KbsQuestionInitThread kbsQuestionInitThread = new KbsQuestionInitThread(pageSize, this.syncService, this.kbsQuestionDOMapper);
        ZxArticleInitThread zxArticleInitThread = new ZxArticleInitThread(pageSize, this.syncService, this.zxArticleMapper);
        YhzxXnzzNsrInitThread yhzxXnzzNsrInitThread = new YhzxXnzzNsrInitThread(pageSize, this.syncService, this.yhzxXnzzNsrMapper);
        KbsReadingInitThread kbsReadingInitThread = new KbsReadingInitThread(pageSize, this.syncService, this.kbsReadingDOMapper);
        KbsKeywordInitThread kbsKeywordInitThread = new KbsKeywordInitThread(pageSize, this.syncService, this.kbsKeywordDOMapper);
        BotMediaInitThread botMediaInitThread = new BotMediaInitThread(pageSize, this.syncService, this.cryptZip, this.botMediaDOMapper);
        KbsTagInitThread kbsTagInitThread = new KbsTagInitThread(pageSize, this.syncService, this.kbsTagDTOMapper);
        BotBizDataInitThread botBizDataInitThread = new BotBizDataInitThread(pageSize, this.syncService, this.bizDataMapper);
        BotConfigServerInitThread botConfigServerInitThread = new BotConfigServerInitThread(pageSize, this.syncService, this.botConfigServerMapper);
        POOL.submit(kbsArticleInitThread);
        POOL.submit(botUnawareDetailInitThread);
        POOL.submit(botTrackDetailInitThread);
        POOL.submit(botChatResponseMessageInitThread);
        POOL.submit(kbsQuestionArticleInitThread);
        POOL.submit(kbsQuestionInitThread);
        // 是否开启智慧财税导入
        if (Boolean.valueOf(System.getProperty(SystemConstants.ZHCS_ON))) {
            //智慧财税导入
            POOL.submit(zxArticleInitThread);
        }
        POOL.submit(kbsReadingInitThread);
        POOL.submit(kbsKeywordInitThread);
        POOL.submit(botMediaInitThread);
        POOL.submit(kbsTagInitThread);
        //特别注意存在加解密的问题，容易引起线程阻塞
        POOL.submit(botBizDataInitThread);
        POOL.submit(botConfigServerInitThread);
        // 是否开启丁税宝导入
        if (Boolean.valueOf(System.getProperty(SystemConstants.DSB_ON))) {
            //丁税宝企业导入
            POOL.submit(yhzxXnzzNsrInitThread);
        }
    }

    /**
     * 查询各个表的最新创建时间，根据最新的创建时间作为基点，同步之后的数据，同步完成之后，将最后创建时间刷新
     */
    @Override
    public void dataSyncInsertIncrementData() {
        insertBotUnawareDetailForSync();
        insertBotTrackDetailForSync();
        insertBotChatResponseMessageForSync();
        insertKbsQuestionArticleForSync();
        insertKbsQuestionForSync();
        //智慧财税导入
        if (Boolean.valueOf(System.getProperty(SystemConstants.ZHCS_ON))) {
            insertZxArticleForSync();
        }
        insertKbsArticleForSync();
        insertKbsReadingForSync();
        insertKbsKeywordForSync();
        insertBotMediaForSync();
        insertBotBizDataForSync();  //特别注意存在加解密的问题，容易引起线程阻塞
        insertBotConfigServerForSync();
        if (Boolean.valueOf(System.getProperty(SystemConstants.DSB_ON))) {
            //丁税宝企业导入
            insertYhzxXnzzNsrForSync();
        }

    }

    /**
     * KBS_ARTICLE KBS_READING KBS_QUESTION_ARTICLE KBS_QUESTION KBS_KEYWORD
     */
    @Override
    public void dataUpdateSync() {
        updateKbsArticleForSync();
        updateKbsKeywordForSync();
        updateKbsQuestionArticleForSync();
        updateKbsReadingForSync();
        updateKbsQuestionForSync();
        //特别注意存在加解密的问题，容易引起线程阻塞
        updateBotBizDataForSync();
        if (Boolean.valueOf(System.getProperty(SystemConstants.ZHCS_ON))) {
            updateZxArticleForSync();
        }
        updateBotUnawareDetailForSync();
        updateBotMediaForSync();
        updateBotConfigServerForSync();
        if (Boolean.valueOf(System.getProperty(SystemConstants.DSB_ON))) {
            //丁税宝企业导入
            //updateYhzxXnzzNsrForSync(); TODO优化性能瓶颈
        }
    }

    /**
     * 同步KBS_ARTICLE 表的内容，更新同步的时候调用
     */
    public void updateKbsArticleForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("KBS_ARTICLE_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_ARTICLE_UPDATE");
        }
        logger.info(MessageFormat.format("updateKbsArticleForSync 获得最后更新时间为 : {0}", lastUpdateTime));
        int pageIndex = 1;

        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            try {
                List<KbsArticleDOWithBLOBs> kbsArticleDOWithBLOBs = checkKbsArticleWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(kbsArticleDOWithBLOBs)) {
                    //读取到list的话，修改本地存储的时间
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_ARTICLE_UPDATE");
                }
                logger.info("======================== 已经存在了,开始实时更新 ================================");
                if (updateKbsArticleAndCheckListSize(kbsArticleDOWithBLOBs, pageSize)) {
                    break;
                }
                logger.info("updateKbsArticleForSync loop times:" + pageIndex + ",list =" + kbsArticleDOWithBLOBs.size());
                Thread.sleep(1000);
            } catch (Exception ex) {
                logger.error(MessageFormat.format("updateKbsArticleForSync sleep error , {0}", lastUpdateTime), ex);
            }
            pageIndex++;
        }

        CommonUtils.writeLocalTimeFile("1", "KBS_ARTICLE_UPDATE_STATUS");
    }

    private List<KbsArticleDOWithBLOBs> checkKbsArticleWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("KBS_ARTICLE_UPDATE_STATUS"))) {
            return kbsArticleDOMapper.selectKbsArticleForUpdateSyncWithLastUpdateTime(lastUpdateTime, first, pageSize);
        } else {
            return kbsArticleDOMapper.selectKbsArticleForUpdateSyncWithoutLastUpdateTime(first, pageSize);
        }
    }

    /**
     * 同步KBS_READING 表的内容，更新同步的时候调用
     */
    private void updateKbsReadingForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("KBS_READING_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_READING_UPDATE");
        }
        logger.info(MessageFormat.format("updateKbsReadingForSync 获得最后更新时间 : {0}", lastUpdateTime));
        int pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<KbsReadingDOWithBLOBs> kbsReadingDOWithBLOBs = checkKbsReadingWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(kbsReadingDOWithBLOBs)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_READING_UPDATE");
                }
                if (updateKbsReadingAndCheckListSize(kbsReadingDOWithBLOBs, pageSize)) {
                    break;
                }
                logger.info("updateKbsReadingForSync loop times:" + pageIndex + ",list =" + kbsReadingDOWithBLOBs.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "KBS_READING_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateKbsReadingForSync sleep error, {0}", ex));
        }

    }

    private List<KbsReadingDOWithBLOBs> checkKbsReadingWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("KBS_READING_UPDATE_STATUS"))) {
            return kbsReadingDOMapper.selectKbsReadingForUpdateSyncWithLastUpdateTime(lastUpdateTime, first, pageSize);
        } else {
            return kbsReadingDOMapper.selectKbsReadingForUpdateSyncWithoutLastUpdateTime(first, pageSize);
        }
    }

    /**
     * 同步KBS_QUESTION_ARTICLE 表的内容，更新同步的时候调用
     */
    private void updateKbsQuestionArticleForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("KBS_QUESTION_ARTICLE_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_QUESTION_ARTICLE_UPDATE");
        }
        logger.info(MessageFormat.format("updateKbsQuestionArticleForSync 获得最后更新时间 : {0}", lastUpdateTime));
        int pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<KbsQuestionArticleDO> kbsQuestionArticleDOS = checkKbsQuestionArticleWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(kbsQuestionArticleDOS)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_QUESTION_ARTICLE_UPDATE");
                }
                if (updateKbsQuestionArticleAndCheckListSize(kbsQuestionArticleDOS, pageSize)) {
                    break;
                }
                logger.info("updateKbsQuestionArticleForSync loop times:" + pageIndex + ",list =" + kbsQuestionArticleDOS.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "KBS_QUESTION_ARTICLE_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateKbsQuestionArticleForSync sleep error, {0}", ex));
        }

    }

    private List<KbsQuestionArticleDO> checkKbsQuestionArticleWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("KBS_QUESTION_ARTICLE_UPDATE_STATUS"))) {
            return kbsQuestionArticleDOMapper.selectKbsQuestionArticleForUpdateSyncWithLastUpdateTime(lastUpdateTime, first, pageSize);
        } else {
            return kbsQuestionArticleDOMapper.selectKbsQuestionArticleForUpdateSyncWithoutLastUpdateTime(first, pageSize);
        }
    }

    /**
     * 同步KBS_QUESTION 表的内容，更新同步的时候调用
     */
    private void updateKbsQuestionForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("KBS_QUESTION_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_QUESTION_UPDATE");
        }
        logger.info(MessageFormat.format("updateKbsQuestionForSync 获得最后更新时间 : {0}", lastUpdateTime));
        int pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<KbsQuestionDO> kbsQuestionDOS = checkKbsQuestionWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(kbsQuestionDOS)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_QUESTION_UPDATE");
                }
                if (updateKbsQuestionAndCheckListSize(kbsQuestionDOS, pageSize)) {
                    break;
                }
                logger.info("updateKbsQuestionForSync loop times:" + pageIndex + ",list =" + kbsQuestionDOS.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "KBS_QUESTION_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateKbsQuestionForSync sleep error, {0}", ex));
        }

    }

    private List<KbsQuestionDO> checkKbsQuestionWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("KBS_QUESTION_UPDATE_STATUS"))) {
            return kbsQuestionDOMapper.selectKbsQuestionForUpdateWithLastUpdateTime(lastUpdateTime, first, pageSize);
        } else {
            return kbsQuestionDOMapper.selectKbsQuestionForUpdateWithoutLastUpdateTime(first, pageSize);
        }
    }

    /**
     * 同步 BOT_BIZ_DATA 表的内容,更新同步的时候
     */
    public void updateBotBizDataForSync() {
        // 该表需要解密,且每行数据量庞大,故每次只查询500条
        int pageSize = 500;
        String lastUpdateTime = CommonUtils.readlocalTimeFile("BOT_BIZ_DATA_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "BOT_BIZ_DATA_UPDATE");
        }
        logger.info(MessageFormat.format("updateBotBizDataForSync 获得最后更新时间 : {0}", lastUpdateTime));
        int pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<BizData> bizDataList = checkBotBizDataWithLastUpdateTime(lastUpdateTime, first,pageSize);
                if (!CollectionUtils.isEmpty(bizDataList)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "BOT_BIZ_DATA_UPDATE");
                }
                if (updateBotBizDataAndCheckListSize(bizDataList, pageSize)) {
                    break;
                }
                logger.info("updateBotBizDataForSync loop times:" + pageIndex + ",list =" + bizDataList.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "BOT_BIZ_DATA_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateKbsQuestionForSync sleep error, {0}", ex));
        }

    }

    private List<BizData> checkBotBizDataWithLastUpdateTime(String lastUpdateTime, Integer first, Integer pageSize) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("BOT_BIZ_DATA_UPDATE_STATUS"))) {
            return bizDataMapper.selectBotBizDataForUpdateWithLastUpdateTime(lastUpdateTime, first, pageSize);
        } else {
            return bizDataMapper.selectBotBizDataForUpdateWithoutLastUpdateTime(first, pageSize);
        }
    }

    /**
     * 同步KBS_QUESTION 表的内容，更新同步的时候调用
     */
    private void updateZxArticleForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("ZX_ARTICLE_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "ZX_ARTICLE_UPDATE");
        }
        logger.info(MessageFormat.format("updateZxArticleForSync 获得最后更新时间 : {0}", lastUpdateTime));
        int pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<ZxArticle> zxArticles = checkZxArticleWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(zxArticles)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "ZX_ARTICLE_UPDATE");
                }
                if (updateZxArticleAndCheckListSize(zxArticles, pageSize)) {
                    break;
                }
                logger.info("updateZxArticleForSync loop times:" + pageIndex + ",list =" + zxArticles.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "ZX_ARTICLE_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateKbsQuestionForSync sleep error, {0}", ex));
        }

    }

    private List<ZxArticle> checkZxArticleWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("ZX_ARTICLE_UPDATE_STATUS"))) {
            return zxArticleMapper.selectZxArticleForUpdateWithLastUpdateTime(lastUpdateTime, first, pageSize);
        } else {
            return zxArticleMapper.selectZxArticleForUpdateWithoutLastUpdateTime(first, pageSize);
        }
    }

    /**
     * 同步YHZX_XNZZ_NSR 表的内容，更新同步的时候调用
     */
    private void updateYhzxXnzzNsrForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("YHZX_XNZZ_NSR_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "YHZX_XNZZ_NSR_UPDATE");
        }
        logger.info(MessageFormat.format("updateYhzxXnzzNsrForSync 获得最后更新时间 : {0}", lastUpdateTime));
        int pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<YhzxXnzzNsr> yhzxXnzzNsrs = checkYhzxXnzzNsrWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(yhzxXnzzNsrs)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "YHZX_XNZZ_NSR_UPDATE");
                }
                if (updateYhzxXnzzNsrAndCheckListSize(yhzxXnzzNsrs, pageSize)) {
                    break;
                }
                logger.info("updateYhzxXnzzNsrForSync loop times:" + pageIndex + ",list =" + yhzxXnzzNsrs.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "YHZX_XNZZ_NSR_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateYhzxXnzzNsrForSync sleep error, {0}", ex));
        }
    }

    private List<YhzxXnzzNsr> checkYhzxXnzzNsrWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("YHZX_XNZZ_NSR_UPDATE_STATUS"))) {
            return yhzxXnzzNsrMapper.selectYhzxXnzzNsrForUpdateWithLastUpdateTime(lastUpdateTime, first, pageSize);
        } else {
            return yhzxXnzzNsrMapper.selectYhzxXnzzNsrForUpdateWithoutLastUpdateTime(first, pageSize);
        }
    }

    /**
     * 同步KBS_KEYWORD 表的内容，更新同步的时候调用
     */
    private void updateKbsKeywordForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("KBS_KEYWORD_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_KEYWORD_UPDATE");
        }
        logger.info(MessageFormat.format("updateKbsKeywordForSync 获得最后更新时间 : {0}", lastUpdateTime));
        Integer pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<KbsKeywordDO> kbsKeywordDOS = checkKbsKeywordWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(kbsKeywordDOS)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "KBS_KEYWORD_UPDATE");
                }
                if (updateKbsKeywordAndCheckListSize(kbsKeywordDOS, pageSize)) {
                    break;
                }
                logger.info("updateKbsKeywordForSync loop times:" + pageIndex + ",list =" + kbsKeywordDOS.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "KBS_KEYWORD_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateKbsKeywordForSync sleep error, {0}", ex));
        }
    }

    private List<KbsKeywordDO> checkKbsKeywordWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("KBS_KEYWORD_UPDATE_STATUS"))) {
            return kbsKeywordDOMapper.selectKbsKeywordForUpdateSyncWithLastUpdateTime(lastUpdateTime, first, pageSize);
        } else {
            return kbsKeywordDOMapper.selectKbsKeywordForUpdateSyncWithoutLastUpdateTime(first, pageSize);
        }
    }

    @Deprecated
    private void updateYhzxxnzzcyForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("YHZX_XNZZCY_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "YHZX_XNZZCY_UPDATE");
        }
        logger.info(MessageFormat.format("updateYhzxxnzzcyForSync 获得最后更新时间 : {0}", lastUpdateTime));
        Integer pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<YhzxxnzzcyDO> yhzxxnzzcyDOS = checkYhzxxnzzcyWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(yhzxxnzzcyDOS)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "YHZX_XNZZCY_UPDATE");
                }
                if (updateYhzxxnbzzcyAndCheckListSize(yhzxxnzzcyDOS, pageSize)) {
                    break;
                }
                logger.info("updateYhzxxnzzcyForSync loop times:" + pageIndex + ",list =" + yhzxxnzzcyDOS.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "YHZX_XNZZCY_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateYhzxxnzzcyForSync sleep error, {0}", ex));
        }
    }

    private List<YhzxxnzzcyDO> checkYhzxxnzzcyWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("YHZX_XNZZCY_UPDATE_STATUS"))) {
            return yhzxxnzzcyDOMapper.selectYhzxxnzzcyForUpdateSyncWithoutLastUpdateTime(first, pageSize);
        } else {
            return yhzxxnzzcyDOMapper.selectYhzxxnzzcyForUpdateSyncWithLastUpdateTime(lastUpdateTime, first, pageSize);
        }
    }

    /**
     * 同步BOT_UNAWARE_DETAIL 表的内容，更新同步的时候调用
     *
     * lastUpdateTime
     */
    private void updateBotUnawareDetailForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("BOT_UNAWARE_DETAIL_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "BOT_UNAWARE_DETAIL_UPDATE");
        }
        logger.info(MessageFormat.format("updateBotUnawareDetailForSync 获得最后更新时间 : {0}", lastUpdateTime));
        Integer pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<BotUnawareDetailDO> botUnawareDetailDOS = checkBotUnawareDetailWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(botUnawareDetailDOS)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "BOT_UNAWARE_DETAIL_UPDATE");
                }
                if (updateBotUnawareDetailAndCheckListSize(botUnawareDetailDOS, pageSize)) {
                    break;
                }
                logger.info("updateBotUnawareDetailForSync loop times:" + pageIndex + ",list =" + botUnawareDetailDOS.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "BOT_UNAWARE_DETAIL_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(String.format("updateBotUnawareDetailForSync sleep error, {0}", ex));
        }
    }

    private List<BotUnawareDetailDO> checkBotUnawareDetailWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("BOT_UNAWARE_DETAIL_UPDATE_STATUS"))) {
            return botUnawareDetailDOMapper.selectBotUnawareDetailWithLastUpdateTime(first, pageSize, lastUpdateTime);
        } else {
            return botUnawareDetailDOMapper.selectBotUnawareDetailWitOuthLastUpdateTime(first, pageSize);
        }
    }

    /**
     * 同步 BOT_MEDIA 表的内容, 更新同步的时候调用
     */
    public void updateBotMediaForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("BOT_MEDIA_UPDATE");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "BOT_MEDIA_UPDATE");
        }
        logger.info(MessageFormat.format("updateBotMediaForSync 获得最后更新时间:{0}", lastUpdateTime));
        Integer pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<BotMediaDO> botMediaDOS = checkBotMediaWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(botMediaDOS)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "BOT_MEDIA_UPDATE");
                }
                if (updateBotMediaAndCheckListSize(botMediaDOS, pageSize)) {
                    break;
                }
                logger.info("updateBotMediaForSync loop times:" + pageIndex + ",list =" + botMediaDOS.size());
                Thread.sleep(1000);
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "BOT_MEDIA_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateBotMediaForSync sleep error, {0}", ex));
        }
    }

    private List<BotMediaDO> checkBotMediaWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("BOT_MEDIA_UPDATE_STATUS"))) {
            return botMediaDOMapper.selectBotMediaWithLastUpdateTime(first, pageSize, lastUpdateTime);
        } else {
            return botMediaDOMapper.selectBotMediaWithOutLastUpdateTime(first, pageSize);
        }
    }

    public void updateBotConfigServerForSync() {
        String lastUpdateTime = CommonUtils.readlocalTimeFile("BOT_CONFIG_SERVER");
        //当读取不到时间的时候，默认存取当前时间，如果读取不到list的数据，那么该时间以后不会变化，只到读取list的数据才变化
        if (StringUtils.isBlank(lastUpdateTime)) {
            CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "BOT_CONFIG_SERVER");
        }
        logger.info(MessageFormat.format("updateBotConfigServerForSync 获得最后更新时间:{0}", lastUpdateTime));
        Integer pageIndex = 1;
        try {
            while (true) {
                Integer first = (pageIndex - 1) * pageSize;
                List<BotConfigServer> botConfigServers = checkBotConfigServerWithLastUpdateTime(lastUpdateTime, first);
                if (!CollectionUtils.isEmpty(botConfigServers)) {
                    CommonUtils.writeLocalTimeFile(compriseUtils.transportData(new Date().toString()), "BOT_CONFIG_SERVER_UPDATE");
                }
                if (updateBotConfigServerAndCheckListSize(botConfigServers, pageSize)) {
                    break;
                }
                logger.info("updateBotConfigServerForSync loop times:" + pageIndex + ",list =" + botConfigServers.size());
                pageIndex++;
            }
            CommonUtils.writeLocalTimeFile("1", "BOT_CONFIG_SERVER_UPDATE_STATUS");
        } catch (Exception ex) {
            logger.error(MessageFormat.format("updateBotConfigServerForSync sleep error , {0}", ex.getMessage()), ex);
        }
    }

    private List<BotConfigServer> checkBotConfigServerWithLastUpdateTime(String lastUpdateTime, Integer first) {
        lastUpdateTime = compriseUtils.transportData(lastUpdateTime);
        if (StringUtils.isNotBlank(lastUpdateTime) && StringUtils.isNotBlank(CommonUtils.readlocalTimeFile("BOT_CONFIG_SERVER_UPDATE_STATUS"))) {
            return botConfigServerMapper.selectBotConfigServerWithLastUpdateTime(first, pageSize, lastUpdateTime);
        } else {
            return botConfigServerMapper.selectBotConfigServerWithOutLastUpdateTime(first, pageSize);
        }
    }

    /***
     *  insert ######################################################################################################################################
     *
     */

    /**
     * @auther: Liuzhengyang
     * 插入BotUnawareDetail表的内容,数据增量同步时调用
     */
    private void insertBotUnawareDetailForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_UNAWARE_DETAIL");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("BotUnawareDetail last createTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<BotUnawareDetailDO> botUnawareDetailDOS = botUnawareDetailDOMapper.selectBotUnawareDetailWithLastUpdateTime(first, pageSize, lastCreateTime);
        if (!CollectionUtils.isEmpty(botUnawareDetailDOS)) {
            syncService.insertBotUnawareDetailAndCheckListSize(botUnawareDetailDOS, pageSize);
            Date modificationDate = botUnawareDetailDOS.get(botUnawareDetailDOS.size() - 1).getModificationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkBotUnawareDetailTimeEquals(botUnawareDetailDOS)) {
                modificationDate = TimeDelayUtils.getNextMilliDate(modificationDate);
            }
            CommonUtils.writeLocalTimeFile(modificationDate.toString(), "BOT_UNAWARE_DETAIL");
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入KbsQuestionArticle表的内容,数据增量同步插入时调用
     */
    private void insertKbsQuestionArticleForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_QUESTION_ARTICLE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("KbsQuestionArticle last createTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<KbsQuestionArticleDO> kbsQuestionArticleDOS = kbsQuestionArticleDOMapper.selectKbsQuestionArticleForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(kbsQuestionArticleDOS)) {
            syncService.insertKbsQuestionArticleAndCheckListSize(kbsQuestionArticleDOS, pageSize);
            Date modificationDate = kbsQuestionArticleDOS.get(kbsQuestionArticleDOS.size() - 1).getModificationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkKbsQuestionArticleTimeEquals(kbsQuestionArticleDOS)) {
                modificationDate = TimeDelayUtils.getNextMilliDate(modificationDate);
            }
            CommonUtils.writeLocalTimeFile(modificationDate.toString(), "KBS_QUESTION_ARTICLE");
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入BotTrackDetail表的内容,数据增量同步插入时调用
     */
    private void insertBotTrackDetailForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_TRACK_DETAIL");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("BotTrackDetail lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<BotTrackDetailDO> botTrackDetailDOS = botTrackDetailDOMapper.selectBotTrackDetailForSync(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(botTrackDetailDOS)) {
            syncService.insertBotTrackDetailAndCheckListSize(botTrackDetailDOS, pageSize);
            Date createDate = botTrackDetailDOS.get(botTrackDetailDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkBotTrackDetailTimeEquals(botTrackDetailDOS)) {
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "BOT_TRACK_DETAIL");
        }

    }

    /**
     * @auther: Liuzhengyang
     * 插入BotChatResponseMessage表的内容，数据增量同步插入时调用
     */
    private void insertBotChatResponseMessageForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_CHAT_RESPONSE_MESSAGE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("BotChatResponseMessage lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<BotChatResponseMessageDO> botChatResponseMessageDOS = botChatResponseMessageDOMapper.selectBotChatResponseMessageForSync(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(botChatResponseMessageDOS)) {
            syncService.insertBotChatResponseMessageAndCheckListSize(botChatResponseMessageDOS, pageSize);
            Date createDate = botChatResponseMessageDOS.get(botChatResponseMessageDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkBotChatResponseMessageTimeEquals(botChatResponseMessageDOS)) {
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "BOT_CHAT_RESPONSE_MESSAGE");
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入KbsQuestion表的内容,数据同步增量导入时调用
     */
    private void insertKbsQuestionForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_QUESTION");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("KbsQuestion lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<KbsQuestionDO> kbsQuestionDOS = kbsQuestionDOMapper.selectKbsQuestionForUpdateWithLastUpdateTime(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(kbsQuestionDOS)) {
            syncService.insertKbsQuestionAndCheckListSize(kbsQuestionDOS, pageSize);
            Date modificationDate = kbsQuestionDOS.get(kbsQuestionDOS.size() - 1).getModificationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkQuestionTimeEquals(kbsQuestionDOS)) {
                modificationDate = TimeDelayUtils.getNextMilliDate(modificationDate);
            }
            CommonUtils.writeLocalTimeFile(modificationDate.toString(), "KBS_QUESTION");
        }
    }

    private void insertBotBizDataForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_BIZ_DATA");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("BOT_BIZ_DATA lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<BizData> bizDataList = bizDataMapper.selectBotBizDataForUpdateWithLastUpdateTime(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(bizDataList)) {
            syncService.insertBotBizDataAndCheckListSize(bizDataList, pageSize);
            Date modificationDate = bizDataList.get(bizDataList.size() - 1).getModificationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkBizDataTimeEquals(bizDataList)) {
                modificationDate = TimeDelayUtils.getNextMilliDate(modificationDate);
            }
            CommonUtils.writeLocalTimeFile(modificationDate.toString(), "BOT_BIZ_DATA");
        }
    }

    /**
     * 插入 BOT_CONFIG_SERVER 表的内容, 数据同步增量导入时调用
     */
    private void insertBotConfigServerForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_CONFIG_SERVER");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info("BOT_CONFIG_SERVER lastCreateTIme : {}", lastCreateTime);
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<BotConfigServer> botConfigServers = botConfigServerMapper.selectBotConfigServerForSync(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(botConfigServers)) {
            syncService.insertBotConfigServerAndCheckListSize(botConfigServers, pageSize);
            Date createDate = botConfigServers.get(botConfigServers.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkBotConfigServerTimeEquals(botConfigServers)) {
                createDate = TimeDelayUtils.getNextMilliDate(createDate);
            }
            CommonUtils.writeLocalTimeFile(createDate.toString(), "BOT_CONFIG_SERVER");
        }
    }

    /***
     * 插入zx_article表,数据同步增量导入时调用
     */
    private void insertZxArticleForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("ZX_ARTICLE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("zxArticle lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<ZxArticle> zxArticles = zxArticleMapper.selectZxArticleForSync(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(zxArticles)) {
            syncService.insertZxArticleAndCheckListSize(zxArticles, pageSize);
            Date createDate = zxArticles.get(zxArticles.size() - 1).getCreationDate();
            CommonUtils.writeLocalTimeFile(createDate.toString(), "ZX_ARTICLE");
        }
    }

    /***
     * 插入yhzx_xnzz_nsr表,数据同步增量导入时调用
     */
    private void insertYhzxXnzzNsrForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("YHZX_XNZZ_NSR");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("YhzxXnzzNsr lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<YhzxXnzzNsr> yhzxXnzzNsrs = yhzxXnzzNsrMapper.selectYhzxXnzzNsrForSync(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(yhzxXnzzNsrs)) {
            syncService.insertYhzxXnzzNsrAndCheckListSize(yhzxXnzzNsrs, pageSize);
            Date createDate = yhzxXnzzNsrs.get(yhzxXnzzNsrs.size() - 1).getCjsj();
            CommonUtils.writeLocalTimeFile(createDate.toString(), "YHZX_XNZZ_NSR");
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入KbsArticle表的内容,数据同步增量导入时调用
     */
    private void insertKbsArticleForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_ARTICLE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        int pageIndex = 1;
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("KbsArticle lastCreateTime : {0}", lastCreateTime));
        Integer first = (pageIndex - 1) * pageSize;
        List<KbsArticleDOWithBLOBs> kbsArticleDOS = kbsArticleDOMapper.selectKbsArticleForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(kbsArticleDOS)) {
            syncService.insertKbsArticleAndCheckListSize(kbsArticleDOS, pageSize);
            Date modificationDate = kbsArticleDOS.get(kbsArticleDOS.size() - 1).getModificationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkArticleTimeEquals(kbsArticleDOS)) {
                modificationDate = TimeDelayUtils.getNextMilliDate(modificationDate);
            }
            CommonUtils.writeLocalTimeFile(modificationDate.toString(), "KBS_ARTICLE");
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入KbsReading表的内容,数据同步增量导入时调用
     */
    private void insertKbsReadingForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_READING");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("KbsReading lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<KbsReadingDOWithBLOBs> kbsReadingDOS = kbsReadingDOMapper.selectKbsReadingForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(kbsReadingDOS)) {
            syncService.insertKbsReadingAndCheckListSize(kbsReadingDOS, pageSize);
            Date modificationDate = kbsReadingDOS.get(kbsReadingDOS.size() - 1).getModificationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkKbsReadingTimeEquals(kbsReadingDOS)) {
                modificationDate = TimeDelayUtils.getNextMilliDate(modificationDate);
            }
            CommonUtils.writeLocalTimeFile(modificationDate.toString(), "KBS_READING");
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入 KbsKeyword 表的内容,数据同步增量导入时调用
     */
    private void insertKbsKeywordForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_KEYWORD");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("KbsKeyword lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<KbsKeywordDO> kbsKeywordDOS = kbsKeywordDOMapper.selectKbsKeywordForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(kbsKeywordDOS)) {
            syncService.insertKbsKeywordAndCheckListSize(kbsKeywordDOS, pageSize);
            Date modificationDate = kbsKeywordDOS.get(kbsKeywordDOS.size() - 1).getModificationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkKbsKeywordTimeEquals(kbsKeywordDOS)) {
                modificationDate = TimeDelayUtils.getNextMilliDate(modificationDate);
            }
            CommonUtils.writeLocalTimeFile(modificationDate.toString(), "KBS_KEYWORD");
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入Yhzxxnzzcy 表的内容，数据增量同步时调用
     */
    private void insertYhzxxnzzcyForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("yhzx_xnzz_cy");
        if (StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("yhzx_xnzz_cy lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<YhzxxnzzcyDO> yhzxxnzzcyDOs = yhzxxnzzcyDOMapper.selectYhzxxnzzcyForSync(lastCreateTime, first, pageSize);
        if (!CollectionUtils.isEmpty(yhzxxnzzcyDOs)) {
            syncService.insertYhzxxnzzcyAndCheckListSize(yhzxxnzzcyDOs, pageSize);
            Date createDate = yhzxxnzzcyDOs.get(yhzxxnzzcyDOs.size() - 1).getCjsj();
            CommonUtils.writeLocalTimeFile(createDate.toString(), "yhzx_xnzz_cy");
        }
    }

    private void insertBotMediaForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_MEDIA");
        if (!StringUtils.isEmpty(lastCreateTime)) {
            return;
        }
        lastCreateTime = compriseUtils.transportData(lastCreateTime);
        logger.info(MessageFormat.format("BotMedia lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        Integer first = (pageIndex - 1) * pageSize;
        List<BotMediaDO> botMediaDOS = botMediaDOMapper.selectBotMediaWithLastUpdateTime(first, pageSize, lastCreateTime);
        if (!CollectionUtils.isEmpty(botMediaDOS)) {
            syncService.insertBotMediaAndCheckListSize(botMediaDOS, pageSize);
            Date createTime = botMediaDOS.get(botMediaDOS.size() - 1).getCreationDate();
            //判断list的时间是否全部相同，若时间相同需要增加1s保存,修复存在相同列表的时间没有办法跳过的问题
            if (BizListCheckUtils.checkBotMediaTimeEquals(botMediaDOS)) {
                createTime = TimeDelayUtils.getNextMilliDate(createTime);
            }
            CommonUtils.writeLocalTimeFile(createTime.toString(), "BOT_MEDIA");
        }
    }

    /**
     * 向ES中插入KbsQuestionArticle相关数据,并判断是否是最后一组List，如果是最后一组，返回true ,数据更新同步时使用
     *
     * @param kbsQuestionArticleDOS KBS_QUESTION_ARTICLE
     * @param pageSize pageSize
     */
    private boolean updateKbsQuestionArticleAndCheckListSize(List<KbsQuestionArticleDO> kbsQuestionArticleDOS, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (KbsQuestionArticleDO kbsQuestionArticleDO : kbsQuestionArticleDOS) {
            if (kbsQuestionArticleDO.isNew()) {
                DataRequest request = compriseUtils.kbsQuestionArticleCompriseDataRequest(kbsQuestionArticleDO);
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsQuestionArticleDOS.size() < pageSize;
    }

    /**
     * 向ES中更新KbsQuestion相关数据,并判断是否是最后一组List，如果是最后一组，返回true,更新同步的时候调用
     */
    public boolean updateKbsQuestionAndCheckListSize(List<KbsQuestionDO> kbsQuestionDOS, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (KbsQuestionDO kbsQuestionDO : kbsQuestionDOS) {
            if (kbsQuestionDO.isNew()) {
                DataRequest request = compriseUtils.kbsQuestionCompriseDataRequest(kbsQuestionDO);
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsQuestionDOS.size() < pageSize;
    }

    /**
     * 向ES中更新KbsQuestion相关数据,并判断是否是最后一组List，如果是最后一组，返回true,更新同步的时候调用
     */
    private boolean updateZxArticleAndCheckListSize(List<ZxArticle> zxArticles, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (ZxArticle zxArticle : zxArticles) {
            DataRequest request = CompriseUtils.zxArticleCompriseDataRequest(zxArticle);
            dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.ZHCS, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return zxArticles.size() < pageSize;
    }

    /**
     * 向ES中更新KbsQuestion相关数据,并判断是否是最后一组List，如果是最后一组，返回true,更新同步的时候调用
     */
    private boolean updateYhzxXnzzNsrAndCheckListSize(List<YhzxXnzzNsr> yhzxXnzzNsrs, Integer pageSize) {
        CryptSimple cryptSimple = new CryptSimple();
        CryptBase36 cryptBase36 = new CryptBase36();
        StringBuilder dsl = new StringBuilder(32);
        for (YhzxXnzzNsr yhzxXnzzNsr : yhzxXnzzNsrs) {
            DataRequest request = compriseUtils.yhzxXnzzNsrCompriseDataRequest(yhzxXnzzNsr, cryptSimple, cryptBase36);
            dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.ZHCS, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return yhzxXnzzNsrs.size() < pageSize;
    }

    /**
     * 向ES中插入KbsArticle相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     *
     * @param kbsArticleDOS KBS_ARTICLE
     * @param pageSize pageSize
     */
    private boolean updateKbsArticleAndCheckListSize(List<KbsArticleDOWithBLOBs> kbsArticleDOS, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (KbsArticleDOWithBLOBs kbsArticleDO : kbsArticleDOS) {
            if (kbsArticleDO.isNew()) {
                DataRequest request = compriseUtils.kbsArticleDOCompriseDataRequest(kbsArticleDO);
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsArticleDOS.size() < pageSize;
    }

    private boolean updateBotBizDataAndCheckListSize(List<BizData> bizDataList, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (BizData bizData : bizDataList) {
            if (bizData.isNew()) {
                DataRequest request = compriseUtils.botBizDataCompriseDataRequest(bizData);
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return bizDataList.size() < pageSize;
    }

    /**
     * 向ES中插入KbsReading相关数据,并判断是否是最后一组List，如果是最后一组，返回true ，用于更新同步
     */
    private boolean updateKbsReadingAndCheckListSize(List<KbsReadingDOWithBLOBs> kbsReadingDOS, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (KbsReadingDOWithBLOBs kbsReadingDO : kbsReadingDOS) {
            if (kbsReadingDO.isNew()) {
                DataRequest request = compriseUtils.kbsReadingCompriseDataRequest(kbsReadingDO);
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsReadingDOS.size() < pageSize;
    }

    /**
     * 向ES中更新kbsKeyword相关数据,并判断是否是最后一组List，如果是最后一组，返回true,在更新同步的时候调用
     */
    private boolean updateKbsKeywordAndCheckListSize(List<KbsKeywordDO> kbsKeywordDOS, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (KbsKeywordDO kbsKeywordDO : kbsKeywordDOS) {
            if (kbsKeywordDO.isNew()) {
                DataRequest request = compriseUtils.kbsKeywordCompriseDataRequest(kbsKeywordDO);
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsKeywordDOS.size() < pageSize;
    }

    /**
     * 向ES中更新Yhzxxnzzcy相关数据,并判断是否是最后一组List，如果是最后一组，返回true,在更新同步的时候调用
     */
    private boolean updateYhzxxnbzzcyAndCheckListSize(List<YhzxxnzzcyDO> yhzxxnzzcyDOS, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (YhzxxnzzcyDO yhzxxnzzcyDO : yhzxxnzzcyDOS) {
            DataRequest request = compriseUtils.yhzxxnzzcyCompriseDataRequest(yhzxxnzzcyDO);
            dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return yhzxxnzzcyDOS.size() < pageSize;
    }

    /**
     * 向ES中更新BotUnawareDetail相关数据,并判断是否是最后一组List，如果是最后一组，返回true,在更新同步的时候调用
     *
     * @param botUnawareDetailDOS botUnawareDetailDOS
     * @param pageSize pageSize
     */
    private boolean updateBotUnawareDetailAndCheckListSize(List<BotUnawareDetailDO> botUnawareDetailDOS, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (BotUnawareDetailDO botUnawareDetailDO : botUnawareDetailDOS) {
            if (botUnawareDetailDO.isNew()) {
                DataRequest request = compriseUtils.botUnawareDetailCompriseDataRequest(botUnawareDetailDO);
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botUnawareDetailDOS.size() < pageSize;
    }

    /**
     * 向ES中更新 BotMedia 相关数据,并判断是否是最后一组List, 如果是最后一组,返回true,在更新同步的时候调用
     *
     * @param botMediaDOS botMediaDOS
     * @param pageSize pageSize
     */
    private boolean updateBotMediaAndCheckListSize(List<BotMediaDO> botMediaDOS, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (BotMediaDO botMediaDO : botMediaDOS) {
            if (botMediaDO.isNew()) {
                DataRequest request = compriseUtils.botMediaCompriseDataRequest(botMediaDO);
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botMediaDOS.size() < pageSize;

    }

    /**
     * 向ES中更细 BotConfigSever 相关数据,并判断是否是最后一组List,如果是最后,返回true,在更新同步的时候调用
     *
     * @param botConfigServers botConfigServers
     * @param pageSize pageSize
     */
    private boolean updateBotConfigServerAndCheckListSize(List<BotConfigServer> botConfigServers, Integer pageSize) {
        StringBuilder dsl = new StringBuilder(32);
        for (BotConfigServer botConfigServer : botConfigServers) {
            DataRequest request = compriseUtils.botConfigServerCompriseDataRequest(botConfigServer);
            dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botConfigServers.size() < pageSize;
    }

}
