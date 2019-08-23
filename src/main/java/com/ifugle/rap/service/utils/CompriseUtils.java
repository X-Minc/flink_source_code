/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.utils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.ifugle.rap.constants.SystemConstants;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.dsb.YhzxXnzzTpcQy;
import com.ifugle.rap.model.enums.TablesEnum;
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
import com.ifugle.rap.security.crypto.CryptNumber;
import com.ifugle.rap.security.crypto.CryptSimple;
import com.ifugle.rap.security.crypto.CryptZip;
import com.ifugle.rap.utils.DecodeUtils;
import com.ifugle.rap.utils.MyHttpRequest;

/**
 * @author LiuZhengyang
 * @version $Id: CompriseUtils.java 100998 2019-06-18 02:40:50Z LiuZhengyang $
 * @since 2018年10月12日 10:49
 */
@Component
public class CompriseUtils {

    @Autowired
    private CryptZip cryptZip;

    @Value("${profiles.active}")
    String env;

    private static final Logger logger = LoggerFactory.getLogger(CompriseUtils.class);

    public DataRequest kbsArticleDOCompriseDataRequest(KbsArticleDOWithBLOBs kbsArticleDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.KBS_ARTICLE.getTableName());
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", kbsArticleDO.getId());
        hashMap.put("PARENT_ID", kbsArticleDO.getParentId());
        hashMap.put("AREA_ID", kbsArticleDO.getAreaId());
        hashMap.put("ARTICLE_TYPE", kbsArticleDO.getArticleType());
        hashMap.put("BIZ_TYPE", kbsArticleDO.getBizType());
        hashMap.put("CATEGORY", kbsArticleDO.getCategory());
        hashMap.put("KEYWORD", kbsArticleDO.getKeyword());
        hashMap.put("TITLE", kbsArticleDO.getTitle());
        hashMap.put("CONTENT", kbsArticleDO.getContent());
        hashMap.put("CONTENT_TEXT", kbsArticleDO.getContentText());
        hashMap.put("CONTENT_HTML", kbsArticleDO.getContentHtml());
        hashMap.put("REMARK", kbsArticleDO.getRemark());
        hashMap.put("KBS_ARTICLE_STATUS", kbsArticleDO.getStatus());
        hashMap.put("VALID_FLAG", kbsArticleDO.getValidFlag());
        hashMap.put("VALID_DATE", kbsArticleDO.getValidDate() == null ? null : getLongData(kbsArticleDO.getValidDate()));
        hashMap.put("INVALID_DATE", kbsArticleDO.getInvalidDate() == null ? null : getLongData(kbsArticleDO.getInvalidDate()));
        hashMap.put("ORIGINAL", kbsArticleDO.getOriginal());
        hashMap.put("ISSUE_NO", kbsArticleDO.getIssueNo());
        hashMap.put("ISSUE_ORG", kbsArticleDO.getIssueOrg());
        hashMap.put("ISSUE_DATE", kbsArticleDO.getIssueDate() == null ? null : getLongData(kbsArticleDO.getIssueDate()));
        hashMap.put("MAP_SOURCE", kbsArticleDO.getMapSource());
        hashMap.put("MAP_ID", kbsArticleDO.getMapId());
        hashMap.put("SYNC_FLAG", kbsArticleDO.getSyncFlag());
        hashMap.put("SYNC_TIME", getLongData(kbsArticleDO.getSyncTime()));
        hashMap.put("CREATOR", kbsArticleDO.getCreator());
        hashMap.put("CREATION_DATE", kbsArticleDO.getCreationDate() == null ? null : getLongData(kbsArticleDO.getCreationDate()));
        hashMap.put("MODIFIER", kbsArticleDO.getModifier());
        hashMap.put("MODIFICATION_DATE", kbsArticleDO.getModificationDate() == null ? null : getLongData(kbsArticleDO.getModificationDate()));
        hashMap.put("INVALID_REASON", kbsArticleDO.getInvalidReason());
        request.setMap(hashMap);
        return request;
    }

    public DataRequest yhzxXnzzNsrCompriseDataRequest(YhzxXnzzNsr yhzxXnzzNsr, CryptSimple cryptSimple, CryptBase36 cryptBase36) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.YHZX_XNZZ_NSR.getTableName());
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("ID", yhzxXnzzNsr.getId());
        hashMap.put("XNZZ_ID", yhzxXnzzNsr.getXnzzId());
        hashMap.put("BM_ID", yhzxXnzzNsr.getBmId());
        hashMap.put("CORPID", yhzxXnzzNsr.getCorpid());
        hashMap.put("NSRSBH", yhzxXnzzNsr.getNsrsbh());
        hashMap.put("ZDSYHBJ", yhzxXnzzNsr.getZdsyhbj());
        hashMap.put("JHBJ", yhzxXnzzNsr.getJhbj());
        hashMap.put("YXCYS", yhzxXnzzNsr.getYxcys());
        hashMap.put("CJSJ", yhzxXnzzNsr.getCjsj() == null ? null : getLongData(yhzxXnzzNsr.getCjsj()));
        hashMap.put("CJR", yhzxXnzzNsr.getCjr());
        hashMap.put("XGSJ", yhzxXnzzNsr.getXgsj() == null ? null : getLongData(yhzxXnzzNsr.getXgsj()));
        hashMap.put("XGR", yhzxXnzzNsr.getXgr());
        hashMap.put("NSRMC",yhzxXnzzNsr.getNsrmc());
        hashMap.put("SHXYDM", yhzxXnzzNsr.getShxydm());
        if (StringUtils.equals(env, "prod")) {
            hashMap.put("NSRMC", DecodeUtils.decodeCryptSimpleProd(yhzxXnzzNsr.getNsrmc(), cryptSimple));
            hashMap.put("SHXYDM", DecodeUtils.deodeCryptBase36Prod(yhzxXnzzNsr.getShxydm(), cryptBase36));
        } else {
            hashMap.put("NSRMC", DecodeUtils.decodeCryptSimpleTest(yhzxXnzzNsr.getNsrmc(), cryptSimple));
            hashMap.put("SHXYDM", DecodeUtils.deodeCryptBase36Test(yhzxXnzzNsr.getShxydm(), cryptBase36));
        }

        hashMap.put("NSRJC", yhzxXnzzNsr.getNsrjc());
        hashMap.put("SHXYDM6", yhzxXnzzNsr.getShxydm6());
        hashMap.put("SJSHXYDM", yhzxXnzzNsr.getSjshxydm());
        hashMap.put("ZGGSJG_DM", yhzxXnzzNsr.getZggsjgDm());
        hashMap.put("ZGDSJG_DM", yhzxXnzzNsr.getZgdsjgDm());
        hashMap.put("DJZCLX_DM", yhzxXnzzNsr.getDjzclxDm());
        hashMap.put("PARENT_DJZCLX_DM", yhzxXnzzNsr.getParentDjzclxDm());
        hashMap.put("HY_DM", yhzxXnzzNsr.getHyDm());
        hashMap.put("NSRZT_DM", yhzxXnzzNsr.getNsrztDm());
        hashMap.put("ZZZCBJ", yhzxXnzzNsr.getZzzcbj());
        hashMap.put("KHYH", yhzxXnzzNsr.getKhyh());
        hashMap.put("YHZH", yhzxXnzzNsr.getYhzh());
        hashMap.put("ZCDZ", yhzxXnzzNsr.getZcdz());
        hashMap.put("ZCDLXDH", yhzxXnzzNsr.getZcdlxdh());
        hashMap.put("SCJYDZ", yhzxXnzzNsr.getScjydz());
        hashMap.put("SCJYDLXDH", yhzxXnzzNsr.getScjydlxdh());
        hashMap.put("QYSDSZSBJ", yhzxXnzzNsr.getQysdszsbj());
        hashMap.put("DJXH", yhzxXnzzNsr.getDjxh());
        hashMap.put("ZZSNSLX", yhzxXnzzNsr.getZzsnslx());
        hashMap.put("DZXWQY_BJ", yhzxXnzzNsr.getDzxwqyBj());
        hashMap.put("XXWLQY_BJ", yhzxXnzzNsr.getXxwlqyBj());
        hashMap.put("NSXYDJ", yhzxXnzzNsr.getNsxydj());
        request.setMap(hashMap);
        return request;
    }
    public DataRequest yhzxXnzzTpcQyCompriseDataRequest(YhzxXnzzTpcQy yhzxXnzzTpcQy, CryptSimple cryptSimple, CryptBase36 cryptBase36) {
        DataRequest request = new DataRequest();
        request.setCatalogType(SystemConstants.DEFAULT_TYPE);
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("ID",yhzxXnzzTpcQy.getId());
        hashMap.put("XNZZ_ID",yhzxXnzzTpcQy.getXnzzId());
        hashMap.put("NSR_ID",yhzxXnzzTpcQy.getNsrId());
        hashMap.put("DJXH",yhzxXnzzTpcQy.getDjxh());
        hashMap.put("NSRSBH",yhzxXnzzTpcQy.getNsrsbh());
//        hashMap.put("SHXYDM",yhzxXnzzTpcQy.getShxydm());
//        hashMap.put("NSRMC",yhzxXnzzTpcQy.getNsrmc());
        if (StringUtils.equals(env, "prod")) {
            hashMap.put("JDXZMC", DecodeUtils.decodeCryptSimpleProd(yhzxXnzzTpcQy.getJdxzmc(), cryptSimple));
            hashMap.put("ZCDZ", DecodeUtils.decodeCryptSimpleProd(yhzxXnzzTpcQy.getZcdz(), cryptSimple));
            hashMap.put("SCJYDZ",DecodeUtils.decodeCryptSimpleProd(yhzxXnzzTpcQy.getScjydz(), cryptSimple));
            hashMap.put("NSRMC", DecodeUtils.decodeCryptSimpleProd(yhzxXnzzTpcQy.getNsrmc(), cryptSimple));
            hashMap.put("SHXYDM", DecodeUtils.deodeCryptBase36Prod(yhzxXnzzTpcQy.getShxydm(), cryptBase36));
        } else {
            hashMap.put("JDXZMC", DecodeUtils.decodeCryptSimpleTest(yhzxXnzzTpcQy.getJdxzmc(), cryptSimple));
            hashMap.put("ZCDZ", DecodeUtils.decodeCryptSimpleTest(yhzxXnzzTpcQy.getZcdz(), cryptSimple));
            hashMap.put("SCJYDZ" DecodeUtils.decodeCryptSimpleTest(yhzxXnzzTpcQy.getScjydz(), cryptSimple));
            hashMap.put("NSRMC", DecodeUtils.decodeCryptSimpleTest(yhzxXnzzTpcQy.getNsrmc(), cryptSimple));
            hashMap.put("SHXYDM", DecodeUtils.deodeCryptBase36Test(yhzxXnzzTpcQy.getShxydm(), cryptBase36));
        }
        hashMap.put("ZZHM",yhzxXnzzTpcQy.getZzhm());
        hashMap.put("JGBM_DM",yhzxXnzzTpcQy.getJgbmDm());
        hashMap.put("ZGSWSKFJ_DM",yhzxXnzzTpcQy.getZgswskfjDm());
        hashMap.put("NSRZT_DM",yhzxXnzzTpcQy.getNsrztDm());
        hashMap.put("DJZCLX_DM",yhzxXnzzTpcQy.getDjzclxDm());
        hashMap.put("HY_DM",yhzxXnzzTpcQy.getHyDm());
        hashMap.put("KZZTDJLX_DM",yhzxXnzzTpcQy.getKzztdjlxDm());
        hashMap.put("DWLSGX_DM",yhzxXnzzTpcQy.getDwlsgxDm());
        hashMap.put("KJZDZZ_DM",yhzxXnzzTpcQy.getKjzdzzDm());
        hashMap.put("XZQHSZ_DM",yhzxXnzzTpcQy.getXzqhszDm());
        hashMap.put("JYFW",yhzxXnzzTpcQy.getJyfw());
        hashMap.put("KYSLRQ",yhzxXnzzTpcQy.getKyslrq());
        hashMap.put("CYRS",yhzxXnzzTpcQy.getCyrs());
        hashMap.put("ZDSYHJKJC_DM",yhzxXnzzTpcQy.getZdsyhjkjcDm());
        hashMap.put("YBNSRRDBJ",yhzxXnzzTpcQy.getYbnsrrdbj());
        hashMap.put("ZZSNSLX",yhzxXnzzTpcQy.getZzsnslx());
        hashMap.put("DZXWQY_BJ",yhzxXnzzTpcQy.getDzxwqyBj());
        hashMap.put("XXWLQY_BJ",yhzxXnzzTpcQy.getXxwlqyBj());
        hashMap.put("NSXYDJ",yhzxXnzzTpcQy.getNsxydj());
        hashMap.put("IS_DELETED",yhzxXnzzTpcQy.getIsDeleted()==true?1:0);
        hashMap.put("CJR",yhzxXnzzTpcQy.getCjr());
        hashMap.put("CJSJ",yhzxXnzzTpcQy.getCjsj() == null ? null : getLongData(yhzxXnzzTpcQy.getCjsj()));
        hashMap.put("XGR",yhzxXnzzTpcQy.getXgr());
        hashMap.put("XGSJ",yhzxXnzzTpcQy.getXgsj() == null ? null : getLongData(yhzxXnzzTpcQy.getXgsj()));
        request.setMap(hashMap);
        return request;
    }


    public DataRequest botOutoundTaskDetailCompriseDataRequest(BotOutoundTaskDetailWithBLOBs botOutoundTaskDetail, CryptSimple cryptSimple, CryptNumber cryptNumber) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.BOT_OUTBOUND_TASK_DETAIL.getTableName());
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("ID", botOutoundTaskDetail.getId());
        hashMap.put("NODE_ID", botOutoundTaskDetail.getNodeId());
        hashMap.put("ORG_ID", botOutoundTaskDetail.getOrgId());
        hashMap.put("TASK_ID", botOutoundTaskDetail.getTaskId());
        hashMap.put("OUTBOUND_MESSAGE_ID", botOutoundTaskDetail.getMessageId());
        hashMap.put("DIALOGUE_ID", botOutoundTaskDetail.getDialogueId());
        hashMap.put("RECEIVER", botOutoundTaskDetail.getReceiver());
        //hashMap.put("RECEIVER_MOBILE", botOutoundTaskDetail.getReceiverMobile());
        //        hashMap.put("CONTENT", botOutoundTaskDetail.getContent());
        //        hashMap.put("CALL_RECORD", botOutoundTaskDetail.getCallRecord());
        //        hashMap.put("CALL_RECORD_URL", botOutoundTaskDetail.getCallRecordUrl());
        //        hashMap.put("FEEDBACK_CONTENT", botOutoundTaskDetail.getFeedbackContent());
        hashMap.put("ANSWER_TIME",botOutoundTaskDetail.getAnswerTime()== null ? null : getLongData(botOutoundTaskDetail.getAnswerTime()));
        //        hashMap.put("HANGUP_TIME", botOutoundTaskDetail.getHangupTime()== null ? null : getLongData(botOutoundTaskDetail.getHangupTime()));
        //        hashMap.put("DURATION", botOutoundTaskDetail.getDuration());
        //        hashMap.put("REMARK", botOutoundTaskDetail.getRemark());
        //        hashMap.put("CREATOR", botOutoundTaskDetail.getCreator());
        //        hashMap.put("MODIFIER", botOutoundTaskDetail.getModifier());
        hashMap.put("CALL_TIME", botOutoundTaskDetail.getCallTime()== null ? null : getLongData(botOutoundTaskDetail.getCallTime()));

        hashMap.put("OUTBOUND_STATUS", botOutoundTaskDetail.getStatus());
        hashMap.put("FEEDBACK_STATUS", botOutoundTaskDetail.getFeedbackStatus());
        if (StringUtils.equals(env, "prod")) {
            hashMap.put("QYMC", DecodeUtils.decodeCryptSimpleProd(botOutoundTaskDetail.getQymc(), cryptSimple));
            hashMap.put("RECEIVER_MOBILE", DecodeUtils.deodeCryptNumberProd(botOutoundTaskDetail.getReceiverMobile(), cryptNumber));
        } else {
            hashMap.put("QYMC", DecodeUtils.decodeCryptSimpleTest(botOutoundTaskDetail.getQymc(), cryptSimple));
            hashMap.put("RECEIVER_MOBILE", DecodeUtils.decodeCryptNumberTest(botOutoundTaskDetail.getReceiverMobile(), cryptNumber));
        }
        //hashMap.put("QYMC", botOutoundTaskDetail.getQymc());
        hashMap.put("BOT_OUTBOUND_TASK_DETAIL_SHXYDM", botOutoundTaskDetail.getShxydm());
        hashMap.put("SWSMC", botOutoundTaskDetail.getSwsmc());
        if(botOutoundTaskDetail.getSgymc()!=null) {
            List<String> sgymcs = GsonUtil.getBean(botOutoundTaskDetail.getSgymc(), new TypeToken<List<String>>() {
            }.getType());
            hashMap.put("SGYMC", sgymcs);
        }
        if(botOutoundTaskDetail.getExcelColumn()!=null) {
            List<String> excelColumn = GsonUtil.getBean(botOutoundTaskDetail.getExcelColumn(), new TypeToken<List<String>>() {
            }.getType());
            hashMap.put("EXCEL_COLUMN", excelColumn);
        }

        hashMap.put("CREATION_DATE", botOutoundTaskDetail.getCreationDate()== null ? null : getLongData(botOutoundTaskDetail.getCreationDate()));

        hashMap.put("MODIFICATION_DATE", botOutoundTaskDetail.getModificationDate()== null ? null : getLongData(botOutoundTaskDetail.getModificationDate()));
        request.setMap(hashMap);
        return request;
    }

    public DataRequest yhzxxnzzcyCompriseDataRequest(YhzxxnzzcyDO yhzxxnzzcyDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType("yhzx_xnzz_cy");
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("id", yhzxxnzzcyDO.getId());
        hashMap.put("xnzz_id", yhzxxnzzcyDO.getXnzzId());
        hashMap.put("bm_id", yhzxxnzzcyDO.getBmId());
        hashMap.put("nsr_id", yhzxxnzzcyDO.getNsrId());
        hashMap.put("dingid", yhzxxnzzcyDO.getDingid());
        hashMap.put("userid", yhzxxnzzcyDO.getUserid());
        hashMap.put("XM", yhzxxnzzcyDO.getXm());
        hashMap.put("SJHM", yhzxxnzzcyDO.getSjhm());
        hashMap.put("rylx_dm", yhzxxnzzcyDO.getRylxDm());
        hashMap.put("cysx", yhzxxnzzcyDO.getCysx());
        hashMap.put("glybj", yhzxxnzzcyDO.getGlybj());
        hashMap.put("zbrybj", yhzxxnzzcyDO.getZbrybj());
        hashMap.put("bz", yhzxxnzzcyDO.getBz());
        hashMap.put("cyzt", yhzxxnzzcyDO.getCyzt());
        hashMap.put("dxzt", yhzxxnzzcyDO.getDxzt());
        hashMap.put("dxfssj", yhzxxnzzcyDO.getDxfssj() == null ? null : getLongData(yhzxxnzzcyDO.getDxfssj()));
        hashMap.put("dxcwxx", yhzxxnzzcyDO.getDxcwxx());
        hashMap.put("cjsj", yhzxxnzzcyDO.getCjsj() == null ? null : getLongData(yhzxxnzzcyDO.getCjsj()));
        hashMap.put("cjr", yhzxxnzzcyDO.getCjr());
        hashMap.put("xgsj", yhzxxnzzcyDO.getXgsj() == null ? null : getLongData(yhzxxnzzcyDO.getXgsj()));
        hashMap.put("xgr", yhzxxnzzcyDO.getXgr());
        hashMap.put("ZW", yhzxxnzzcyDO.getZw());
        hashMap.put("zcfsbj", yhzxxnzzcyDO.getZcfsbj());
        hashMap.put("zcrzjb", yhzxxnzzcyDO.getZcrzjb());
        hashMap.put("qyglybj", yhzxxnzzcyDO.getQyglybj());
        hashMap.put("xssx", yhzxxnzzcyDO.getXssx());
        hashMap.put("zjlx", yhzxxnzzcyDO.getZjlx());
        hashMap.put("zjhm", yhzxxnzzcyDO.getZjhm());
        hashMap.put("gpy_id", yhzxxnzzcyDO.getGpyId());
        hashMap.put("jhsj", yhzxxnzzcyDO.getJhsj() == null ? null : getLongData(yhzxxnzzcyDO.getJhsj()));
        request.setMap(hashMap);
        return request;
    }

    public DataRequest botUnawareDetailCompriseDataRequest(BotUnawareDetailDO botUnawareDetailDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.BOT_UNAWARE_DETAIL.getTableName());
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", botUnawareDetailDO.getId());
        hashMap.put("NODE_ID", botUnawareDetailDO.getNodeId());
        hashMap.put("SERVER_ID", botUnawareDetailDO.getServerId());
        hashMap.put("SERVER_NAME", botUnawareDetailDO.getServerName());
        hashMap.put("USER_ID", botUnawareDetailDO.getUserId());
        hashMap.put("DEPT_ID", botUnawareDetailDO.getDeptId());
        hashMap.put("REQUEST_ID", botUnawareDetailDO.getRequestId());
        hashMap.put("AUTH_VENDOR", botUnawareDetailDO.getAuthVendor());
        hashMap.put("MESSAGE_ID", botUnawareDetailDO.getMessageId());
        hashMap.put("UTTERANCE", botUnawareDetailDO.getUtterance());
        hashMap.put("BOT_UNAWARE_DETAIL_TYPE", botUnawareDetailDO.getType());
        hashMap.put("SATISFACTION_LEVEL", botUnawareDetailDO.getSatisfactionLevel());
        hashMap.put("CATEGORY", botUnawareDetailDO.getCategory());
        hashMap.put("PRIORITY", botUnawareDetailDO.getPriority());
        hashMap.put("CLUSTER_ID", botUnawareDetailDO.getClusterId());
        hashMap.put("TAGS", botUnawareDetailDO.getTags());
        hashMap.put("REASON", botUnawareDetailDO.getReason());
        hashMap.put("STATUS", botUnawareDetailDO.getStatus());
        hashMap.put("HANDLER", botUnawareDetailDO.getHandler());
        hashMap.put("HANDLE_DATE", botUnawareDetailDO.getHandleDate() == null ? null : getLongData(botUnawareDetailDO.getHandleDate()));
        hashMap.put("REMARK", botUnawareDetailDO.getRemark());
        hashMap.put("CREATION_DATE", getLongData(botUnawareDetailDO.getCreationDate()));
        hashMap.put("TRACK_CODE", botUnawareDetailDO.getTrackCode());
        hashMap.put("MODIFICATION_DATE", botUnawareDetailDO.getModificationDate() == null ? null : getLongData(botUnawareDetailDO.getModificationDate()));
        request.setMap(hashMap);
        return request;
    }

    public DataRequest botTrackDetailCompriseDataRequest(BotTrackDetailDO botTrackDetailDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.BOT_TRACK_DETAIL.getTableName());
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("ID", botTrackDetailDO.getId());
        hashMap.put("NODE_ID", botTrackDetailDO.getNodeId());
        hashMap.put("USER_ID", botTrackDetailDO.getUserId());
        hashMap.put("USER_NAME", botTrackDetailDO.getUserName());
        hashMap.put("SESSION_ID", botTrackDetailDO.getSessionId());
        hashMap.put("REQUEST_ID", botTrackDetailDO.getRequestId());
        hashMap.put("MESSAGE_ID", botTrackDetailDO.getMessageId());
        hashMap.put("SERVER_ID", botTrackDetailDO.getServerId());
        hashMap.put("SERVER_NAME", botTrackDetailDO.getServerName());
        hashMap.put("AUTH_VENDOR", botTrackDetailDO.getAuthVendor());
        hashMap.put("TRACK_CODE", botTrackDetailDO.getTrackCode());
        hashMap.put("TRACK_DATA", botTrackDetailDO.getTrackData());
        hashMap.put("REMARK", botTrackDetailDO.getRemark());
        hashMap.put("CREATION_DATE", getLongData(botTrackDetailDO.getCreationDate()));
        request.setMap(hashMap);
        return request;
    }

    public DataRequest botChatResponseMessageCompriseDatarequest(BotChatResponseMessageDO botChatResponseMessageDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.BOT_CHAT_RESPONSE_MESSAGE.getTableName());
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", botChatResponseMessageDO.getId());
        hashMap.put("NODE_ID", botChatResponseMessageDO.getNodeId());
        hashMap.put("REQUEST_ID", botChatResponseMessageDO.getRequestId());
        hashMap.put("RESPONSE_ID", botChatResponseMessageDO.getResponseId());
        hashMap.put("USER_ID", botChatResponseMessageDO.getUserId());
        hashMap.put("MESSAGE_TIP", botChatResponseMessageDO.getMessageTip());
        hashMap.put("ARRAY_INDEX", botChatResponseMessageDO.getArrayIndex());
        hashMap.put("SUB_INDEX", botChatResponseMessageDO.getSubIndex());
        hashMap.put("BotChatResponseMessage_TYPE", botChatResponseMessageDO.getType());
        hashMap.put("TITLE", botChatResponseMessageDO.getTitle());
        hashMap.put("SUMMARY", botChatResponseMessageDO.getSummary());
        hashMap.put("CONTENT", botChatResponseMessageDO.getContent());
        hashMap.put("KNOWLEDGE_ID", botChatResponseMessageDO.getKnowledgeId());
        hashMap.put("ANSWER_SOURCE", botChatResponseMessageDO.getAnswerSource());
        hashMap.put("SELECTED", botChatResponseMessageDO.getSelected());
        hashMap.put("CLICKED_TIME", botChatResponseMessageDO.getClickedTime() == null ? null : getLongData(botChatResponseMessageDO.getClickedTime()));
        hashMap.put("CREATION_DATE", botChatResponseMessageDO.getCreationDate() == null ? null : getLongData(botChatResponseMessageDO.getCreationDate()));
        hashMap.put("DURATION", botChatResponseMessageDO.getDuration());
        request.setMap(hashMap);
        return request;
    }

    public DataRequest kbsQuestionArticleCompriseDataRequest(KbsQuestionArticleDO kbsQuestionArticleDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.KBS_QUESTION_ARTICLE.getTableName());
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", kbsQuestionArticleDO.getId());
        hashMap.put("QUESTION_ID", kbsQuestionArticleDO.getQuestionId());
        hashMap.put("ARTICLE_ID", kbsQuestionArticleDO.getArticleId());
        hashMap.put("ARTICLE_TYPE", kbsQuestionArticleDO.getArticleType());
        hashMap.put("ARTICLE_TITLE", kbsQuestionArticleDO.getArticleTitle());
        hashMap.put("ARTICLE_BLOCK", kbsQuestionArticleDO.getArticleBlock());
        hashMap.put("REMARK", kbsQuestionArticleDO.getRemark());
        hashMap.put("PRIORITY", kbsQuestionArticleDO.getPriority());
        hashMap.put("KBS_QUESTION_ARTICLE_STATUS", kbsQuestionArticleDO.getStatus());
        hashMap.put("MAP_SOURCE", kbsQuestionArticleDO.getMapSource());
        hashMap.put("MAP_ID", kbsQuestionArticleDO.getMapId());
        hashMap.put("SYNC_FLAG", kbsQuestionArticleDO.getSyncFlag());
        hashMap.put("SYNC_TIME", kbsQuestionArticleDO.getSyncTime() == null ? null : getLongData(kbsQuestionArticleDO.getSyncTime()));
        hashMap.put("CREATOR", kbsQuestionArticleDO.getCreator());
        hashMap.put("CREATION_DATE", kbsQuestionArticleDO.getCreationDate() == null ? null : getLongData(kbsQuestionArticleDO.getCreationDate()));
        hashMap.put("MODIFIER", kbsQuestionArticleDO.getModifier());
        hashMap.put("MODIFICATION_DATE", kbsQuestionArticleDO.getModificationDate() == null ? null : getLongData(kbsQuestionArticleDO.getModificationDate()));
        request.setMap(hashMap);
        return request;
    }

    public DataRequest botBizDataCompriseDataRequest(BizData bizData) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.BOT_BIZ_DATA.getTableName());
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", bizData.getId());
        hashMap.put("NODE_ID", bizData.getNodeId());
        hashMap.put("ORG_ID", bizData.getOrgId());
        hashMap.put("DEFINE_ID", bizData.getDefineId());
        hashMap.put("LATITUDE", bizData.getLatitude());
        hashMap.put("LONGITUDE", bizData.getLongitude());
        hashMap.put("ID1", bizData.getId1());
        hashMap.put("ID2", bizData.getId2());
        hashMap.put("ID3", bizData.getId3());
        hashMap.put("DATA1", bizData.getData1());
        hashMap.put("DATA2", bizData.getData2());
        hashMap.put("DATA3", bizData.getData3());
        hashMap.put("DATA4", bizData.getData4());
        hashMap.put("DATA4_NO_INDEX1",bizData.getData4());
        hashMap.put("DATA5", bizData.getData5());
        hashMap.put("BOT_BIZ_DATA_STATUS", bizData.getStatus());
        hashMap.put("CREATOR", bizData.getCreator());
        hashMap.put("CREATION_DATE", bizData.getCreationDate() == null ? null : getLongData(bizData.getCreationDate()));
        hashMap.put("MODIFIER", bizData.getModifier());
        hashMap.put("MODIFICATION_DATE", bizData.getModificationDate() == null ? null : getLongData(bizData.getModificationDate()));
        request.setMap(hashMap);
        return request;
    }

    public DataRequest kbsQuestionCompriseDataRequest(KbsQuestionDO kbsQuestionDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType(SystemConstants.DEFAULT_TYPE);
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", kbsQuestionDO.getId());
        hashMap.put("QUESTION", kbsQuestionDO.getQuestion());
        hashMap.put("GRADE", kbsQuestionDO.getGrade());
        hashMap.put("CATEGORY", kbsQuestionDO.getCategory());
        hashMap.put("PRIMARY_KEYWORD", kbsQuestionDO.getPrimaryKeyword());
        hashMap.put("ALTERNATE_KEYWORD", kbsQuestionDO.getAlternateKeyword());
        hashMap.put("SEARCH_KEYWORD", kbsQuestionDO.getSearchKeyword());
        hashMap.put("KEYWORD_OPTION", kbsQuestionDO.getKeywordOption());
        hashMap.put("SYNONYMS", kbsQuestionDO.getSynonyms());
        hashMap.put("ANSWER", kbsQuestionDO.getAnswer());
        hashMap.put("REMARK", kbsQuestionDO.getRemark());
        hashMap.put("STATUS", kbsQuestionDO.getStatus());
        hashMap.put("VALID_DATE", kbsQuestionDO.getValidDate() == null ? null : getLongData(kbsQuestionDO.getValidDate()));
        hashMap.put("INVALID_DATE", kbsQuestionDO.getInvalidDate() == null ? null : getLongData(kbsQuestionDO.getInvalidDate()));
        hashMap.put("INVALID_REASON", kbsQuestionDO.getInvalidReason());
        hashMap.put("ADD_MODE", kbsQuestionDO.getAddMode());
        hashMap.put("ORIGINAL", kbsQuestionDO.getOriginal());
        hashMap.put("MAP_SOURCE", kbsQuestionDO.getMapSource());
        hashMap.put("MAP_ID", kbsQuestionDO.getMapId());
        hashMap.put("SYNC_FLAG", kbsQuestionDO.getSyncFlag());
        hashMap.put("SYNC_TIME", kbsQuestionDO.getSyncTime() == null ? null : getLongData(kbsQuestionDO.getSyncTime()));
        hashMap.put("CREATOR", kbsQuestionDO.getCreator());
        hashMap.put("CREATION_DATE", kbsQuestionDO.getCreationDate() == null ? null : getLongData(kbsQuestionDO.getCreationDate()));
        hashMap.put("MODIFIER", kbsQuestionDO.getModifier());
        hashMap.put("MODIFICATION_DATE", kbsQuestionDO.getModificationDate() == null ? null : getLongData(kbsQuestionDO.getModificationDate()));
        hashMap.put("QUESTION_TYPE", kbsQuestionDO.getQuestionType());
        hashMap.put("APPROVAL_STATUS", kbsQuestionDO.getApprovalStatus());
        request.setMap(hashMap);
        return request;
    }

    public static DataRequest zxArticleCompriseDataRequest(ZxArticle zxArticle) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.ZX_ARTICLE.getTableName());
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", zxArticle.getId());
        hashMap.put("USER_ID", zxArticle.getUserId());
        hashMap.put("NODE_ID", zxArticle.getNodeId());
        hashMap.put("MAP_SOURCE", zxArticle.getMapSource());
        hashMap.put("TITLE", zxArticle.getTitle());
        hashMap.put("AUTHOR", zxArticle.getAuthor());
        hashMap.put("SORT_ID", zxArticle.getSortId());
        hashMap.put("KEY_WORD", zxArticle.getKeyWord());
        hashMap.put("PUBLISH_TYPE", zxArticle.getPublishType());
        hashMap.put("COVER_TYPE", zxArticle.getCoverType());
        hashMap.put("CONTENT", zxArticle.getContent());
        hashMap.put("CONTENT_HTML", MyHttpRequest.sendGetSSL(zxArticle.getContent(), null, "utf-8"));
        hashMap.put("ALLOW", zxArticle.getAllow());
        hashMap.put("FREE_STAT", zxArticle.getFreeStat());
        hashMap.put("PUBLISH_DATE", zxArticle.getPublishDate() == null ? null : getLongData(zxArticle.getPublishDate()));
        hashMap.put("ENABLED", zxArticle.getEnabled());
        hashMap.put("CREATION_DATE", zxArticle.getCreationDate() == null ? null : getLongData(zxArticle.getCreationDate()));
        hashMap.put("MODIFICATION_DATE", zxArticle.getModificationDate() == null ? null : getLongData(zxArticle.getModificationDate()));
        hashMap.put("REVISION", zxArticle.getRevision());
        request.setMap(hashMap);
        return request;
    }

    public static DataRequest kbsTagsCompriseDataRequest(Long id, LinkedList<String> tags) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.KBS_QUESTION.getTableName());
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("ID", id);
        hashMap.put("TAGS", tags);
        request.setMap(hashMap);
        return request;
    }

    public DataRequest kbsKeywordCompriseDataRequest(KbsKeywordDO kbsKeywordDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.KBS_KEYWORD.getTableName());
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", kbsKeywordDO.getId());
        hashMap.put("ORG_ID", kbsKeywordDO.getOrgId());
        hashMap.put("keywordName", kbsKeywordDO.getKeywordName());
        hashMap.put("keywordPinyin", kbsKeywordDO.getKeywordPinyin());
        hashMap.put("abbrev", kbsKeywordDO.getAbbrev());
        hashMap.put("synonyms", kbsKeywordDO.getSynonyms());
        if (StringUtils.isNotBlank(kbsKeywordDO.getSynonyms())) {
            String[] synonymsList = kbsKeywordDO.getSynonyms().split("\\|");
            hashMap.put("synonym_list", synonymsList);
        } else {
            hashMap.put("synonym_list", kbsKeywordDO.getSynonyms());
        }
        hashMap.put("near", kbsKeywordDO.getNear());
        hashMap.put("typos", kbsKeywordDO.getTypos());
        hashMap.put("hypernym", kbsKeywordDO.getHypernym());
        hashMap.put("hyponym", kbsKeywordDO.getHyponym());
        hashMap.put("related", kbsKeywordDO.getRelated());
        hashMap.put("domain", kbsKeywordDO.getDomain());
        hashMap.put("KBS_KEYWORD_type", kbsKeywordDO.getType());
        hashMap.put("definition", kbsKeywordDO.getDefinition());
        hashMap.put("score", kbsKeywordDO.getScore());
        hashMap.put("weight", kbsKeywordDO.getWeight());
        hashMap.put("sensitivity", kbsKeywordDO.getSensitivity());
        hashMap.put("popularity", kbsKeywordDO.getPopularity());
        hashMap.put("enabled", kbsKeywordDO.getEnabled());
        hashMap.put("creationDate", kbsKeywordDO.getCreationDate() == null ? null : getLongData(kbsKeywordDO.getCreationDate()));
        hashMap.put("creator", kbsKeywordDO.getCreator());
        hashMap.put("modificationDate", kbsKeywordDO.getModificationDate() == null ? null : getLongData(kbsKeywordDO.getModificationDate()));
        hashMap.put("modifier", kbsKeywordDO.getModifier());
        request.setMap(hashMap);
        return request;
    }

    public DataRequest botConfigServerCompriseDataRequest(BotConfigServer botConfigServer) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.BOT_CONFIG_SERVER.getTableName());
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", botConfigServer.getId());
        hashMap.put("BOT_TYPE", botConfigServer.getType());
        hashMap.put("KBS_TYPE_STRING", botConfigServer.getKbsType());
        hashMap.put("NAME", botConfigServer.getName());
        hashMap.put("SHORT_NAME", botConfigServer.getShortName());
        hashMap.put("PARENT_ID", botConfigServer.getParentId());
        hashMap.put("DEFAULT_NODE_ID", botConfigServer.getDefaultNodeId());
        hashMap.put("ORG_ID", botConfigServer.getOrgId());
        hashMap.put("ALLOW_GUEST", botConfigServer.getAllowGuest());
        hashMap.put("AUTH_VENDOR", botConfigServer.getAuthVendor());
        hashMap.put("APP_ID", botConfigServer.getAppId());
        hashMap.put("APP_KEY", botConfigServer.getAppKey());
        hashMap.put("APP_SECRET", botConfigServer.getAppSecret());
        hashMap.put("ACCESS_TOKEN", botConfigServer.getAccessToken());
        hashMap.put("BASE_URL", botConfigServer.getBaseUrl());
        hashMap.put("AUTH_URL", botConfigServer.getAuthUrl());
        hashMap.put("ENABLED_FORECAST", botConfigServer.getEnabledForecast());
        hashMap.put("MESSAGE_LEVEL", botConfigServer.getLevel());
        hashMap.put("RECOMMEND_TIPS", botConfigServer.getRecommendTips());
        hashMap.put("KNOWLEDGE_TIPS", botConfigServer.getKnowledgeTips());
        hashMap.put("REMARK", botConfigServer.getRemark());
        hashMap.put("VISIBLE", botConfigServer.getVisible());
        hashMap.put("ENABLED", botConfigServer.getEnabled());
        hashMap.put("CREATION_DATE", botConfigServer.getCreationDate() == null ? null : getLongData(botConfigServer.getCreationDate()));
        hashMap.put("CREATOR", botConfigServer.getCreator());
        hashMap.put("MODIFICATION_DATE", botConfigServer.getModificationDate() == null ? null : getLongData(botConfigServer.getModificationDate()));
        hashMap.put("MODIFIER", botConfigServer.getModifier());
        hashMap.put("REVISION", botConfigServer.getRevision());
        request.setMap(hashMap);
        return request;
    }

    public DataRequest kbsReadingCompriseDataRequest(KbsReadingDOWithBLOBs kbsReadingDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.KBS_READING.getTableName());
        Map<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("ID", kbsReadingDO.getId());
        hashMap.put("KEYWORD", kbsReadingDO.getKeyword());
        hashMap.put("SYNONYMS", kbsReadingDO.getSynonyms());
        hashMap.put("TITLE", kbsReadingDO.getTitle());
        hashMap.put("CONTENT", kbsReadingDO.getContent());
        hashMap.put("CONTENT_TEXT", kbsReadingDO.getContentText());
        hashMap.put("CONTENT_HTML", kbsReadingDO.getContentHtml());
        hashMap.put("REMARK", kbsReadingDO.getRemark());
        hashMap.put("KBS_READING_STATUS", kbsReadingDO.getStatus());
        hashMap.put("ORIGINAL", kbsReadingDO.getOriginal());
        hashMap.put("CREATOR", kbsReadingDO.getCreator());
        hashMap.put("CREATION_DATE", kbsReadingDO.getCreationDate() == null ? null : getLongData(kbsReadingDO.getCreationDate()));
        hashMap.put("MODIFIER", kbsReadingDO.getModifier());
        hashMap.put("MODIFICATION_DATE", kbsReadingDO.getModificationDate() == null ? null : getLongData(kbsReadingDO.getModificationDate()));
        request.setMap(hashMap);
        return request;
    }

    public DataRequest botMediaCompriseDataRequest(BotMediaDO botMediaDO) {
        DataRequest request = new DataRequest();
        request.setCatalogType(TablesEnum.BOT_MEDIA.getTableName());
        Map<String, Object> hashMap = new HashMap<>(32);
        hashMap.put("ID", botMediaDO.getId());
        hashMap.put("NODE_ID", botMediaDO.getNodeId());
        hashMap.put("REQUEST_ID", botMediaDO.getRequestId());
        hashMap.put("RESPONSE_MESSAGE_ID", botMediaDO.getResponseMessageId());
        hashMap.put("ORG_ID", botMediaDO.getOrgId());
        hashMap.put("SERVER_ID", botMediaDO.getServerId());
        hashMap.put("FILE_NAME", botMediaDO.getFileName());
        hashMap.put("FILE_TYPE", botMediaDO.getFileType());
        hashMap.put("FILE_SIZE", botMediaDO.getFileSize());
        hashMap.put("RECOGNITION_ID", botMediaDO.getRecognitionId());
        hashMap.put("RECOGNITION_TEXT", decrypt(botMediaDO.getRecognitionText()));
        hashMap.put("CORRECTION_TEXT", decrypt(botMediaDO.getCorrectionText()));
        hashMap.put("CORRECTION_DATE", botMediaDO.getCorrectionDate() == null ? null : getLongData(botMediaDO.getCorrectionDate()));
        hashMap.put("OSS_KEY", botMediaDO.getOssKey());
        hashMap.put("MEDIA_ID", botMediaDO.getMediaId());
        hashMap.put("MEDIA_SOURCE", botMediaDO.getMediaSource());
        hashMap.put("MEDIA_STATUS", botMediaDO.getStatus());
        hashMap.put("MEDIA_TYPE", botMediaDO.getMediaType());
        hashMap.put("DURATION", botMediaDO.getDuration());
        hashMap.put("CREATION_DATE", botMediaDO.getCreationDate() == null ? null : getLongData(botMediaDO.getCreationDate()));
        request.setMap(hashMap);
        return request;
    }

    public String transportData(String lastUpdateTime) {
        try {
            if(StringUtils.isBlank(lastUpdateTime)){
                return "0";
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf2.format(sdf1.parse(lastUpdateTime));
        } catch (Exception ex) {
            return lastUpdateTime;
        }
    }

    private static long getLongData(Date date) {
        if (date == null) {
            return System.currentTimeMillis();
        } else {
            return date.getTime();
        }
    }

    private String decrypt(String text) {
        if (StringUtils.isNotBlank(text)) {
            try {
                return cryptZip.decrypt(text);
            } catch (Exception ex) {
                logger.error(MessageFormat.format("解密失败,text = {0}, ex = {1}", text, ex));
                return null;
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(new CompriseUtils().transportData("Mon Nov 23 14:19:41 CST 2013"));
    }

}
