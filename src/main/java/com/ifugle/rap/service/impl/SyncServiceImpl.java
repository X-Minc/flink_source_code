/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
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
import com.ifugle.rap.service.redis.ParseConstant;
import com.ifugle.rap.service.redis.RedisMessageSubscriber;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.utils.DecodeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    /**
     * 批量插入BOT_UNAWARE_DETAIL
     *
     * @param botUnawareDetailDOS
     * @param pageSize
     *
     * @return
     */
    public boolean insertBotUnawareDetailAndCheckListSize(List<BotUnawareDetailDO> botUnawareDetailDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_UNAWARE_DETAIL to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        for (BotUnawareDetailDO botUnawareDetailDO : botUnawareDetailDOS) {
            if(!botUnawareDetailDO.isNew()){
                continue;
            }
            logger.debug(MessageFormat.format("[DataSyncServiceImpl] insertBotUnawareDetailAndCheckListSize botUnawareDetailDO {0}", botUnawareDetailDO.toString()));
            DataRequest request = compriseUtils.botUnawareDetailCompriseDataRequest(botUnawareDetailDO);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertYhzxxnzzcyAndCheckListSize(List<YhzxxnzzcyDO> yhzxxnzzcyDOs, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table YhzxxnzzcyDO to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        for (YhzxxnzzcyDO yhzxxnzzcyDO : yhzxxnzzcyDOs) {
            DataRequest request = compriseUtils.yhzxxnzzcyCompriseDataRequest(yhzxxnzzcyDO);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.DINGTAX, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.DINGTAX, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertBotTrackDetailAndCheckListSize(List<BotTrackDetailDO> botTrackDetailDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_TRACK_DETAIL to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        for (BotTrackDetailDO botTrackDetailDO : botTrackDetailDOS) {
            DataRequest request = compriseUtils.botTrackDetailCompriseDataRequest(botTrackDetailDO);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertKbsArticleAndCheckListSize(List<KbsArticleDOWithBLOBs> kbsArticleDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_ARTICLE to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        for (KbsArticleDOWithBLOBs kbsArticleDO : kbsArticleDOS) {
            if(!kbsArticleDO.isNew()){
                continue;
            }
            DataRequest request = compriseUtils.kbsArticleDOCompriseDataRequest(kbsArticleDO);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertYhzxXnzzNsrAndCheckListSize(List<YhzxXnzzNsr> yhzxXnzzNsrs, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table YhzxXnzzNsr to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        CryptSimple cryptSimple=new CryptSimple();
        DecodeUtils.initCryptSimpleProd(cryptSimple);

        CryptBase36 cryptBase36 =new CryptBase36();
        DecodeUtils.initCryptBase36(cryptBase36);
        for (YhzxXnzzNsr yhzxXnzzNsr : yhzxXnzzNsrs) {
            DataRequest request = compriseUtils.yhzxXnzzNsrCompriseDataRequest(yhzxXnzzNsr,cryptSimple,cryptBase36);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.DINGTAX, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.DINGTAX, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        logger.info("[SyncServiceImpl] pageSize="+pageSize+","+yhzxXnzzNsrs.size());
        return yhzxXnzzNsrs.size() < pageSize;
    }

    /**
     * 向ES中插入BotBizData相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    public boolean insertBotBizDataAndCheckListSize(List<BizData> bizDataList, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_BIZ_DATA to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        for (BizData bizData : bizDataList) {
            if(!bizData.isNew()){
                continue;
            }
            DataRequest request = compriseUtils.botBizDataCompriseDataRequest(bizData);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertBotChatResponseMessageAndCheckListSize(List<BotChatResponseMessageDO> botChatResponseMessageDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_CHAT_RESPONSE_MESSAGE to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        for (BotChatResponseMessageDO botChatResponseMessageDO : botChatResponseMessageDOS) {
            DataRequest request = compriseUtils.botChatResponseMessageCompriseDatarequest(botChatResponseMessageDO);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertKbsQuestionArticleAndCheckListSize(List<KbsQuestionArticleDO> kbsQuestionArticleDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_QUESTION_ARTICLE to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        for (KbsQuestionArticleDO kbsQuestionArticleDO : kbsQuestionArticleDOS) {
            if(!kbsQuestionArticleDO.isNew()){
                continue;
            }
            DataRequest request = compriseUtils.kbsQuestionArticleCompriseDataRequest(kbsQuestionArticleDO);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertKbsQuestionAndCheckListSize(List<KbsQuestionDO> kbsQuestionDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_QUESTION to es ....");
        StringBuffer DSL = new StringBuffer(32);
        List<String> messages = new ArrayList<String>();
        for (int i = 0;i<kbsQuestionDOS.size();i++) {
            if(!kbsQuestionDOS.get(i).isNew()){
                continue;
            }
            DataRequest request = compriseUtils.kbsQuestionCompriseDataRequest(kbsQuestionDOS.get(i));
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
                messages.add(String.valueOf(kbsQuestionDOS.get(i).getId()));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        // 发送消息给税小蜜业务
        redisMessageSubscriber.sendMessageBatch(ParseConstant.BOT_ES_PRODUCTER,messages.toArray(new String[0]));
        return kbsQuestionDOS.size() < pageSize;
    }

    /***
     * 批量查询 ZX_ARTICLE
     * @param articles
     * @param pageSize
     * @return
     */
    public boolean insertZxArticleAndCheckListSize(List<ZxArticle> articles, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table ZX_ARTICLE to es ....");
        StringBuffer DSL = new StringBuffer(32);
        for (ZxArticle zxArticle : articles) {
            DataRequest request = compriseUtils.zxArticleCompriseDataRequest(zxArticle);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.ZHCS, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.ZHCS, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertKbsReadingAndCheckListSize(List<KbsReadingDOWithBLOBs> kbsReadingDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_READING to es ....");
        StringBuffer DSL = new StringBuffer(32);
        for (KbsReadingDOWithBLOBs kbsReadingDO : kbsReadingDOS) {
            if(!kbsReadingDO.isNew()){
                continue;
            }
            DataRequest request = compriseUtils.kbsReadingCompriseDataRequest(kbsReadingDO);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertKbsKeywordAndCheckListSize(List<KbsKeywordDO> kbsKeywordDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_KEYWORD to es ....");
        StringBuffer DSL = new StringBuffer(32);
        for (KbsKeywordDO kbsKeywordDO : kbsKeywordDOS) {
            if(!kbsKeywordDO.isNew()){
                continue;
            }
            DataRequest request = compriseUtils.kbsKeywordCompriseDataRequest(kbsKeywordDO);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        return kbsKeywordDOS.size() < pageSize;
    }

    /**
     * 批量插入 BOT_CONFIG_SERVER
     *
     * @param botConfigServers BOT_CONFIG_SERVER的集合
     * @param pageSize pageSize
     */
    public boolean insertBotConfigServerAndCheckListSize(List<BotConfigServer> botConfigServers, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_CONFIG_SERVER to es ....");
        StringBuffer DSL = new StringBuffer(32);
        for (BotConfigServer botConfigServer : botConfigServers) {
            DataRequest request = compriseUtils.botConfigServerCompriseDataRequest(botConfigServer);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
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
    public boolean insertBotMediaAndCheckListSize(List<BotMediaDO> botMediaDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_MEDIA to es ....");
        StringBuffer DSL = new StringBuffer(32);
        for (BotMediaDO botMediaDO : botMediaDOS) {
            if(!botMediaDO.isNew()){
                continue;
            }
            DataRequest request = compriseUtils.botMediaCompriseDataRequest(botMediaDO);
            if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
                DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI, request));
            }
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        return botMediaDOS.size() < pageSize;
    }

    /**
     * 批量向 KBS_QUESTION 插入 tags
     *
     * @param tags
     *
     * @return
     */
    public boolean insertKbsTags(Map<Long, LinkedList<String>> tags) {
        try {
            StringBuffer DSL = new StringBuffer(20 * 16);
            for (Long key : tags.keySet()) {
                DataRequest request = CompriseUtils.kbsTagsCompriseDataRequest(key, tags.get(key));
                DSL.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.SHUIXIAOMI, request));
            }
            elasticSearchBusinessService.bulkOperation(DSL.toString());
        } catch (Exception ex) {
            logger.error("插入失败", ex);
            return false;
        }
        return true;
    }
}
