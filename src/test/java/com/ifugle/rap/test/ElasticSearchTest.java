/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.test;

/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */

import com.alibaba.fastjson.JSON;
import com.ifugle.rap.elasticsearch.api.BusinessCommonApi;
import com.ifugle.rap.elasticsearch.api.BusinessSearchApi;
import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.model.RealtimeSearchResponse;
import com.ifugle.rap.elasticsearch.model.SimpleSearchRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.mapper.*;
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
import com.ifugle.rap.model.shuixiaomi.BotMediaDO;
import com.ifugle.rap.model.shuixiaomi.KbsQuestionDO;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.impl.DataSyncServiceImpl;
import com.ifugle.rap.service.thread.BotConfigServerInitThread;
import com.ifugle.rap.service.thread.KbsTagInitThread;
import com.ifugle.rap.service.utils.CompriseUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id: ElasticSearchTest.java 102069 2019-07-02 08:22:31Z LiuZhengyang $
 */
public class ElasticSearchTest extends  BaseTest{

    @Autowired
    private BusinessSearchApi   businessSearchApi;

    @Autowired
    private BusinessCommonApi   businessCommonApi;

    @Autowired
    private DataSyncServiceImpl dataSyncServiceImpl;

    @Autowired
    private KbsQuestionDOMapper kbsQuestionDOMapper;

    @Autowired
    private SyncService syncService;


    private final static Logger logger = LoggerFactory.getLogger(ElasticSearchTest.class);

    @Test
    public void queryTest() {
        SimpleSearchRequest request = new SimpleSearchRequest();
        request.setPage(1);
        request.setPageSize(20);

        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("user_name", "huanglei");
        request.setSearchParams(searchParams);
        RealtimeSearchResponse response = businessSearchApi.queryBusinessData(
                ChannelType.SHUIXIAOMI.getCode(), "track", request);
        logger.error("==============response===========" + JSON.toJSONString(response));
    }

    @Test
    public void queryDetailTest() {
        Map<String, Object>  map = businessCommonApi.get(ChannelType.SHUIXIAOMI.getCode(), "KBS_ARTICLE", "220997");
        logger.info("==============response==========="+JSON.toJSONString(map));
    }

    @Test
    public void updateKbsArticleForSyncTest(){
//        dataSyncServiceImpl.updateKbsArticleForSync();
    }

    @Test
    public void updateKbsQuestionSyncTest(){
        KbsQuestionDO kbsQuestionDO = kbsQuestionDOMapper.selectByPrimaryKey(378867l);
        List<KbsQuestionDO> kbsQuestionDOS = new ArrayList<KbsQuestionDO>();
        kbsQuestionDOS.add(kbsQuestionDO);
//        dataSyncServiceImpl.updateKbsQuestionAndCheckListSize(kbsQuestionDOS,100);
    }

    @Autowired
    private YhzxxnzzcyDOMapper yhzxxnzzcyDOMapper;

    @Test
    public void insertYhzxxnzzcyAndCheckListSizeTest(){
        List<YhzxxnzzcyDO> yhzxxnzzcyDOS =  yhzxxnzzcyDOMapper.selectYhzxxnzzcyForInit(0,1000);
        syncService.insertYhzxxnzzcyAndCheckListSize(yhzxxnzzcyDOS,1000);
    }

    @Autowired
    private BotMediaDOMapper botMediaDOMapper;

    @Autowired
    private CompriseUtils compriseUtils;

    @Test
    public void botMediaTest(){
        List<BotMediaDO> botMediaDOS = botMediaDOMapper.selectBotMediaForInit(0,1000);
        StringBuffer DSL = new StringBuffer(32);
        for (BotMediaDO botMediaDO : botMediaDOS) {
            DataRequest request = compriseUtils.botMediaCompriseDataRequest(botMediaDO);
            DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI.getCode(), request));
        }
        elasticSearchBusinessService.bulkOperation(DSL.toString());
    }

    @Test
    public void execBulk(){
        elasticSearchBusinessService.bulkOperation("{ \"index\": {  \"_index\": \"dingtax\",\"_type\": \"test_type\",  \"_id\": \"1\" }}{ \"name\": \"huanglei\",\"age\": \"38\"}{ \"index\": {  \"_index\": \"dingtax\",\"_type\": \"test_type\",  \"_id\": \"2\" }}{ \"name\": \"huyu\",\"age\": \"24\"}{ \"index\": {  \"_index\": \"dingtax\",\"_type\": \"test_type\",  \"_id\": \"3\" }}{ \"name\": \"laozhu\",\"age\": \"48\"}");
    }

    @Test
    public void botMedisUpdate(){
//        dataSyncServiceImpl.updateBotMediaForSync();
    }

    @Autowired
    private ElasticSearchBusinessService elasticSearchBusinessService;

    static {
        System.setProperty("rap.redis.server","127.0.0.1:16379");
        System.setProperty("rap.redis.auth.password","dsbwansui");
        System.setProperty("rap.redis.channels","KBS_QUESTION");
    }

    @Autowired
    private KbsTagDTOMapper kbsTagDTOMapper;

    @Test
    public void kbsTagInitThreadTest(){
        KbsTagInitThread initThread = new KbsTagInitThread(1000,syncService,kbsTagDTOMapper);
        initThread.run();
    }

    @Test
    public void  testImport(){
        String name = "{ \"create\": { \"_index\": \"shuixiaomi\", \"_type\": \"KBS_QUESTION\", \"_id\": \"393472\" }} \n" + "\n"
                + "{\"VALID_DATE\":null,\"ORIGINAL\":\"SXMSHH\",\"APPROVAL_STATUS\":5,\"QUESTION\":\"企业严重亏损，城镇土地使用税是否有减免？\",\"INVALID_DATE\":null,\"KEYWORD_OPTION\":0,\"CATEGORY\":null,\"MAP_SOURCE\":null,\"CREATOR\":\"同步创建\",\"ID\":393472,\"REMARK\":\"ZJCS6_CQ_QUESTION.ID|1330000002017071100079\",\"ALTERNATE_KEYWORD\":null,\"SYNC_FLAG\":1,\"MAP_ID\":null,\"MODIFIER\":\"同步创建\",\"ANSWER\":null,\"SYNC_TIME\":1536063152000,\"QUESTION_TYPE\":\"21\",\"CREATION_DATE\":1504875578000,\"SEARCH_KEYWORD\":\"困难|亏损|自然灾害|社会公益|扶持产业|土地使用税|优惠|减免\",\"KBS_QUESTION_STATUS\":2,\"PRIMARY_KEYWORD\":\"土地使用税\",\"MODIFICATION_DATE\":1539325633000,\"GRADE\":1,\"SYNONYMS\":null,\"INVALID_REASON\":null} \n"
                + "\n" + "{ \"create\": { \"_index\": \"shuixiaomi\", \"_type\": \"KBS_QUESTION\", \"_id\": \"393728\" }} \n" + "\n"
                + "{\"VALID_DATE\":null,\"ORIGINAL\":\"SXMSHH\",\"APPROVAL_STATUS\":5,\"QUESTION\":\"全面营改增后政府举办的从事学历教育的高等、中等和初等学校（不含下属单位），举办进修班、培训班取得的哪些收入可以免征增值税？\",\"INVALID_DATE\":null,\"KEYWORD_OPTION\":0,\"CATEGORY\":null,\"MAP_SOURCE\":null,\"CREATOR\":\"同步创建\",\"ID\":393728,\"REMARK\":\"ZJCS6_CQ_QUESTION.ID|1330000002017071300342\",\"ALTERNATE_KEYWORD\":null,\"SYNC_FLAG\":0,\"MAP_ID\":null,\"MODIFIER\":\"龚煦\",\"ANSWER\":null,\"SYNC_TIME\":1536063152000,\"QUESTION_TYPE\":\"3\",\"CREATION_DATE\":1504875578000,\"SEARCH_KEYWORD\":\"进修班|培训班|学校|教育|培训|免税|优惠|辅导班|财政专户|专户管理|学历教育|全额上缴|增值税|营改增\",\"KBS_QUESTION_STATUS\":1,\"PRIMARY_KEYWORD\":\"增值税\",\"MODIFICATION_DATE\":1539325633000,\"GRADE\":1,\"SYNONYMS\":null,\"INVALID_REASON\":null} \n"
                + "\n";
        System.out.println(name);
        elasticSearchBusinessService.bulkOperation(name);
    }

    @Autowired
    private BotConfigServerMapper botConfigServerMapper;

    @Test
    public void botConfigServerInitThreadTest(){
        BotConfigServerInitThread botConfigServerInitThread = new BotConfigServerInitThread(20,syncService,botConfigServerMapper);
        botConfigServerInitThread.run();
    }


}

