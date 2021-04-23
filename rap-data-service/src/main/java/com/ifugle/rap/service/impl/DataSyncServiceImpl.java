package com.ifugle.rap.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ifugle.rap.common.lang.util.DateUtils;
import com.ifugle.rap.mapper.BizDataMapper;
import com.ifugle.rap.mapper.BotChatRequestMapper;
import com.ifugle.rap.mapper.BotChatResponseMessageDOMapper;
import com.ifugle.rap.mapper.BotConfigServerMapper;
import com.ifugle.rap.mapper.BotMediaDOMapper;
import com.ifugle.rap.mapper.BotOutoundTaskDetailMapper;
import com.ifugle.rap.mapper.BotTrackDetailDOMapper;
import com.ifugle.rap.mapper.BotUnawareDetailDOMapper;
import com.ifugle.rap.mapper.KbsArticleDOMapper;
import com.ifugle.rap.mapper.KbsKeywordDOMapper;
import com.ifugle.rap.mapper.KbsQuestionArticleDOMapper;
import com.ifugle.rap.mapper.KbsReadingDOMapper;
import com.ifugle.rap.model.shuixiaomi.BizData;
import com.ifugle.rap.model.shuixiaomi.BotChatRequest;
import com.ifugle.rap.model.shuixiaomi.BotChatResponseMessageDO;
import com.ifugle.rap.model.shuixiaomi.BotConfigServer;
import com.ifugle.rap.model.shuixiaomi.BotMediaDO;
import com.ifugle.rap.model.shuixiaomi.BotOutoundTaskDetailWithBLOBs;
import com.ifugle.rap.model.shuixiaomi.BotTrackDetailDO;
import com.ifugle.rap.model.shuixiaomi.BotUnawareDetailDO;
import com.ifugle.rap.model.shuixiaomi.KbsArticleDOWithBLOBs;
import com.ifugle.rap.model.shuixiaomi.KbsKeywordDO;
import com.ifugle.rap.model.shuixiaomi.KbsQuestionArticleDO;
import com.ifugle.rap.model.shuixiaomi.KbsReadingDOWithBLOBs;
import com.ifugle.rap.service.DataSyncService;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.utils.CommonUtils;
import com.ifugle.util.DateUtil;

/**
 * @author LiuZhengyang
 * @version $Id$
 * @since 2018年10月16日 17:17
 */
@Service
public class DataSyncServiceImpl implements DataSyncService {

    @Value("${pageSize}")
    Integer pageSize = 1000;

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
    private KbsReadingDOMapper kbsReadingDOMapper;

    @Autowired
    private KbsKeywordDOMapper kbsKeywordDOMapper;

    @Autowired
    private BotMediaDOMapper botMediaDOMapper;

    @Autowired
    private BotConfigServerMapper botConfigServerMapper;

    @Autowired
    private SyncService syncService;

    @Autowired
    private BizDataMapper bizDataMapper;

    @Autowired
    private BotOutoundTaskDetailMapper botOutoundTaskDetailMapper;

    @Autowired
    private BotChatRequestMapper botChatRequestMapper;

    @Value("${env}")
    String env;

    private final static Logger logger = LoggerFactory.getLogger(DataSyncServiceImpl.class);

    /**
     * 查询各个表的最新创建时间，根据最新的创建时间作为基点，同步之后的数据，同步完成之后，将最后创建时间刷新
     */
    @Override
    public void dataSyncInsertIncrementData() {
        /***
         * 税小蜜同步操作
         */
        insertBotUnawareDetailForSync();
        insertBotTrackDetailForSync();
        insertBotChatResponseMessageForSync();
        insertKbsQuestionArticleForSync();
        insertKbsArticleForSync();
        insertKbsReadingForSync();
        insertKbsKeywordForSync();
        insertBotMediaForSync();
        insertBotBizDataForSync();  //特别注意存在加解密的问题，容易引起线程阻塞
        insertBotConfigServerForSync();
        insertBotOutoundTaskDetailForSync();
        insertBotChatRequestForSync();
    }

    private void insertBotUnawareDetailForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_UNAWARE_DETAIL");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotUnawareDetailForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat
                .format("#### [BOT_UNAWARE_DETAIL] 开始同步表 BotUnawareDetail 获取本地偏移时间 updateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotUnawareDetailDO> botUnawareDetailDOS = botUnawareDetailDOMapper
                    .selectBotUnawareDetailWithLastUpdateTime(first, pageSize, lastCreateTime);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(botUnawareDetailDOS)) {
                logger.info("[BOT_UNAWARE_DETAIL] 查询该表的列表的size，size=" + botUnawareDetailDOS.size());
                syncService.insertBotUnawareDetailAndCheckListSize(botUnawareDetailDOS, pageSize);
                modificationDate = botUnawareDetailDOS.get(botUnawareDetailDOS.size() - 1).getModificationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "BOT_UNAWARE_DETAIL");
                break;
            }
            // 满足2种情况：不仅处理最后一页的情况，也处理每次一条的情况
            if (botUnawareDetailDOS.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "BOT_UNAWARE_DETAIL");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [BOT_UNAWARE_DETAIL] 同步表数据单次结束");
    }

    private void insertKbsQuestionArticleForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_QUESTION_ARTICLE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertKbsQuestionArticleForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat
                .format("#### [KBS_QUESTION_ARTICLE] 开始同步表 KBS_QUESTION_ARTICLE 获取本地偏移时间 updateTime : {0}",
                        lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<KbsQuestionArticleDO> kbsQuestionArticleDOS = kbsQuestionArticleDOMapper
                    .selectKbsQuestionArticleForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(kbsQuestionArticleDOS)) {
                logger.info("[KBS_QUESTION_ARTICLE] 查询该表的列表的size，size=" + kbsQuestionArticleDOS.size());
                syncService.insertKbsQuestionArticleAndCheckListSize(kbsQuestionArticleDOS, pageSize);
                modificationDate = kbsQuestionArticleDOS.get(kbsQuestionArticleDOS.size() - 1).getModificationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "KBS_QUESTION_ARTICLE");
                break;
            }
            if (kbsQuestionArticleDOS.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "KBS_QUESTION_ARTICLE");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [KBS_QUESTION_ARTICLE] 同步表数据单次结束");
    }

    /**
     *
     */
    private void insertBotTrackDetailForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_TRACK_DETAIL");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotTrackDetailForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat
                .format("#### [BOT_TRACK_DETAIL] 开始同步表 BOT_TRACK_DETAIL 获取本地偏移时间 updateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotTrackDetailDO> botTrackDetailDOS = botTrackDetailDOMapper
                    .selectBotTrackDetailForSync(lastCreateTime, first, pageSize);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(botTrackDetailDOS)) {
                logger.info("[BOT_TRACK_DETAIL] 查询该表的列表的size，size=" + botTrackDetailDOS.size());
                syncService.insertBotTrackDetailAndCheckListSize(botTrackDetailDOS, pageSize);
                modificationDate = botTrackDetailDOS.get(botTrackDetailDOS.size() - 1).getCreationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "BOT_TRACK_DETAIL");
                break;
            }
            if (botTrackDetailDOS.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "BOT_TRACK_DETAIL");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [BOT_TRACK_DETAIL] 同步表数据单次结束");
    }

    /**
     *
     */
    private void insertBotChatResponseMessageForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_CHAT_RESPONSE_MESSAGE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotChatResponseMessageForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat
                .format("#### [BOT_CHAT_RESPONSE_MESSAGE] 开始同步表 BOT_CHAT_RESPONSE_MESSAGE 获取本地偏移时间 updateTime : {0}",
                        lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotChatResponseMessageDO> botChatResponseMessageDOS = botChatResponseMessageDOMapper
                    .selectBotChatResponseMessageForSync(lastCreateTime, first, pageSize);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(botChatResponseMessageDOS)) {
                logger.info("[BOT_CHAT_RESPONSE_MESSAGE] 查询该表的列表的size，size=" + botChatResponseMessageDOS.size());
                syncService.insertBotChatResponseMessageAndCheckListSize(botChatResponseMessageDOS, pageSize);
                modificationDate = botChatResponseMessageDOS.get(botChatResponseMessageDOS.size() - 1)
                        .getCreationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "BOT_CHAT_RESPONSE_MESSAGE");
                break;
            }
            if (botChatResponseMessageDOS.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "BOT_CHAT_RESPONSE_MESSAGE");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [BOT_CHAT_RESPONSE_MESSAGE] 同步表数据单次结束");
    }

    private void insertBotBizDataForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_BIZ_DATA");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotBizDataForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat
                .format("#### [BOT_BIZ_DATA] 开始同步表 BOT_BIZ_DATA 获取本地偏移时间 updateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BizData> bizDataList = bizDataMapper
                    .selectBotBizDataForUpdateWithLastUpdateTime(lastCreateTime, first, pageSize);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(bizDataList)) {
                logger.info("[BOT_BIZ_DATA] 查询该表的列表的size，size=" + bizDataList.size());
                syncService.insertBotBizDataAndCheckListSize(bizDataList, pageSize);
                modificationDate = bizDataList.get(bizDataList.size() - 1).getModificationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "BOT_BIZ_DATA");
                break;
            }
            if (bizDataList.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "BOT_BIZ_DATA");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [BOT_BIZ_DATA] 同步表数据单次结束");
    }

    /**
     * 插入 BOT_CONFIG_SERVER 表的内容, 数据同步增量导入时调用
     */
    private void insertBotConfigServerForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_CONFIG_SERVER");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotConfigServerForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat
                .format("#### [BOT_CONFIG_SERVER] 开始同步表 BOT_CONFIG_SERVER 获取本地偏移时间 updateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotConfigServer> botConfigServers = botConfigServerMapper
                    .selectBotConfigServerForSync(lastCreateTime, first, pageSize);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(botConfigServers)) {
                logger.info("[BOT_CONFIG_SERVER] 查询该表的列表的size，size=" + botConfigServers.size());
                syncService.insertBotConfigServerAndCheckListSize(botConfigServers, pageSize);
                modificationDate = botConfigServers.get(botConfigServers.size() - 1).getCreationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "BOT_CONFIG_SERVER");
                break;
            }
            if (botConfigServers.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "BOT_CONFIG_SERVER");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [BOT_CONFIG_SERVER] 同步表数据单次结束");
    }

    /***
     * 执行BotChatRequest表的同步
     */
    private void insertBotChatRequestForSync() {
        try {
            String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_CHAT_REQUEST");
            if (StringUtils.isEmpty(lastCreateTime)) {
                logger.info("insertBotChatRequestForSync lastCreateTime is null");
                return;
            }
            int pageIndex = 1;
            int customPageSize = 2000;
            while (true) {
                logger.info(MessageFormat
                        .format("#### [BOT_CHAT_REQUEST] 开始同步表 BOT_CHAT_REQUEST 获取本地偏移时间 updateTime : {0}",
                                lastCreateTime));
                Integer first = (pageIndex - 1) * customPageSize;
                List<BotChatRequest> botChatRequests = botChatRequestMapper
                        .selectBotChatRequestForSync(lastCreateTime, first, customPageSize);
                Date modificationDate;
                if (!CollectionUtils.isEmpty(botChatRequests)) {
                    logger.info("#### [BOT_CHAT_REQUEST] 查询该表的列表的size，size=" + botChatRequests.size());
                    syncService.insertBotChatRequestAndCheckListSize(botChatRequests, customPageSize);
                    modificationDate = botChatRequests.get(botChatRequests.size() - 1).getCreationDate();
                } else {
                    break;
                }
                if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                    CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                            "BOT_CHAT_REQUEST");
                    break;
                }
                if (botChatRequests.size() < pageSize) {
                    //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                    if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                        CommonUtils.writeLocalTimeFile(
                                DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                        DateUtil.ISO8601_DATEITME_LONG), "BOT_CHAT_REQUEST");
                    }
                    break;
                }
                pageIndex++;
            }
        } catch (Exception e) {
            logger.error("[data sync] deal", e);
        }
        logger.info("#### [BOT_CHAT_REQUEST] 同步表数据单次结束");
    }

    /**
     *
     */
    private void insertKbsArticleForSync() {
        int size = 300;
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_ARTICLE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertKbsArticleForSync lastCreateTime is null");
            return;
        }
        int pageIndex = 1;
        while (true) {
            logger.info(MessageFormat
                    .format("#### [KBS_ARTICLE] 开始同步表 KBS_ARTICLE 获取本地偏移时间 updateTime : {0}", lastCreateTime));
            Integer first = (pageIndex - 1) * size;
            List<KbsArticleDOWithBLOBs> kbsArticleDOS = kbsArticleDOMapper
                    .selectKbsArticleForUpdateSyncWithLastUpdateTime(lastCreateTime, first, size);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(kbsArticleDOS)) {
                logger.info("[KBS_ARTICLE] 查询该表的列表的size，size=" + kbsArticleDOS.size());
                syncService.insertKbsArticleAndCheckListSize(kbsArticleDOS, size);
                modificationDate = kbsArticleDOS.get(kbsArticleDOS.size() - 1).getModificationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "KBS_ARTICLE");
                break;
            }
            if (kbsArticleDOS.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "KBS_ARTICLE");
                }
                break;
            }

            pageIndex++;
        }
        logger.info("#### [KBS_ARTICLE] 同步表数据单次结束");
    }

    /**
     *
     */
    private void insertKbsReadingForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_READING");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertKbsReadingForSync lastCreateTime is null");
            return;
        }
        logger.info(
                MessageFormat.format("#### [KBS_READING] 开始同步表 KBS_READING 获取本地偏移时间 updateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<KbsReadingDOWithBLOBs> kbsReadingDOS = kbsReadingDOMapper
                    .selectKbsReadingForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(kbsReadingDOS)) {
                logger.info("[KBS_READING] 查询该表的列表的size，size=" + kbsReadingDOS.size());
                syncService.insertKbsReadingAndCheckListSize(kbsReadingDOS, pageSize);
                modificationDate = kbsReadingDOS.get(kbsReadingDOS.size() - 1).getModificationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "KBS_READING");
                break;
            }
            if (kbsReadingDOS.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "KBS_READING");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [KBS_READING] 同步表数据单次结束");
    }

    /**
     *
     */
    private void insertKbsKeywordForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_KEYWORD");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertKbsKeywordForSync lastCreateTime is null");
            return;
        }
        logger.info(
                MessageFormat.format("#### [KBS_KEYWORD] 开始同步表 KBS_KEYWORD 获取本地偏移时间 updateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<KbsKeywordDO> kbsKeywordDOS = kbsKeywordDOMapper
                    .selectKbsKeywordForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(kbsKeywordDOS)) {
                logger.info("[KBS_KEYWORD] 查询该表的列表的size，size=" + kbsKeywordDOS.size());
                syncService.insertKbsKeywordAndCheckListSize(kbsKeywordDOS, pageSize);
                modificationDate = kbsKeywordDOS.get(kbsKeywordDOS.size() - 1).getModificationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "KBS_KEYWORD");
                break;
            }
            if (kbsKeywordDOS.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "KBS_KEYWORD");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [KBS_KEYWORD] 同步表数据单次结束");
    }

    private void insertBotMediaForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_MEDIA");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotMediaForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("#### [BOT_MEDIA] 开始同步表 BOT_MEDIA 获取本地偏移时间 updateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotMediaDO> botMediaDOS = botMediaDOMapper
                    .selectBotMediaWithLastUpdateTime(first, pageSize, lastCreateTime);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(botMediaDOS)) {
                logger.info("[BOT_MEDIA] 查询该表的列表的size，size=" + botMediaDOS.size());
                syncService.insertBotMediaAndCheckListSize(botMediaDOS, pageSize);
                modificationDate = botMediaDOS.get(botMediaDOS.size() - 1).getModificationDate();
            } else {
                break;
            }
            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "BOT_MEDIA");
                break;
            }
            if (botMediaDOS.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "BOT_MEDIA");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [BOT_MEDIA] 同步表数据单次结束");
    }

    private void insertBotOutoundTaskDetailForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_OUTBOUND_TASK_DETAIL");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotOutoundTaskDetailForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat
                .format("#### [BOT_OUTBOUND_TASK_DETAIL] 开始同步表 BOT_OUTBOUND_TASK_DETAIL 获取本地偏移时间 updateTime : {0}",
                        lastCreateTime));
        int pageIndex = 1;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotOutoundTaskDetailWithBLOBs> botOutoundTaskDetailWithBLOBs = botOutoundTaskDetailMapper
                    .selectBotOutoundTaskDetailForSync(lastCreateTime, first, pageSize);
            Date modificationDate;
            if (!CollectionUtils.isEmpty(botOutoundTaskDetailWithBLOBs)) {
                logger.info("#### [BOT_OUTOUND_TASK_DETAIL] 查询该表的列表的size，size=" + botOutoundTaskDetailWithBLOBs.size());

                syncService.insertBotOutBoundTaskDetailAndCheckListSize(botOutoundTaskDetailWithBLOBs, pageSize);
                modificationDate = botOutoundTaskDetailWithBLOBs.get(botOutoundTaskDetailWithBLOBs.size() - 1)
                        .getModificationDate();
            } else {
                break;
            }

            if (modificationDate.compareTo(DateUtil.dateOf(lastCreateTime)) > 0) {
                CommonUtils.writeLocalTimeFile(DateUtil.format(modificationDate, DateUtil.ISO8601_DATEITME_LONG),
                        "BOT_OUTBOUND_TASK_DETAIL");
                break;
            }
            if (botOutoundTaskDetailWithBLOBs.size() < pageSize) {
                //修改时间已经同步的时+1分钟还有没到现在，说明1分钟之内没有修改
                if (DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "mi", 1).before(new Date())) {
                    CommonUtils.writeLocalTimeFile(
                            DateUtil.format(DateUtil.dateAdd(DateUtil.dateOf(lastCreateTime), "ss", 1),
                                    DateUtil.ISO8601_DATEITME_LONG), "BOT_OUTBOUND_TASK_DETAIL");
                }
                break;
            }
            pageIndex++;
        }
        logger.info("#### [BOT_OUTBOUND_TASK_DETAIL] 同步表数据单次结束");
    }

    @Override
    public void initLocalTime() {
        logger.info("init data localhost file start");
        if (!CommonUtils.isExistDir("BOT_BIZ_DATA")) {
            logger.info("开始写入本地时间:BOT_BIZ_DATA,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_BIZ_DATA");
        }
        if (!CommonUtils.isExistDir("BOT_CHAT_RESPONSE_MESSAGE")) {
            logger.info("开始写入本地时间:BOT_CHAT_RESPONSE_MESSAGE,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_CHAT_RESPONSE_MESSAGE");
        }
        if (!CommonUtils.isExistDir("BOT_CONFIG_SERVER")) {
            logger.info("开始写入本地时间:BOT_CONFIG_SERVER,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_CONFIG_SERVER");
        }
        if (!CommonUtils.isExistDir("BOT_MEDIA")) {
            logger.info("开始写入本地时间:BOT_MEDIA,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_MEDIA");
        }
        if (!CommonUtils.isExistDir("BOT_TAG")) {
            logger.info("开始写入本地时间:BOT_TAG,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_TAG");
        }
        if (!CommonUtils.isExistDir("BOT_TRACK_DETAIL")) {
            logger.info("开始写入本地时间:BOT_TRACK_DETAIL,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_TRACK_DETAIL");
        }
        if (!CommonUtils.isExistDir("BOT_UNAWARE_DETAIL")) {
            logger.info("开始写入本地时间:BOT_UNAWARE_DETAIL,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_UNAWARE_DETAIL");
        }
        if (!CommonUtils.isExistDir("KBS_ARTICLE")) {
            logger.info("开始写入本地时间:KBS_ARTICLE,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_ARTICLE");
        }
        if (!CommonUtils.isExistDir("KBS_KEYWORD")) {
            logger.info("开始写入本地时间:KBS_KEYWORD,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_KEYWORD");
        }
        if (!CommonUtils.isExistDir("KBS_QUESTION")) {
            logger.info("开始写入本地时间:KBS_QUESTION,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_QUESTION");
        }
        if (!CommonUtils.isExistDir("KBS_QUESTION_ARTICLE")) {
            logger.info("开始写入本地时间:KBS_QUESTION_ARTICLE,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_QUESTION_ARTICLE");
        }
        if (!CommonUtils.isExistDir("KBS_READING")) {
            logger.info("开始写入本地时间:KBS_READING,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_READING");
        }
        if (!CommonUtils.isExistDir("XXZX_XXMX")) {
            logger.info("开始写入本地时间:XXZX_XXMX,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "XXZX_XXMX");
        }

        if (!CommonUtils.isExistDir("BOT_CHAT_REQUEST")) {
            logger.info("开始写入本地时间:BOT_CHAT_REQUEST,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_CHAT_REQUEST");
        }

        if (!CommonUtils.isExistDir("BOT_OUTBOUND_TASK_DETAIL")) {
            logger.info("开始写入本地时间:BOT_OUTBOUND_TASK_DETAIL,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_OUTBOUND_TASK_DETAIL");
        }

        if (!CommonUtils.isExistDir("BOT_SCA_TASK_RESULT")) {
            logger.info("开始写入本地时间:BOT_SCA_TASK_RESULT,time=" + DateUtils.simpleFormat(new Date()));
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_SCA_TASK_RESULT");
        }
        logger.info("init data localhost file end");
    }

}
