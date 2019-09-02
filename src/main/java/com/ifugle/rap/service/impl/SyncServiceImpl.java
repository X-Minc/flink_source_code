/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.dsb.YhzxXnzzTpcQy;
import com.ifugle.rap.model.shuixiaomi.BizData;
import com.ifugle.rap.model.shuixiaomi.BotChatResponseMessageDO;
import com.ifugle.rap.model.shuixiaomi.BotConfigServer;
import com.ifugle.rap.model.shuixiaomi.BotMediaDO;
import com.ifugle.rap.model.shuixiaomi.BotOutoundTaskDetail;
import com.ifugle.rap.model.shuixiaomi.BotOutoundTaskDetailWithBLOBs;
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
import com.ifugle.rap.security.crypto.CryptNumber;
import com.ifugle.rap.security.crypto.CryptSimple;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.redis.ParseConstant;
import com.ifugle.rap.service.redis.RedisMessageSubscriber;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.utils.DecodeUtils;

/**
 * @author LiuZhengyang(zhengyang)
 */
@Component
public class SyncServiceImpl implements SyncService {

    private static final Logger logger = LoggerFactory.getLogger(SyncServiceImpl.class);

    @Autowired
    private ElasticSearchBusinessService elasticSearchBusinessService;

    @Autowired
    private CompriseUtils compriseUtils;

    @Autowired
    RedisMessageSubscriber redisMessageSubscriber;

    @Value("${profiles.active}")
    String env;

    /**
     * 批量插入BOT_UNAWARE_DETAIL
     *
     * @param botUnawareDetailDOS
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertBotUnawareDetailAndCheckListSize(List<BotUnawareDetailDO> botUnawareDetailDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_UNAWARE_DETAIL to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        for (BotUnawareDetailDO botUnawareDetailDO : botUnawareDetailDOS) {
            logger.debug(
                    MessageFormat.format("[DataSyncServiceImpl] insertBotUnawareDetailAndCheckListSize botUnawareDetailDO {0}", botUnawareDetailDO.toString()));
            DataRequest request = compriseUtils.botUnawareDetailCompriseDataRequest(botUnawareDetailDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botUnawareDetailDOS.size() < pageSize;
    }

    /**
     * 批量插入YhzxxnzzcyDO
     *
     * @param yhzxxnzzcyDOs
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertYhzxxnzzcyAndCheckListSize(List<YhzxxnzzcyDO> yhzxxnzzcyDOs, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table YhzxxnzzcyDO to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        for (YhzxxnzzcyDO yhzxxnzzcyDO : yhzxxnzzcyDOs) {
            DataRequest request = compriseUtils.yhzxxnzzcyCompriseDataRequest(yhzxxnzzcyDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.DINGTAX, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return yhzxxnzzcyDOs.size() < pageSize;
    }

    /**
     * 批量插入BOT_TRACK_DETAIL
     *
     * @param botTrackDetailDOS
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertBotTrackDetailAndCheckListSize(List<BotTrackDetailDO> botTrackDetailDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_TRACK_DETAIL to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        for (BotTrackDetailDO botTrackDetailDO : botTrackDetailDOS) {
            DataRequest request = compriseUtils.botTrackDetailCompriseDataRequest(botTrackDetailDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botTrackDetailDOS.size() < pageSize;
    }

    /**
     * 批量插入KBS_ARTICLE
     *
     * @param kbsArticleDOS
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertKbsArticleAndCheckListSize(List<KbsArticleDOWithBLOBs> kbsArticleDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_ARTICLE to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        for (KbsArticleDOWithBLOBs kbsArticleDO : kbsArticleDOS) {
            DataRequest request = compriseUtils.kbsArticleDOCompriseDataRequest(kbsArticleDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsArticleDOS.size() < pageSize;
    }

    /**
     * 批量插入YhzxXnzzNsr
     *
     * @param yhzxXnzzNsrs
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertYhzxXnzzNsrAndCheckListSize(List<YhzxXnzzNsr> yhzxXnzzNsrs, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table YhzxXnzzNsr to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        CryptSimple cryptSimple = new CryptSimple();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptSimpleProd(cryptSimple);
        }

        CryptBase36 cryptBase36 = new CryptBase36();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptBase36(cryptBase36);
        }
        for (YhzxXnzzNsr yhzxXnzzNsr : yhzxXnzzNsrs) {
            DataRequest request = compriseUtils.yhzxXnzzNsrCompriseDataRequest(yhzxXnzzNsr, cryptSimple, cryptBase36);
            DSL.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.DINGTAX, request));
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        logger.info("[SyncServiceImpl] insertYhzxXnzzNsrAndCheckListSize pageSize=" + pageSize + "," + yhzxXnzzNsrs.size());
        return yhzxXnzzNsrs.size() < pageSize;
    }

    @Override
    public boolean insertYhzxXnzzTpcQyAndCheckListSize(List<YhzxXnzzTpcQy> yhzxXnzzTpcQys, Integer pageSize){
        logger.info("[SyncServiceImpl] start export table YhzxXnzzNsr to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        CryptSimple cryptSimple = new CryptSimple();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptSimpleProd(cryptSimple);
        }

        CryptBase36 cryptBase36 = new CryptBase36();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptBase36(cryptBase36);
        }
        for (YhzxXnzzTpcQy yhzxXnzzTpcQy : yhzxXnzzTpcQys) {
            DataRequest request = compriseUtils.yhzxXnzzTpcQyCompriseDataRequest(yhzxXnzzTpcQy, cryptSimple, cryptBase36);
            DSL.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.YHZX_XNZZ_TPC_QY, request));
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        logger.info("[SyncServiceImpl] insertYhzxXnzzTpcQyAndCheckListSize pageSize=" + pageSize + "," + yhzxXnzzTpcQys.size());
        return yhzxXnzzTpcQys.size() < pageSize;
    }

    /**
     * 向ES中插入BotBizData相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    @Override
    public boolean insertBotBizDataAndCheckListSize(List<BizData> bizDataList, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_BIZ_DATA to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        CryptSimple cryptSimple =new CryptSimple(CryptSimple.MAX_SEARCH_SIZE_4096);
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptSimpleProd(cryptSimple);
        }

        CryptBase62 cryptBase62 = new CryptBase62(CryptBase62.MAX_SEARCH_SIZE_6);
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils. initCryptBase62Reverse6(cryptBase62);
        }
        for (BizData bizData : bizDataList) {
            DataRequest request = compriseUtils.botBizDataCompriseDataRequest(bizData,cryptSimple,cryptBase62);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return bizDataList.size() < pageSize;
    }

    /**
     * 批量插入 BOT_CHAT_RESPONSE_MESSAGE
     *
     * @param botChatResponseMessageDOS
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertBotChatResponseMessageAndCheckListSize(List<BotChatResponseMessageDO> botChatResponseMessageDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_CHAT_RESPONSE_MESSAGE to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        for (BotChatResponseMessageDO botChatResponseMessageDO : botChatResponseMessageDOS) {
            DataRequest request = compriseUtils.botChatResponseMessageCompriseDatarequest(botChatResponseMessageDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botChatResponseMessageDOS.size() < pageSize;
    }

    /**
     * 批量插入 KBS_QUESTION_ARTICLE
     *
     * @param kbsQuestionArticleDOS
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertKbsQuestionArticleAndCheckListSize(List<KbsQuestionArticleDO> kbsQuestionArticleDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_QUESTION_ARTICLE to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        for (KbsQuestionArticleDO kbsQuestionArticleDO : kbsQuestionArticleDOS) {
            DataRequest request = compriseUtils.kbsQuestionArticleCompriseDataRequest(kbsQuestionArticleDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsQuestionArticleDOS.size() < pageSize;
    }

    /**
     * 批量插入 KBS_QUESTION
     *
     * @param kbsQuestionDOS
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertKbsQuestionAndCheckListSize(List<KbsQuestionDO> kbsQuestionDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_QUESTION to es ....");
        StringBuilder dsl = new StringBuilder(32);
        List<String> messages = new ArrayList<>();
        for (KbsQuestionDO kbsQuestionDO : kbsQuestionDOS) {
            DataRequest request = compriseUtils.kbsQuestionCompriseDataRequest(kbsQuestionDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.KBS_QUESTION, request));
            messages.add(String.valueOf(kbsQuestionDO.getId()));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        // 发送消息给税小蜜业务
        redisMessageSubscriber.sendMessageBatch(ParseConstant.BOT_ES_PRODUCTER, messages.toArray(new String[0]));
        return kbsQuestionDOS.size() < pageSize;
    }

    /***
     * 批量查询 ZX_ARTICLE
     * @param articles
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertZxArticleAndCheckListSize(List<ZxArticle> articles, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table ZX_ARTICLE to es ....");
        StringBuilder dsl = new StringBuilder(32);
        for (ZxArticle zxArticle : articles) {
            DataRequest request = CompriseUtils.zxArticleCompriseDataRequest(zxArticle);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.ZHCS, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return articles.size() < pageSize;
    }

    /**
     * 批量插入 KBS_READING
     *
     * @param kbsReadingDOS
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertKbsReadingAndCheckListSize(List<KbsReadingDOWithBLOBs> kbsReadingDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_READING to es ....");
        StringBuilder dsl = new StringBuilder(32);
        for (KbsReadingDOWithBLOBs kbsReadingDO : kbsReadingDOS) {
            DataRequest request = compriseUtils.kbsReadingCompriseDataRequest(kbsReadingDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsReadingDOS.size() < pageSize;
    }

    /**
     * 批量插入 KBS_KEYWORD
     *
     * @param kbsKeywordDOS
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertKbsKeywordAndCheckListSize(List<KbsKeywordDO> kbsKeywordDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_KEYWORD to es ....");
        StringBuilder dsl = new StringBuilder(32);
        for (KbsKeywordDO kbsKeywordDO : kbsKeywordDOS) {
            DataRequest request = compriseUtils.kbsKeywordCompriseDataRequest(kbsKeywordDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsKeywordDOS.size() < pageSize;
    }

    /**
     * 批量插入 BOT_CONFIG_SERVER
     *
     * @param botConfigServers
     *         BOT_CONFIG_SERVER的集合
     * @param pageSize
     *         pageSize
     */
    @Override
    public boolean insertBotConfigServerAndCheckListSize(List<BotConfigServer> botConfigServers, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_CONFIG_SERVER to es ....");
        StringBuilder dsl = new StringBuilder(32);
        for (BotConfigServer botConfigServer : botConfigServers) {
            DataRequest request = compriseUtils.botConfigServerCompriseDataRequest(botConfigServer);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botConfigServers.size() < pageSize;
    }

    /**
     * 批量导入 BOT_MEDIA
     *
     * @param botMediaDOS
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertBotMediaAndCheckListSize(List<BotMediaDO> botMediaDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_MEDIA to es ....");
        StringBuilder dsl = new StringBuilder(32);
        for (BotMediaDO botMediaDO : botMediaDOS) {
            DataRequest request = compriseUtils.botMediaCompriseDataRequest(botMediaDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botMediaDOS.size() < pageSize;
    }

    /**
     * 批量向 KBS_QUESTION 插入 tags
     *
     * @param tags
     *
     * @return
     */
    @Override
    public boolean insertKbsTags(Map<Long, LinkedList<String>> tags) {
        try {
            StringBuilder dsl = new StringBuilder(20 * 16);
            for (Long key : tags.keySet()) {
                DataRequest request = CompriseUtils.kbsTagsCompriseDataRequest(key, tags.get(key));
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
            elasticSearchBusinessService.bulkOperation(dsl.toString());
        } catch (Exception ex) {
            logger.error("插入失败", ex);
            return false;
        }
        return true;
    }

    /**
     * BotOutoundTaskDetail
     *
     * @param botOutoundTaskDetails
     * @param pageSize
     *
     * @return
     */
    @Override
    public boolean insertBotOutBoundTaskDetailAndCheckListSize(List<BotOutoundTaskDetailWithBLOBs> botOutoundTaskDetails, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BotOutoundTaskDetail to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        CryptSimple cryptSimple = new CryptSimple(CryptSimple.MAX_SEARCH_SIZE_4096);
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptSimpleProd(cryptSimple);
        }

        CryptNumber cryptNumber = new CryptNumber();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptNumber(cryptNumber);
        }
        for (BotOutoundTaskDetailWithBLOBs botOutoundTaskDetail : botOutoundTaskDetails) {
            DataRequest request = compriseUtils.botOutoundTaskDetailCompriseDataRequest(botOutoundTaskDetail, cryptSimple, cryptNumber);
            DSL.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.SHUIXIAOMI, request));
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        logger.info("[SyncServiceImpl] pageSize=" + pageSize + "," + botOutoundTaskDetails.size());
        return botOutoundTaskDetails.size() < pageSize;
    }
}
