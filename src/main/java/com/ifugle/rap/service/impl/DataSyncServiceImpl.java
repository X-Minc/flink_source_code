package com.ifugle.rap.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import com.ifugle.rap.mapper.*;
import com.ifugle.rap.mapper.dsb.XxzxXxmxMapper;
import com.ifugle.rap.model.dingtax.XxzxXxmx;
import com.ifugle.rap.model.shuixiaomi.*;
import org.apache.camel.spi.AsEndpointUri;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ifugle.rap.common.lang.util.DateUtils;
import com.ifugle.rap.constants.SystemConstants;
import com.ifugle.rap.mapper.dsb.YhzxXnzzNsrMapper;
import com.ifugle.rap.mapper.dsb.YhzxXnzzTpcQyMapper;
import com.ifugle.rap.mapper.zhcs.ZxArticleMapper;
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.dsb.YhzxXnzzTpcQy;
import com.ifugle.rap.model.zhcs.ZxArticle;
import com.ifugle.rap.service.DataSyncService;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.utils.BizListCheckUtils;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.service.utils.TimeDelayUtils;
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
    private ZxArticleMapper zxArticleMapper;

    @Autowired
    private YhzxXnzzNsrMapper yhzxXnzzNsrMapper;

    @Autowired
    private BotConfigServerMapper botConfigServerMapper;

    @Autowired
    private SyncService syncService;

    @Autowired
    private CompriseUtils compriseUtils;

    @Autowired
    private BizDataMapper bizDataMapper;

    @Autowired
    private BotOutoundTaskDetailMapper botOutoundTaskDetailMapper;

    @Autowired
    private YhzxXnzzTpcQyMapper yhzxXnzzTpcQyMapper;

    @Autowired
    private XxzxXxmxMapper xxzxXxmxMapper;

    @Autowired
    BotChatRequestMapper botChatRequestMapper;


    @Value("${profiles.active}")
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
        insertKbsQuestionForSync();
        insertKbsArticleForSync();
        insertKbsReadingForSync();
        insertKbsKeywordForSync();
        insertBotMediaForSync();
        insertBotBizDataForSync();  //特别注意存在加解密的问题，容易引起线程阻塞
        insertBotConfigServerForSync();
        insertBotOutoundTaskDetailForSync();
        insertBotChatRequestForSync();
        /***
         *  智慧财税导入
         */
        if (Boolean.valueOf(System.getProperty(SystemConstants.ZHCS_ON))) {
            insertZxArticleForSync();
        }
        /***
         *  丁税宝导入
         */
        if (Boolean.valueOf(System.getProperty(SystemConstants.DSB_ON))) {
            insertYhzxXnzzNsrForSync();
            insertYhzxXnzzTpcQyForSync();
            insertXxzxXxmxForSync();
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
            logger.info("insertBotUnawareDetailForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("BotUnawareDetail last createTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotUnawareDetailDO> botUnawareDetailDOS = botUnawareDetailDOMapper.selectBotUnawareDetailWithLastUpdateTime(first, pageSize, lastCreateTime);
            if (!CollectionUtils.isEmpty(botUnawareDetailDOS)) {
                syncService.insertBotUnawareDetailAndCheckListSize(botUnawareDetailDOS, pageSize);
                Date modificationDate = botUnawareDetailDOS.get(botUnawareDetailDOS.size() - 1).getModificationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modificationDate), "BOT_UNAWARE_DETAIL");
            }else {
                break;
            }
            pageIndex++;
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入KbsQuestionArticle表的内容,数据增量同步插入时调用
     */
    private void insertKbsQuestionArticleForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_QUESTION_ARTICLE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertKbsQuestionArticleForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("KbsQuestionArticle last createTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<KbsQuestionArticleDO> kbsQuestionArticleDOS = kbsQuestionArticleDOMapper
                    .selectKbsQuestionArticleForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(kbsQuestionArticleDOS)) {
                syncService.insertKbsQuestionArticleAndCheckListSize(kbsQuestionArticleDOS, pageSize);
                Date modificationDate = kbsQuestionArticleDOS.get(kbsQuestionArticleDOS.size() - 1).getModificationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modificationDate), "KBS_QUESTION_ARTICLE");
            }else {
                break;
            }
            pageIndex++;
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入BotTrackDetail表的内容,数据增量同步插入时调用
     */
    private void insertBotTrackDetailForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_TRACK_DETAIL");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotTrackDetailForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("BotTrackDetail lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotTrackDetailDO> botTrackDetailDOS = botTrackDetailDOMapper.selectBotTrackDetailForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(botTrackDetailDOS)) {
                syncService.insertBotTrackDetailAndCheckListSize(botTrackDetailDOS, pageSize);
                Date createDate = botTrackDetailDOS.get(botTrackDetailDOS.size() - 1).getCreationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(createDate), "BOT_TRACK_DETAIL");
            } else {
                break;
            }
            pageIndex++;
        }

    }

    /**
     * @auther: Liuzhengyang
     * 插入BotChatResponseMessage表的内容，数据增量同步插入时调用
     */
    private void insertBotChatResponseMessageForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_CHAT_RESPONSE_MESSAGE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotChatResponseMessageForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("BotChatResponseMessage lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotChatResponseMessageDO> botChatResponseMessageDOS = botChatResponseMessageDOMapper
                    .selectBotChatResponseMessageForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(botChatResponseMessageDOS)) {
                syncService.insertBotChatResponseMessageAndCheckListSize(botChatResponseMessageDOS, pageSize);
                Date createDate = botChatResponseMessageDOS.get(botChatResponseMessageDOS.size() - 1).getCreationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(createDate), "BOT_CHAT_RESPONSE_MESSAGE");
            }else{
                break;
            }
            pageIndex++;
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入KbsQuestion表的内容,数据同步增量导入时调用
     */
    private void insertKbsQuestionForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_QUESTION");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertKbsQuestionForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("KbsQuestion lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<KbsQuestionDO> kbsQuestionDOS = kbsQuestionDOMapper.selectKbsQuestionForUpdateWithLastUpdateTime(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(kbsQuestionDOS)) {
                syncService.insertKbsQuestionAndCheckListSize(kbsQuestionDOS, pageSize);
                Date modificationDate = kbsQuestionDOS.get(kbsQuestionDOS.size() - 1).getModificationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modificationDate), "KBS_QUESTION");
            }else{
                break;
            }
            pageIndex++;
        }
    }

    private void insertBotBizDataForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_BIZ_DATA");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotBizDataForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("BOT_BIZ_DATA lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BizData> bizDataList = bizDataMapper.selectBotBizDataForUpdateWithLastUpdateTime(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(bizDataList)) {
                syncService.insertBotBizDataAndCheckListSize(bizDataList, pageSize);
                Date modificationDate = bizDataList.get(bizDataList.size() - 1).getModificationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modificationDate), "BOT_BIZ_DATA");
            }else{
                break;
            }
            pageIndex++;
        }
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
        logger.info("BOT_CONFIG_SERVER lastCreateTIme : {}", lastCreateTime);
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotConfigServer> botConfigServers = botConfigServerMapper.selectBotConfigServerForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(botConfigServers)) {
                syncService.insertBotConfigServerAndCheckListSize(botConfigServers, pageSize);
                Date createDate = botConfigServers.get(botConfigServers.size() - 1).getCreationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(createDate), "BOT_CONFIG_SERVER");
            }else{
                break;
            }
            pageIndex++;
        }
    }

    /***
     * 插入zx_article表,数据同步增量导入时调用
     */
    private void insertZxArticleForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("ZX_ARTICLE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertZxArticleForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("zxArticle lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<ZxArticle> zxArticles = zxArticleMapper.selectZxArticleForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(zxArticles)) {
                syncService.insertZxArticleAndCheckListSize(zxArticles, pageSize);
                Date createDate = zxArticles.get(zxArticles.size() - 1).getCreationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(createDate), "ZX_ARTICLE");
            }else{
                break;
            }
            pageIndex++;
        }
    }

    /***
     * 插入yhzx_xnzz_nsr表,数据同步增量导入时调用
     */
    private void insertYhzxXnzzNsrForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("YHZX_XNZZ_NSR");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertYhzxXnzzNsrForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("YhzxXnzzNsr lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<YhzxXnzzNsr> yhzxXnzzNsrs = yhzxXnzzNsrMapper.selectYhzxXnzzNsrForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(yhzxXnzzNsrs)) {
                syncService.insertYhzxXnzzNsrAndCheckListSize(yhzxXnzzNsrs, pageSize);
                Date modifyDate = yhzxXnzzNsrs.get(yhzxXnzzNsrs.size() - 1).getXgsj();
                //注意该本地时间一定要在break之前写入。
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modifyDate), "YHZX_XNZZ_NSR");
                /***
                 * 该逻辑是处理大范围修改时间是相同值的情况，减少循环offset的偏移量，start
                 */
                Date startDate = DateUtils.string2Date(lastCreateTime,DateUtils.simple);
                if (modifyDate.compareTo(startDate) > 0 || yhzxXnzzNsrs.size() < pageSize) {
                    break;
                }
                //end
            }else{
                break;
            }
            pageIndex++;
        }
    }


    /**
     * YhzxXnzzTpcQy,数据同步增量导入时调用
     */
    private void insertYhzxXnzzTpcQyForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("YHZX_XNZZ_TPC_QY");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertYhzxXnzzTpcQyForSync lastCreateTime is null");
            return;
        }
        int pageIndex = 1;
        while(true) {
            logger.info(MessageFormat.format("YHZX_XNZZ_TPC_QY lastCreateTime : {0}", lastCreateTime));
            Integer first = (pageIndex - 1) * pageSize;
            List<YhzxXnzzTpcQy> yhzxXnzzTpcQyList = yhzxXnzzTpcQyMapper.selectYhzxXnzzTpcQyForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(yhzxXnzzTpcQyList)) {
                syncService.insertYhzxXnzzTpcQyAndCheckListSize(yhzxXnzzTpcQyList, pageSize);
                Date modificationDate = yhzxXnzzTpcQyList.get(yhzxXnzzTpcQyList.size() - 1).getXgsj();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modificationDate), "YHZX_XNZZ_TPC_QY");
                /***
                 * 该逻辑是处理大范围修改时间是相同值的情况，减少循环offset的偏移量，start
                 */
                Date startDate = DateUtils.string2Date(lastCreateTime,DateUtils.simple);
                if (modificationDate.compareTo(startDate) > 0 || yhzxXnzzTpcQyList.size() < pageSize) {
                    break;
                }
            }else{
                break;
            }
            pageIndex++;
        }
    }


    /***
     * 执行BotChatRequest表的同步
     */
    private void insertBotChatRequestForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_CHAT_REQUEST");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotChatRequestForSync lastCreateTime is null");
            return;
        }
        int pageIndex = 1;
        while(true) {
            logger.info(MessageFormat.format("BOT_CHAT_REQUEST lastCreateTime : {0}", lastCreateTime));
            Integer first = (pageIndex - 1) * pageSize;
            List<BotChatRequest> botChatRequests = botChatRequestMapper.selectBotChatRequestForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(botChatRequests)) {
                syncService.insertBotChatRequestAndCheckListSize(botChatRequests, pageSize);
                Date creationDate = botChatRequests.get(botChatRequests.size() - 1).getCreationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(creationDate), "BOT_CHAT_REQUEST");
                /***
                 * 该逻辑是处理大范围修改时间是相同值的情况，减少循环offset的偏移量，start
                 */
                Date startDate = DateUtils.string2Date(lastCreateTime,DateUtils.simple);
                if (creationDate.compareTo(startDate) > 0 || botChatRequests.size() < pageSize) {
                    break;
                }
            }else{
                break;
            }
            pageIndex++;
        }
    }

    /***
     * 同步消息明细
     */
    private void insertXxzxXxmxForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("XXZX_XXMX");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertXxzxXxmxForSync lastCreateTime is null");
            return;
        }
        int pageIndex = 1;
        while(true) {
            logger.info(MessageFormat.format("XXZX_XXMX lastCreateTime : {0}", lastCreateTime));
            Integer first = (pageIndex - 1) * pageSize;
            List<XxzxXxmx> xxzxXxmxs = xxzxXxmxMapper.selectXxzxXxmxForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(xxzxXxmxs)) {
                syncService.insertXxzxXxmxAndCheckListSize(xxzxXxmxs, pageSize);
                Date modificationDate = xxzxXxmxs.get(xxzxXxmxs.size() - 1).getXgsj();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modificationDate), "XXZX_XXMX");
                /***
                 * 该逻辑是处理大范围修改时间是相同值的情况，减少循环offset的偏移量，start
                 */
                Date startDate = DateUtils.string2Date(lastCreateTime,DateUtils.simple);
                if (modificationDate.compareTo(startDate) > 0 || xxzxXxmxs.size() < pageSize) {
                    break;
                }
            }else{
                break;
            }
            pageIndex++;
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入KbsArticle表的内容,数据同步增量导入时调用
     */
    private void insertKbsArticleForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_ARTICLE");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertKbsArticleForSync lastCreateTime is null");
            return;
        }
        int pageIndex = 1;
        while(true) {
            logger.info(MessageFormat.format("KbsArticle lastCreateTime : {0}", lastCreateTime));
            Integer first = (pageIndex - 1) * pageSize;
            List<KbsArticleDOWithBLOBs> kbsArticleDOS = kbsArticleDOMapper.selectKbsArticleForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(kbsArticleDOS)) {
                syncService.insertKbsArticleAndCheckListSize(kbsArticleDOS, pageSize);
                Date modificationDate = kbsArticleDOS.get(kbsArticleDOS.size() - 1).getModificationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modificationDate), "KBS_ARTICLE");
            }else{
                break;
            }
            pageIndex++;
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入KbsReading表的内容,数据同步增量导入时调用
     */
    private void insertKbsReadingForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_READING");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertKbsReadingForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("KbsReading lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<KbsReadingDOWithBLOBs> kbsReadingDOS = kbsReadingDOMapper.selectKbsReadingForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(kbsReadingDOS)) {
                syncService.insertKbsReadingAndCheckListSize(kbsReadingDOS, pageSize);
                Date modificationDate = kbsReadingDOS.get(kbsReadingDOS.size() - 1).getModificationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modificationDate), "KBS_READING");
            } else {
                break;
            }
            pageIndex++;
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入 KbsKeyword 表的内容,数据同步增量导入时调用
     */
    private void insertKbsKeywordForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("KBS_KEYWORD");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertKbsKeywordForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("KbsKeyword lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<KbsKeywordDO> kbsKeywordDOS = kbsKeywordDOMapper.selectKbsKeywordForUpdateSyncWithLastUpdateTime(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(kbsKeywordDOS)) {
                syncService.insertKbsKeywordAndCheckListSize(kbsKeywordDOS, pageSize);
                Date modificationDate = kbsKeywordDOS.get(kbsKeywordDOS.size() - 1).getModificationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(modificationDate), "KBS_KEYWORD");
            }else {
                break;
            }
            pageIndex++;
        }
    }

    /**
     * @auther: Liuzhengyang
     * 插入Yhzxxnzzcy 表的内容，数据增量同步时调用
     */
    private void insertYhzxxnzzcyForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("yhzx_xnzz_cy");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertYhzxxnzzcyForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("yhzx_xnzz_cy lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<YhzxxnzzcyDO> yhzxxnzzcyDOs = yhzxxnzzcyDOMapper.selectYhzxxnzzcyForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(yhzxxnzzcyDOs)) {
                syncService.insertYhzxxnzzcyAndCheckListSize(yhzxxnzzcyDOs, pageSize);
                Date createDate = yhzxxnzzcyDOs.get(yhzxxnzzcyDOs.size() - 1).getCjsj();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(createDate), "yhzx_xnzz_cy");
            }else {
                break;
            }
            pageIndex++;
        }
    }

    private void insertBotMediaForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_MEDIA");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotMediaForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("BotMedia lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotMediaDO> botMediaDOS = botMediaDOMapper.selectBotMediaWithLastUpdateTime(first, pageSize, lastCreateTime);
            if (!CollectionUtils.isEmpty(botMediaDOS)) {
                syncService.insertBotMediaAndCheckListSize(botMediaDOS, pageSize);
                Date createTime = botMediaDOS.get(botMediaDOS.size() - 1).getCreationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(createTime), "BOT_MEDIA");
            }else {
                break;
            }
            pageIndex++;
        }
    }

    private void insertBotOutoundTaskDetailForSync() {
        String lastCreateTime = CommonUtils.readlocalTimeFile("BOT_OUTBOUND_TASK_DETAIL");
        if (StringUtils.isEmpty(lastCreateTime)) {
            logger.info("insertBotOutoundTaskDetailForSync lastCreateTime is null");
            return;
        }
        logger.info(MessageFormat.format("BotMedia lastCreateTime : {0}", lastCreateTime));
        int pageIndex = 1;
        while(true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<BotOutoundTaskDetailWithBLOBs> botOutoundTaskDetailWithBLOBs = botOutoundTaskDetailMapper
                    .selectBotOutoundTaskDetailForSync(lastCreateTime, first, pageSize);
            if (!CollectionUtils.isEmpty(botOutoundTaskDetailWithBLOBs)) {
                syncService.insertBotOutBoundTaskDetailAndCheckListSize(botOutoundTaskDetailWithBLOBs, pageSize);
                Date createTime = botOutoundTaskDetailWithBLOBs.get(botOutoundTaskDetailWithBLOBs.size() - 1).getCreationDate();
                CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(createTime), "BOT_OUTBOUND_TASK_DETAIL");
            }else {
                break;
            }
            pageIndex++;
        }
    }

    @Override
    public void initLocalTime() {
        logger.info("init data localhost file start");
        if(!CommonUtils.isExistDir("BOT_BIZ_DATA")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_BIZ_DATA");
        }
        if (!CommonUtils.isExistDir("BOT_CHAT_RESPONSE_MESSAGE")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_CHAT_RESPONSE_MESSAGE");
        }
        if (!CommonUtils.isExistDir("BOT_CONFIG_SERVER")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_CONFIG_SERVER");
        }
        if (!CommonUtils.isExistDir("BOT_MEDIA")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_MEDIA");
        }
        if (!CommonUtils.isExistDir("BOT_TAG")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_TAG");
        }
        if (!CommonUtils.isExistDir("BOT_TRACK_DETAIL")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_TRACK_DETAIL");
        }
        if (!CommonUtils.isExistDir("BOT_UNAWARE_DETAIL")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_UNAWARE_DETAIL");
        }
        if (!CommonUtils.isExistDir("KBS_ARTICLE")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_ARTICLE");
        }
        if (!CommonUtils.isExistDir("KBS_KEYWORD")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_KEYWORD");
        }
        if (!CommonUtils.isExistDir("KBS_QUESTION")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_QUESTION");
        }
        if (!CommonUtils.isExistDir("KBS_QUESTION_ARTICLE")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_QUESTION_ARTICLE");
        }
        if (!CommonUtils.isExistDir("KBS_READING")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "KBS_READING");
        }
        if (!CommonUtils.isExistDir("YHZX_XNZZ_NSR")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "YHZX_XNZZ_NSR");
        }
        if (!CommonUtils.isExistDir("BOT_OUTBOUND_TASK_DETAIL")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_OUTBOUND_TASK_DETAIL");
        }
        if (!CommonUtils.isExistDir("YHZX_XNZZ_TPC_QY")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "YHZX_XNZZ_TPC_QY");
        }
        if (!CommonUtils.isExistDir("XXZX_XXMX")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "XXZX_XXMX");
        }

        if (!CommonUtils.isExistDir("BOT_CHAT_REQUEST")) {
            CommonUtils.writeLocalTimeFile(DateUtils.simpleFormat(new Date()), "BOT_CHAT_REQUEST");
        }

        logger.info("init data localhost file end");
    }

    public static void main(String[] args) throws  Exception{
        Date startDate = DateUtils.strToDtSimpleFormat("Fri Aug 02 14:37:41 CST 2019");
        System.out.println(startDate);
    }
}
