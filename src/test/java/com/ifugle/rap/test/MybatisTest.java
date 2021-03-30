/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.test;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ifugle.rap.elasticsearch.enums.ChannelType;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.mapper.BizDataMapper;
import com.ifugle.rap.mapper.BotChatResponseMessageDOMapper;
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
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
import com.ifugle.rap.model.shuixiaomi.BizData;
import com.ifugle.rap.model.shuixiaomi.BotChatResponseMessageDO;
import com.ifugle.rap.model.shuixiaomi.BotMediaDO;
import com.ifugle.rap.model.shuixiaomi.BotTrackDetailDO;
import com.ifugle.rap.model.shuixiaomi.BotUnawareDetailDO;
import com.ifugle.rap.model.shuixiaomi.KbsArticleDOWithBLOBs;
import com.ifugle.rap.model.shuixiaomi.KbsKeywordDO;
import com.ifugle.rap.model.shuixiaomi.KbsQuestionArticleDO;
import com.ifugle.rap.model.shuixiaomi.KbsQuestionDO;
import com.ifugle.rap.model.shuixiaomi.KbsReadingDOWithBLOBs;
import com.ifugle.rap.model.shuixiaomi.dto.KbsTagDTO;
import com.ifugle.rap.service.SyncService;
import com.ifugle.rap.service.thread.KbsArticleInitThread;
import com.ifugle.rap.service.utils.CompriseUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author HuangLei(wenyuan)
 * @version $Id MybatisTest.java v 0.1 2018/11/13 HuangLei(wenyuan) Exp $
 */
public class MybatisTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(MybatisTest.class);

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
    private SyncService syncService;

    @Autowired
    private ElasticSearchBusinessService elasticSearchBusinessService;

    @Test
    public void insertBotUnawareDetailTest() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<BotUnawareDetailDO> botUnawareDetailDOS = botUnawareDetailDOMapper.selectBotUnawareDetailForInit((pageIndex - 1) * pageSize, pageSize);
        //dataSyncService.insertBotUnawareDetailAndCheckListSize(botUnawareDetailDOS, pageSize);
    }

    @Test
    public void selectBotUnawareDetailForIntiTest() {
        Integer first = 0;
        Integer end = 10;
        List<BotUnawareDetailDO> botUnawareDetailDOS = botUnawareDetailDOMapper.selectBotUnawareDetailForInit(first, end);
        for (BotUnawareDetailDO botUnawareDetailDO : botUnawareDetailDOS) {
            logger.info(botUnawareDetailDO.toString());
        }
        BotUnawareDetailDO a = botUnawareDetailDOMapper.selectByPrimaryKey(Long.valueOf("92716"));
        logger.info(a.toString());
    }

    @Test
    public void selectBotTrackDetailForInitTest() {
        Integer first = 0;
        Integer end = 10;
        List<BotTrackDetailDO> botTrackDetailDOS = botTrackDetailDOMapper.selectBotTrackDetailForInit(first, end);
        for (BotTrackDetailDO botTrackDetailDO : botTrackDetailDOS) {
            logger.info(botTrackDetailDO.toString());
        }
    }

    @Test
    public void selectBotChatResponseMessageForInitTest() {
        Integer first = 0;
        Integer end = 10;
        List<BotChatResponseMessageDO> botChatResponseMessageDOS = botChatResponseMessageDOMapper.selectBotChatResponseMessageForInit(first, end);
        for (BotChatResponseMessageDO botChatResponseMessageDO : botChatResponseMessageDOS) {
            logger.info(botChatResponseMessageDO.toString());
        }
    }

    @Test
    public void selectKbsQuestionForInitTest() {
        Integer pageIndex = 1;
        Integer pageSize = 1000;
        while (true) {
            Integer first = (pageIndex - 1) * pageSize;
            List<KbsQuestionDO> kbsQuestionDOS = kbsQuestionDOMapper.selectKbsQuestionForInit(first, pageSize);
            for (KbsQuestionDO kbsQuestionDO : kbsQuestionDOS) {
                logger.info(kbsQuestionDO.toString2());
            }
            if (kbsQuestionDOS.size() < pageSize) {
                break;
            }
            pageIndex++;
        }

    }

    @Test
    public void selectKbsQuestionArticleForIntiTest() {
        Integer first = 0;
        Integer end = 10;
        List<KbsQuestionArticleDO> kbsQuestionArticleDOS = kbsQuestionArticleDOMapper.selectKbsQuestionArticleForInit(first, end);
        for (KbsQuestionArticleDO kbsQuestionArticleDO : kbsQuestionArticleDOS) {
            logger.info(kbsQuestionArticleDO.toString());
        }
    }

    @Test
    public void selectBotUnawareDetailForSyncTest() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<BotUnawareDetailDO> botUnawareDetailDOS = botUnawareDetailDOMapper.selectBotUnawareDetailForSync("0", (pageIndex - 1) * pageSize, pageSize);
        for (BotUnawareDetailDO botUnawareDetailDO : botUnawareDetailDOS) {
            logger.info(botUnawareDetailDO.toString());
        }
    }

    @Test
    public void selectBotTrackDetailForSyncTest() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<BotTrackDetailDO> botTrackDetailDOS = botTrackDetailDOMapper.selectBotTrackDetailForSync("0", (pageIndex - 1) * pageSize, pageSize);
        for (BotTrackDetailDO botTrackDetailDO : botTrackDetailDOS) {
            logger.info(botTrackDetailDO.toString());
        }
    }

    @Test
    public void selectBotChatResponseMessageForSyncTest() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<BotChatResponseMessageDO> botChatResponseMessageDOS = botChatResponseMessageDOMapper.selectBotChatResponseMessageForSync("0", (pageIndex - 1) * pageSize, pageSize);
        for (BotChatResponseMessageDO botChatResponseMessageDO : botChatResponseMessageDOS) {
            logger.info(botChatResponseMessageDO.toString());
        }
    }

    @Test
    public void selectKbsQuestionForSync() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<KbsQuestionDO> kbsQuestionDOS = kbsQuestionDOMapper.selectKbsQuestionForSync("0", (pageIndex - 1) * pageSize, pageSize);
        for (KbsQuestionDO kbsQuestionDO : kbsQuestionDOS) {
            logger.info(kbsQuestionDO.toString());
        }
    }

    @Test
    public void selectKbsQuestionArticleForSync() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<KbsQuestionArticleDO> kbsQuestionArticleDOS = kbsQuestionArticleDOMapper.selectKbsQuestionArticleForSync("0", (pageIndex - 1) * pageSize, pageSize);
        for (KbsQuestionArticleDO kbsQuestionArticleDO : kbsQuestionArticleDOS) {
            logger.info(kbsQuestionArticleDO.toString());
        }
    }

    @Test
    public void selectKbsReadingForInit() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<KbsReadingDOWithBLOBs> kbsReadingDOS = kbsReadingDOMapper.selectKbsReadingForInit((pageIndex - 1) * pageSize, pageSize);
        for (KbsReadingDOWithBLOBs kbsReadingDO : kbsReadingDOS) {
            logger.info(kbsReadingDO.toString());
        }
    }

    @Test
    public void selectKbsReadingForSync() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<KbsReadingDOWithBLOBs> kbsReadingDOS = kbsReadingDOMapper.selectKbsReadingForSync("0", (pageIndex - 1) * pageSize, pageSize);
        for (KbsReadingDOWithBLOBs kbsReadingDO : kbsReadingDOS) {
            logger.info(kbsReadingDO.toString());
        }
    }

    @Test
    public void selectKbsArticleForInitTest() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<KbsArticleDOWithBLOBs> kbsArticleDOWithBLOBsList = kbsArticleDOMapper.selectKbsArticleForInit((pageIndex - 1) * pageSize, pageSize);
        for (KbsArticleDOWithBLOBs kbsArticleDOWithBLOBs : kbsArticleDOWithBLOBsList) {
            logger.info(kbsArticleDOWithBLOBs.toString());
        }
    }

    @Test
    public void selectKbsArticleForSyncTest() {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        List<KbsArticleDOWithBLOBs> kbsArticleDOWithBLOBsList = kbsArticleDOMapper.selectKbsArticleForSync("0", (pageIndex - 1) * pageSize, pageSize);
        for (KbsArticleDOWithBLOBs kbsArticleDOWithBLOBs : kbsArticleDOWithBLOBsList) {
            logger.info(kbsArticleDOWithBLOBs.toString());
        }
    }

    @Autowired
    CompriseUtils compriseUtils;

    @Test
    public void selectKbsArticleForDetailTest() {
        Long id = 367646L;
        KbsArticleDOWithBLOBs kbsArticleDO = kbsArticleDOMapper.selectByPrimaryKey(id);
        StringBuffer DSL = new StringBuffer(32);
        logger.info(JSON.toJSONString(kbsArticleDO));
        DataRequest request = compriseUtils.kbsArticleDOCompriseDataRequest(kbsArticleDO);
        //        if (!elasticSearchBusinessService.checkDataExistsInEs(ChannelType.SHUIXIAOMI, request)) {
        DSL.append(elasticSearchBusinessService.formatInsertDSL(ChannelType.SHUIXIAOMI.getCode(), request));
        //        }
        logger.info(DSL.toString());
        elasticSearchBusinessService.bulkOperation(DSL.toString());
    }

    @Test
    public void selectKbsKeywordForInitTest() {
        Integer pageIndex = 1;
        Integer pageSize = 1000;
        List<KbsKeywordDO> kbsKeywordDOS = kbsKeywordDOMapper.selectKbsKeywordForInit((pageIndex - 1) * pageSize, pageSize);
        for (KbsKeywordDO kbsKeywordDO : kbsKeywordDOS) {
            logger.info(kbsKeywordDO.toString());
        }
    }

    @Test
    public void selectKbsKeywordForSync() {
        Integer pageIndex = 1;
        Integer pageSize = 1000;
        List<KbsKeywordDO> kbsKeywordDOS = kbsKeywordDOMapper.selectKbsKeywordForSync("0", (pageIndex - 1) * pageSize, pageSize);
        for (KbsKeywordDO kbsKeywordDO : kbsKeywordDOS) {
            logger.info(kbsKeywordDO.toString());
        }
    }

    @Test
    public void selectKbsQuestionForUpdateWithoutLastUpdateTimeTest() {
        List<KbsQuestionDO> kbsQuestionDOS = kbsQuestionDOMapper.selectKbsQuestionForUpdateWithoutLastUpdateTime(0, 1000);
        for (KbsQuestionDO kbsQuestionDO : kbsQuestionDOS) {
            logger.info("================================" + kbsQuestionDO + "=============================================");
        }
    }

    @Autowired
    private YhzxxnzzcyDOMapper yhzxxnzzcyDOMapper;

    @Test
    public void selectYhzxxnzzcyForInitTest() {
        List<YhzxxnzzcyDO> yhzxxnzzcyDOS = yhzxxnzzcyDOMapper.selectYhzxxnzzcyForInit(0, 1000);
        for (YhzxxnzzcyDO yhzxxnzzcyDO : yhzxxnzzcyDOS) {
            logger.info(yhzxxnzzcyDO.toString());
        }
    }

    @Autowired
    private BotMediaDOMapper botMediaDOMapper;

    @Test
    public void selectBotMediaForInitTest() {
        List<BotMediaDO> botMediaDOS = botMediaDOMapper.selectBotMediaForInit(0, 1000);
        for (BotMediaDO botMediaDO : botMediaDOS) {
            logger.info(botMediaDO.toString());
        }
    }

    @Autowired
    private KbsTagDTOMapper kbsTagDTOMapper;

    @Test
    public void selectKbsTagForInit() {
        List<KbsTagDTO> kbsTagDTOS = kbsTagDTOMapper.selectKbsTagForInit(0, 1000);
        for (KbsTagDTO dto : kbsTagDTOS) {
            logger.info(dto.toString());
        }
    }

    @Autowired
    private BizDataMapper bizDataMapper;

    @Test
    public void selectBizDataTest() {
        BizData bizData = bizDataMapper.selectByPrimaryKey(1114301L);
        System.out.println(bizData);
    }

    @Test
    public void testImport() {
        //        String name = "{ \"create\": { \"_index\": \"shuixiaomi\", \"_type\": \"KBS_QUESTION\", \"_id\": \"393472\" }} \n" + "\n"
        //                + "{\"VALID_DATE\":null,\"ORIGINAL\":\"SXMSHH\",\"APPROVAL_STATUS\":5,\"QUESTION\":\"企业严重亏损，城镇土地使用税是否有减免？\",\"INVALID_DATE\":null,\"KEYWORD_OPTION\":0,\"CATEGORY\":null,
        // \"MAP_SOURCE\":null,\"CREATOR\":\"同步创建\",\"ID\":393472,\"REMARK\":\"ZJCS6_CQ_QUESTION.ID|1330000002017071100079\",\"ALTERNATE_KEYWORD\":null,\"SYNC_FLAG\":1,\"MAP_ID\":null,
        // \"MODIFIER\":\"同步创建\",\"ANSWER\":null,\"SYNC_TIME\":1536063152000,\"QUESTION_TYPE\":\"21\",\"CREATION_DATE\":1504875578000,\"SEARCH_KEYWORD\":\"困难|亏损|自然灾害|社会公益|扶持产业|土地使用税|优惠|减免\",
        // \"KBS_QUESTION_STATUS\":2,\"PRIMARY_KEYWORD\":\"土地使用税\",\"MODIFICATION_DATE\":1539325633000,\"GRADE\":1,\"SYNONYMS\":null,\"INVALID_REASON\":null} \n"
        //                + "\n" + "{ \"create\": { \"_index\": \"shuixiaomi\", \"_type\": \"KBS_QUESTION\", \"_id\": \"393728\" }} \n" + "\n"
        //                + "{\"VALID_DATE\":null,\"ORIGINAL\":\"SXMSHH\",\"APPROVAL_STATUS\":5,\"QUESTION\":\"全面营改增后政府举办的从事学历教育的高等、中等和初等学校（不含下属单位），举办进修班、培训班取得的哪些收入可以免征增值税？\",\"INVALID_DATE\":null,
        // \"KEYWORD_OPTION\":0,\"CATEGORY\":null,\"MAP_SOURCE\":null,\"CREATOR\":\"同步创建\",\"ID\":393728,\"REMARK\":\"ZJCS6_CQ_QUESTION.ID|1330000002017071300342\",\"ALTERNATE_KEYWORD\":null,
        // \"SYNC_FLAG\":0,\"MAP_ID\":null,\"MODIFIER\":\"龚煦\",\"ANSWER\":null,\"SYNC_TIME\":1536063152000,\"QUESTION_TYPE\":\"3\",\"CREATION_DATE\":1504875578000,
        // \"SEARCH_KEYWORD\":\"进修班|培训班|学校|教育|培训|免税|优惠|辅导班|财政专户|专户管理|学历教育|全额上缴|增值税|营改增\",\"KBS_QUESTION_STATUS\":1,\"PRIMARY_KEYWORD\":\"增值税\",\"MODIFICATION_DATE\":1539325633000,\"GRADE\":1,
        // \"SYNONYMS\":null,\"INVALID_REASON\":null} \n"
        //                + "\n";
        //        System.out.println(name);
        //        elasticSearchBusinessService.bulkOperation(name);
        String name = "{ \"create\": { \"_index\": \"shuixiaomi\", \"_type\": \"KBS_QUESTION\", \"_id\": \"393472\" }} \n" + "\n"
                + "{\"VALID_DATE\":null,\"ORIGINAL\":\"SXMSHH\",\"APPROVAL_STATUS\":5,\"QUESTION\":\"企业严重亏损，城镇土地使用税是否有减免？\",\"INVALID_DATE\":null,\"KEYWORD_OPTION\":0,\"CATEGORY\":null,"
                + "\"MAP_SOURCE\":null,\"CREATOR\":\"同步创建\",\"ID\":393472,\"REMARK\":\"ZJCS6_CQ_QUESTION.ID|1330000002017071100079\",\"ADD_MODE\":0,\"ALTERNATE_KEYWORD\":null,\"SYNC_FLAG\":1,"
                + "\"MAP_ID\":null,\"MODIFIER\":\"同步创建\",\"ANSWER\":null,\"SYNC_TIME\":1536063152000,\"QUESTION_TYPE\":\"21\",\"CREATION_DATE\":1504875578000,"
                + "\"SEARCH_KEYWORD\":\"困难|亏损|自然灾害|社会公益|扶持产业|土地使用税|优惠|减免\",\"KBS_QUESTION_STATUS\":2,\"PRIMARY_KEYWORD\":\"土地使用税\",\"MODIFICATION_DATE\":1539325633000,\"GRADE\":1,\"SYNONYMS\":null,"
                + "\"INVALID_REASON\":null} \n"
                + "\n" + "{ \"create\": { \"_index\": \"shuixiaomi\", \"_type\": \"KBS_QUESTION\", \"_id\": \"393728\" }} \n" + "\n"
                + "{\"VALID_DATE\":null,\"ORIGINAL\":\"SXMSHH\",\"APPROVAL_STATUS\":5,\"QUESTION\":\"全面营改增后政府举办的从事学历教育的高等、中等和初等学校（不含下属单位），举办进修班、培训班取得的哪些收入可以免征增值税？\",\"INVALID_DATE\":null,"
                + "\"KEYWORD_OPTION\":0,\"CATEGORY\":null,\"MAP_SOURCE\":null,\"CREATOR\":\"同步创建\",\"ID\":393728,\"REMARK\":\"ZJCS6_CQ_QUESTION.ID|1330000002017071300342\",\"ADD_MODE\":0,"
                + "\"ALTERNATE_KEYWORD\":null,\"SYNC_FLAG\":0,\"MAP_ID\":null,\"MODIFIER\":\"龚煦\",\"ANSWER\":null,\"SYNC_TIME\":1536063152000,\"QUESTION_TYPE\":\"3\","
                + "\"CREATION_DATE\":1504875578000,\"SEARCH_KEYWORD\":\"进修班|培训班|学校|教育|培训|免税|优惠|辅导班|财政专户|专户管理|学历教育|全额上缴|增值税|营改增\",\"KBS_QUESTION_STATUS\":1,\"PRIMARY_KEYWORD\":\"增值税\","
                + "\"MODIFICATION_DATE\":1539325633000,\"GRADE\":1,\"SYNONYMS\":null,\"INVALID_REASON\":null} \n"
                + "\n";
        List<KbsQuestionDO> kbsQuestionDOS = kbsQuestionDOMapper.selectKbsQuestionForInit(0, 2);
        syncService.insertKbsQuestionAndCheckListSize(kbsQuestionDOS, 2);
    }

    @Test
    public void initArticleTest() {
        KbsArticleInitThread KbsArticleInitThread = new KbsArticleInitThread(100, syncService, kbsArticleDOMapper);
        KbsArticleInitThread.run();
    }
}

