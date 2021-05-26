/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.model.dingtax.XxzxXxmx;
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.dsb.YhzxXnzzTpcQy;
import com.ifugle.rap.model.sca.BotScaTaskResultDO;
import com.ifugle.rap.model.shuixiaomi.*;
import com.ifugle.rap.model.zhcs.ZxArticle;
import com.ifugle.rap.security.crypto.CryptBase36;
import com.ifugle.rap.security.crypto.CryptBase62;
import com.ifugle.rap.security.crypto.CryptNumber;
import com.ifugle.rap.security.crypto.CryptSimple;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.rocketmq.RocketMqProducter;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.utils.DecodeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    RocketMqProducter rocketMqProducter;

    @Value("${env}")
    String env;

    @Value("${pageSize}")
    Integer pageSize = 1000;

    /**
     * 批量插入BOT_UNAWARE_DETAIL
     *
     * @param botUnawareDetailDOS
     * @param pageSize
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
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.BOT_UNAWARE_DETAIL.getCode(), request));
        }
        logger.info("[bot_unaware_detail] sync data to es success,index = shuixiaomi,size =" + botUnawareDetailDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botUnawareDetailDOS.size() < pageSize;
    }

    /**
     * 批量插入YhzxxnzzcyDO
     *
     * @param yhzxxnzzcyDOs
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertYhzxxnzzcyAndCheckListSize(List<YhzxxnzzcyDO> yhzxxnzzcyDOs, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table YhzxxnzzcyDO to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        for (YhzxxnzzcyDO yhzxxnzzcyDO : yhzxxnzzcyDOs) {
            DataRequest request = compriseUtils.yhzxxnzzcyCompriseDataRequest(yhzxxnzzcyDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.YHZX_XNZZ_NSR.getCode(), request));
        }
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return yhzxxnzzcyDOs.size() < pageSize;
    }

    /**
     * 批量插入BOT_TRACK_DETAIL
     *
     * @param botTrackDetailDOS
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertBotTrackDetailAndCheckListSize(List<BotTrackDetailDO> botTrackDetailDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_TRACK_DETAIL to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        for (BotTrackDetailDO botTrackDetailDO : botTrackDetailDOS) {
            DataRequest request = compriseUtils.botTrackDetailCompriseDataRequest(botTrackDetailDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.BOT_TRACK_DETAIL.getCode(), request));
        }
        logger.info("[bot_track_detail] sync data to es success,index = shuixiaomi,size =" + botTrackDetailDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botTrackDetailDOS.size() < pageSize;
    }

    /**
     * 批量插入KBS_ARTICLE
     *
     * @param kbsArticleDOS
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertKbsArticleAndCheckListSize(List<KbsArticleDOWithBLOBs> kbsArticleDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_ARTICLE to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        List<Long> ids = new ArrayList<>();
        for (KbsArticleDOWithBLOBs kbsArticleDO : kbsArticleDOS) {
            DataRequest request = compriseUtils.kbsArticleDOCompriseDataRequest(kbsArticleDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.KBS_ARTICLE.getCode(), request));
            ids.add(kbsArticleDO.getId());
        }
        logger.info("[yhzx_xnzz_nsr] sync es ids = " + new Gson().toJson(ids));
        logger.info("[kbs_article] sync data to es success,index = shuixiaomi,size =" + kbsArticleDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsArticleDOS.size() < pageSize;
    }

    /**
     * 批量插入YhzxXnzzNsr
     *
     * @param yhzxXnzzNsrs
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertYhzxXnzzNsrAndCheckListSize(List<YhzxXnzzNsr> yhzxXnzzNsrs, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table YHZX_XNZZ_NSR to es .... total=" + yhzxXnzzNsrs.size() + ",pagesize=" + pageSize);
        StringBuffer DSL = new StringBuffer(32);
        CryptSimple cryptSimple = new CryptSimple();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptSimpleProd(cryptSimple);
        }

        CryptBase36 cryptBase36 = new CryptBase36();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptBase36(cryptBase36);
        }

        CryptNumber cryptNumber = new CryptNumber();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptNumber(cryptNumber);
        }
        List<Long> ids  = new ArrayList<>();
        for (YhzxXnzzNsr yhzxXnzzNsr : yhzxXnzzNsrs) {
            DataRequest request = compriseUtils.yhzxXnzzNsrCompriseDataRequest(yhzxXnzzNsr, cryptSimple, cryptBase36, cryptNumber);
            DSL.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.DINGTAX.getCode(), request));
            ids.add(yhzxXnzzNsr.getId());
        }
        if (CollectionUtils.isNotEmpty(ids) && ids.size() != pageSize) {
            logger.info("[yhzx_xnzz_nsr] sync es ids = " + new Gson().toJson(ids));
        }
        logger.info("[yhzx_xnzz_nsr] sync data to es success,index = dingtax,size =" + yhzxXnzzNsrs.size());
        elasticSearchBusinessService.bulkOperation2(DSL.toString());
        return yhzxXnzzNsrs.size() < pageSize;
    }

    @Override
    public boolean insertYhzxXnzzTpcQyAndCheckListSize(List<YhzxXnzzTpcQy> yhzxXnzzTpcQys, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table YHZX_XNZZ_TPC_QY to es .... total=" + yhzxXnzzTpcQys.size() + ",pagesize=" + pageSize);
        StringBuffer DSL = new StringBuffer(32);
        CryptSimple cryptSimple = new CryptSimple();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptSimpleProd(cryptSimple);
        }

        CryptBase36 cryptBase36 = new CryptBase36();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptBase36(cryptBase36);
        }
        List<Long> ids  = new ArrayList<>();
        for (YhzxXnzzTpcQy yhzxXnzzTpcQy : yhzxXnzzTpcQys) {
            DataRequest request = compriseUtils.yhzxXnzzTpcQyCompriseDataRequest(yhzxXnzzTpcQy, cryptSimple, cryptBase36);
            DSL.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.YHZX_XNZZ_TPC_QY.getCode(), request));
            ids.add(yhzxXnzzTpcQy.getId());
        }
        if (CollectionUtils.isNotEmpty(ids) && ids.size() != pageSize) {
            logger.info("[yhzx_xnzz_tpc_qy] sync es ids = " + new Gson().toJson(ids));
        }
        logger.info("[yhzx_xnzz_tpc_qy] sync data to es success,index = yhzx_xnzz_tpc_qy,size =" + yhzxXnzzTpcQys.size());
        elasticSearchBusinessService.bulkOperation2(DSL.toString());
        return yhzxXnzzTpcQys.size() < pageSize;
    }

    @Override
    public boolean insertBotChatRequestAndCheckListSize(List<BotChatRequest> botChatRequests, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table botChatRequests to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        CryptSimple cryptSimple = new CryptSimple();
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptSimpleProd(cryptSimple);
        }

        CryptBase36 cryptBase36 = new CryptBase36(CryptBase36.MAX_SEARCH_SIZE_6);
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptBase36(cryptBase36);
        }
        List<Long> ids = new ArrayList<>();
        for (BotChatRequest botChatRequest : botChatRequests) {
            ids.add(botChatRequest.getId());
            DataRequest request = compriseUtils.botChatRequestCompriseDataRequest(botChatRequest, cryptSimple, cryptBase36);
            DSL.append(elasticSearchBusinessService.formatSaveOrUpdateDSL("bot_chat_request", request));
        }
        if (CollectionUtils.isNotEmpty(ids) && ids.size() != pageSize) {
            logger.info("[bot_chat_request] sync es ids = " + new Gson().toJson(ids));
        }
        logger.info("[bot_chat_request] sync data to es success,index = bot_chat_request,size =" + botChatRequests.size());
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        /***
         * 发送到mq
         */
        EsDocumentData esDocumentData = new EsDocumentData(ids, "doc", "bot_chat_request");
        rocketMqProducter.sendMessage(JSON.toJSONString(esDocumentData));
        logger.info("[SyncServiceImpl] insertBotChatRequestAndCheckListSize pageSize=" + pageSize + "," + botChatRequests.size());
        return botChatRequests.size() < pageSize;
    }


    @Override
    public boolean insertXxzxXxmxAndCheckListSize(List<XxzxXxmx> xxzxXxmxes, Integer pageSize) {
        logger.info("[SyncServiceImpl] start insertXxzxXxmxAndCheckListSize table to es .... ");
        StringBuffer DSL = new StringBuffer(32);
        CryptSimple cryptSimple = new CryptSimple(CryptSimple.MAX_SEARCH_SIZE_4096);
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptSimpleProd(cryptSimple);
        }

        CryptBase36 cryptBase36 = new CryptBase36(CryptBase36.MAX_SEARCH_SIZE_6);
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptBase36(cryptBase36);
        }
        for (XxzxXxmx xxzxXxmx : xxzxXxmxes) {
            DataRequest request = compriseUtils.xxzxXxmxCompriseDataRequest(xxzxXxmx, cryptSimple, cryptBase36);
            DSL.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.XXZX_XXMX.getCode(), request));
        }
        logger.info("[xxzx_xxmx] sync data to es success,index = xxzx_xxmx,size =" + xxzxXxmxes.size());
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        logger.info("[SyncServiceImpl] insertXxzxXxmxAndCheckListSize pageSize=" + pageSize + "," + xxzxXxmxes.size());
        return xxzxXxmxes.size() < pageSize;
    }


    /**
     * 向ES中插入BotBizData相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    @Override
    public boolean insertBotBizDataAndCheckListSize(List<BizData> bizDataList, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_BIZ_DATA to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        CryptSimple cryptSimple = new CryptSimple(CryptSimple.MAX_SEARCH_SIZE_4096);
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptSimpleProd(cryptSimple);
        }

        CryptBase62 cryptBase62 = new CryptBase62(CryptBase62.MAX_SEARCH_SIZE_6);
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptBase62Reverse6(cryptBase62);
        }
        for (BizData bizData : bizDataList) {
            DataRequest request = compriseUtils.botBizDataCompriseDataRequest(bizData, cryptSimple, cryptBase62);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.BOT_BIZ_DATA.getCode(), request));
        }
        logger.info("[BOT_BIZ_DATA] sync data to es success,index = SHUIXIAOMI,size =" + bizDataList.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return bizDataList.size() < pageSize;
    }

    /**
     * 批量插入 BOT_CHAT_RESPONSE_MESSAGE
     *
     * @param botChatResponseMessageDOS
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertBotChatResponseMessageAndCheckListSize(List<BotChatResponseMessageDO> botChatResponseMessageDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_CHAT_RESPONSE_MESSAGE to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        List<Long> ids = new ArrayList<>();
        for (BotChatResponseMessageDO botChatResponseMessageDO : botChatResponseMessageDOS) {
            DataRequest request = compriseUtils.botChatResponseMessageCompriseDatarequest(botChatResponseMessageDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.BOT_CHAT_RESPONSE_MESSAGE.getCode(), request));
            ids.add(botChatResponseMessageDO.getId());
        }
        if (CollectionUtils.isNotEmpty(ids) && ids.size() != pageSize) {
            logger.info("[bot_outbound_task_detail] sync es ids = " + new Gson().toJson(ids));
        }
        logger.info("[BOT_CHAT_RESPONSE_MESSAGE] sync data to es success,index = SHUIXIAOMI,size =" + botChatResponseMessageDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botChatResponseMessageDOS.size() < pageSize;
    }

    /**
     * 批量插入 KBS_QUESTION_ARTICLE
     *
     * @param kbsQuestionArticleDOS
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertKbsQuestionArticleAndCheckListSize(List<KbsQuestionArticleDO> kbsQuestionArticleDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_QUESTION_ARTICLE to es .... ");
        StringBuilder dsl = new StringBuilder(32);
        List<Long> ids = new ArrayList<>();
        for (KbsQuestionArticleDO kbsQuestionArticleDO : kbsQuestionArticleDOS) {
            DataRequest request = compriseUtils.kbsQuestionArticleCompriseDataRequest(kbsQuestionArticleDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.KBS_QUESTION_ARTICLE.getCode(), request));
            ids.add(kbsQuestionArticleDO.getId());
        }
        if (CollectionUtils.isNotEmpty(ids) && ids.size() != pageSize) {
            logger.info("[bot_outbound_task_detail] sync es ids = " + new Gson().toJson(ids));
        }
        logger.info("[kbs_question_article] sync data to es success,index = SHUIXIAOMI,size =" + kbsQuestionArticleDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsQuestionArticleDOS.size() < pageSize;
    }

    /**
     * 批量插入 KBS_QUESTION
     *
     * @param kbsQuestionDOS
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertKbsQuestionAndCheckListSize(List<KbsQuestionDO> kbsQuestionDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_QUESTION to es ....");
        StringBuilder dsl = new StringBuilder(32);
        List<Long> messages = new ArrayList<>();
        for (KbsQuestionDO kbsQuestionDO : kbsQuestionDOS) {
            DataRequest request = compriseUtils.kbsQuestionCompriseDataRequest(kbsQuestionDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.KBS_QUESTION.getCode(), request));
            messages.add(kbsQuestionDO.getId());
        }
        if (CollectionUtils.isNotEmpty(messages) && messages.size() != pageSize) {
            logger.info("[kbs_question] sync es ids = " + new Gson().toJson(messages));
        }
        logger.info("[kbs_question] sync data to es success,index = KBS_QUESTION,size =" + kbsQuestionDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        // 发送消息给税小蜜业务
        if(messages.size()>0) {
//            redisMessageSubscriber.sendMessageBatch(ParseConstant.BOT_ES_PRODUCTER, messages.toArray(new String[0]));
            EsDocumentData esDocumentData = new EsDocumentData(messages, "doc", ChannelType.KBS_QUESTION.getCode());
            rocketMqProducter.sendKbsQuestionMessage(JSON.toJSONString(esDocumentData));
        }
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
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.ZHCS.getCode(), request));
        }
        logger.info("[ZX_ARTICLE] sync data to es success,index = SHUIXIAOMI,size =" + articles.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return articles.size() < pageSize;
    }

    /**
     * 批量插入 KBS_READING
     *
     * @param kbsReadingDOS
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertKbsReadingAndCheckListSize(List<KbsReadingDOWithBLOBs> kbsReadingDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_READING to es ....");
        StringBuilder dsl = new StringBuilder(32);
        for (KbsReadingDOWithBLOBs kbsReadingDO : kbsReadingDOS) {
            DataRequest request = compriseUtils.kbsReadingCompriseDataRequest(kbsReadingDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.KBS_READING.getCode(), request));
        }
        logger.info("[kbs_reading] sync data to es success,index = SHUIXIAOMI,size =" + kbsReadingDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsReadingDOS.size() < pageSize;
    }

    /**
     * 批量插入 KBS_KEYWORD
     *
     * @param kbsKeywordDOS
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertKbsKeywordAndCheckListSize(List<KbsKeywordDO> kbsKeywordDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table KBS_KEYWORD to es ....");
        StringBuilder dsl = new StringBuilder(32);
        for (KbsKeywordDO kbsKeywordDO : kbsKeywordDOS) {
            DataRequest request = compriseUtils.kbsKeywordCompriseDataRequest(kbsKeywordDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.KBS_KEYWORD.getCode(), request));
        }
        logger.info("[kbs_keyword] sync data to es success,index = SHUIXIAOMI,size =" + kbsKeywordDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return kbsKeywordDOS.size() < pageSize;
    }

    /**
     * 批量插入 BOT_CONFIG_SERVER
     *
     * @param botConfigServers BOT_CONFIG_SERVER的集合
     * @param pageSize         pageSize
     */
    @Override
    public boolean insertBotConfigServerAndCheckListSize(List<BotConfigServer> botConfigServers, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_CONFIG_SERVER to es ....");
        StringBuilder dsl = new StringBuilder(32);
        for (BotConfigServer botConfigServer : botConfigServers) {
            DataRequest request = compriseUtils.botConfigServerCompriseDataRequest(botConfigServer);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.BOT_CONFIG_SERVER.getCode(), request));
        }
        logger.info("[bot_config_server] sync data to es success,index = SHUIXIAOMI,size =" + botConfigServers.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botConfigServers.size() < pageSize;
    }

    /**
     * 批量导入 BOT_MEDIA
     *
     * @param botMediaDOS
     * @param pageSize
     * @return
     */
    @Override
    public boolean insertBotMediaAndCheckListSize(List<BotMediaDO> botMediaDOS, Integer pageSize) {
        logger.info("[SyncServiceImpl] start export table BOT_MEDIA to es ....");
        StringBuilder dsl = new StringBuilder(32);
        for (BotMediaDO botMediaDO : botMediaDOS) {
            DataRequest request = compriseUtils.botMediaCompriseDataRequest(botMediaDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.BOT_MEDIA.getCode(), request));
        }
        logger.info("[bot_media] sync data to es success,index = SHUIXIAOMI,size =" + botMediaDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        return botMediaDOS.size() < pageSize;
    }

    /**
     * 批量向 KBS_QUESTION 插入 tags
     *
     * @param tags
     * @return
     */
    @Override
    public boolean insertKbsTags(Map<Long, LinkedList<String>> tags) {
        try {
            StringBuilder dsl = new StringBuilder(20 * 16);
            for (Long key : tags.keySet()) {
                DataRequest request = CompriseUtils.kbsTagsCompriseDataRequest(key, tags.get(key));
                dsl.append(elasticSearchBusinessService.formatUpdateDSL(ChannelType.KBS_QUESTION.getCode(), request));
            }
            logger.info("[kbs_tags] sync data to es success,index = SHUIXIAOMI,size =" + tags.size());
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


        CryptBase36 cryptBase36 = new CryptBase36(CryptBase36.MAX_SEARCH_SIZE_6);
        if (StringUtils.equalsIgnoreCase(env, "prod")) {
            DecodeUtils.initCryptBase36(cryptBase36);
        }

        List<Long> ids = new ArrayList<>();
        for (BotOutoundTaskDetailWithBLOBs botOutoundTaskDetail : botOutoundTaskDetails) {
            DataRequest request = compriseUtils.botOutoundTaskDetailCompriseDataRequest(botOutoundTaskDetail, cryptSimple, cryptNumber, cryptBase36);
            DSL.append(elasticSearchBusinessService.formatSaveOrUpdateDSL("bot_outbound_task_detail", request));
            ids.add(botOutoundTaskDetail.getId());
        }
        if (CollectionUtils.isNotEmpty(ids) && ids.size() != pageSize) {
            logger.info("[bot_outbound_task_detail] sync es ids = " + new Gson().toJson(ids));
        }
        logger.info("[bot_outbound_task_detail] sync data to es success,index = bot_outbound_task_detail,size =" + botOutoundTaskDetails.size());
        elasticSearchBusinessService.bulkOperation(DSL.toString());
        EsDocumentData esDocumentData = new EsDocumentData(ids, "doc", "bot_outbound_task_detail");
        rocketMqProducter.sendMessage(JSON.toJSONString(esDocumentData));
        logger.info("[SyncServiceImpl] pageSize=" + pageSize + "," + botOutoundTaskDetails.size());
        return botOutoundTaskDetails.size() < pageSize;
    }

	@Override
	public boolean insertBotScaTaskResultAndCheckListSize(List<BotScaTaskResultDO> botScaTaskResultDOS,
			Integer pageSize) {
		logger.info("[SyncServiceImpl] start export table BOT_SCA_TASK_RESULT to es ....");
        StringBuilder dsl = new StringBuilder(32);
        List<Long> messages = new ArrayList<>();
        for (BotScaTaskResultDO botScaTaskResultDO : botScaTaskResultDOS) {
            DataRequest request = compriseUtils.botScaTaskResultCompriseDataRequest(botScaTaskResultDO);
            dsl.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(ChannelType.BOT_SCA_TASK_RESULT.getCode(), request));
            messages.add(botScaTaskResultDO.getId());
        }
        if (CollectionUtils.isNotEmpty(messages) && messages.size() != pageSize) {
            logger.info("[bot_sca_task_result] sync es ids = " + new Gson().toJson(messages));
        }
        logger.info("[bot_sca_task_result] sync data to es success,index = BOT_SCA_TASK_RESULT,size =" + botScaTaskResultDOS.size());
        elasticSearchBusinessService.bulkOperation(dsl.toString());
        // 发送消息给税小蜜业务
        if(messages.size()>0) {
//            redisMessageSubscriber.sendMessageBatch(ParseConstant.BOT_ES_PRODUCTER, messages.toArray(new String[0]));
            EsDocumentData esDocumentData = new EsDocumentData(messages, "doc", ChannelType.BOT_SCA_TASK_RESULT.getCode());
            rocketMqProducter.sendBotScaTaskResultMessage(JSON.toJSONString(esDocumentData));
        }
        return botScaTaskResultDOS.size() < pageSize;
	}
}
