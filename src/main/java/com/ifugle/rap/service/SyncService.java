/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ifugle.rap.model.dingtax.XxzxXxmx;
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.dsb.YhzxXnzzTpcQy;
import com.ifugle.rap.model.sca.BotScaTaskResultDO;
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
import com.ifugle.rap.model.shuixiaomi.KbsQuestionDO;
import com.ifugle.rap.model.shuixiaomi.KbsReadingDOWithBLOBs;
import com.ifugle.rap.model.zhcs.ZxArticle;

/**
 * @author LiuZhengyang
 * @version $Id: SyncService.java 98228 2019-05-07 01:23:46Z HuangLei $
 * @since 2018年10月12日 10:49
 */
public interface SyncService {

    /**
     * 向ES中插入BotUnawareDetail相关数据,并判断是否是是最后一组List，如果是最后一组，返回true
     *
     * @param botUnawareDetailDOS
     * @param pageSize
     *
     * @return
     */
    boolean insertBotUnawareDetailAndCheckListSize(List<BotUnawareDetailDO> botUnawareDetailDOS, Integer pageSize);

    /**
     * 向ES中插入Yhzxxnzzcy相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     *
     * @param yhzxxnzzcyDOs
     * @param pageSize
     *
     * @return
     */
    boolean insertYhzxxnzzcyAndCheckListSize(List<YhzxxnzzcyDO> yhzxxnzzcyDOs, Integer pageSize);

    /**
     * 向ES中插入BotTrackDetail相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertBotTrackDetailAndCheckListSize(List<BotTrackDetailDO> botTrackDetailDOS, Integer pageSize);

    /**
     * 向ES中插入KbsArticle相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertKbsArticleAndCheckListSize(List<KbsArticleDOWithBLOBs> kbsArticleDOS, Integer pageSize);

    /**
     * 向ES中插入KbsArticle相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertYhzxXnzzNsrAndCheckListSize(List<YhzxXnzzNsr> yhzxXnzzNsrs, Integer pageSize);



    boolean insertYhzxXnzzTpcQyAndCheckListSize(List<YhzxXnzzTpcQy> yhzxXnzzTpcQys, Integer pageSize);


    boolean insertBotChatRequestAndCheckListSize(List<BotChatRequest> botChatRequests, Integer pageSize);


    boolean insertXxzxXxmxAndCheckListSize(List<XxzxXxmx> xxzxXxmxes, Integer pageSize);

    /**
     * 向ES中插入BotBizData相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertBotBizDataAndCheckListSize(List<BizData> bizDataList, Integer pageSize);

    /**
     * 向ES中插入BotChatResponseMessage相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertBotChatResponseMessageAndCheckListSize(List<BotChatResponseMessageDO> botChatResponseMessageDOS, Integer pageSize);

    /**
     * 向ES中插入KbsQuestionArticle相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertKbsQuestionArticleAndCheckListSize(List<KbsQuestionArticleDO> kbsQuestionArticleDOS, Integer pageSize);

    /**
     * 向ES中插入KbsQuestion相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertKbsQuestionAndCheckListSize(List<KbsQuestionDO> kbsQuestionDOS, Integer pageSize);

    /**
     * 向ES中插入KbsQuestion相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertZxArticleAndCheckListSize(List<ZxArticle> articles, Integer pageSize);

    /**
     * 向ES中插入KbsReading相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertKbsReadingAndCheckListSize(List<KbsReadingDOWithBLOBs> kbsReadingDOS, Integer pageSize);

    /**
     * 向ES中插入kbsKeyword相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertKbsKeywordAndCheckListSize(List<KbsKeywordDO> kbsKeywordDOS, Integer pageSize);

    /**
     * 向ES中插入botConfigServer相关数据,并判断是否是最后一组,如果是最后一组,返回true
     */
    boolean insertBotConfigServerAndCheckListSize(List<BotConfigServer> botConfigServers, Integer pageSize);

    /**
     * 向ES 中插入kbsKeyword相关数据，并判断是否是最后一组List，如果是最后一组，返回true
     *
     * @param botMediaDOS botMediaDOS
     * @param pageSize pageSize
     *
     * @return
     */
    boolean insertBotMediaAndCheckListSize(List<BotMediaDO> botMediaDOS, Integer pageSize);

    /**
     * 批量向 KBS_QUESTION 插入 tags
     *
     * @param tags
     *
     * @return
     */
    boolean insertKbsTags(Map<Long, LinkedList<String>> tags);

    /***
     * 批量插入BotOutoundTaskDetailWithBLOBs
     * @param botOutoundTaskDetails
     * @param pageSize
     * @return
     */
    boolean insertBotOutBoundTaskDetailAndCheckListSize(List<BotOutoundTaskDetailWithBLOBs> botOutoundTaskDetails, Integer pageSize);
    
    /**
     * 向ES中插入BOT_SCA_TASK_RESULT相关数据,并判断是否是最后一组List，如果是最后一组，返回true
     */
    boolean insertBotScaTaskResultAndCheckListSize(List<BotScaTaskResultDO> botScaTaskResultDO, Integer pageSize);

}
