package com.ifugle.rap.data.task;

import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.bigdata.task.service.BulkTemplateRepository;
import com.ifugle.rap.sqltransform.base.CommonFiledExtractorBase;
import com.ifugle.rap.sqltransform.base.SpecialFiledExtractorBase;
import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.commonfiledextractor.CommonFiledExtractor;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import com.ifugle.rap.sqltransform.entry.SqlEntry;
import com.ifugle.rap.sqltransform.specialfiledextractor.AggregationSpecialFiledExtractor;
import com.ifugle.rap.sqltransform.rule.WhereSqlTransformRule;
import com.ifugle.rap.sync.service.InnerSyncService;
import com.ifugle.rap.sqltransform.rule.GroupBySqlTransformRule;
import com.ifugle.rap.utils.SqlTransformDslUtil;
import com.ifugle.rap.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 时间格式过滤只支持 yyyy-MM-dd HH:mm:ss格式
 * limit 为hint部分查询数据的大小
 * glimit为分组后显示bucket的数量
 * count()请加字段
 *
 * @author Minc
 * @date 2021/12/30 14:20
 */
@Component
public class TestTask {
    @Autowired
    BulkTemplateRepository<Map<String, Object>> esTemplateRepository;

    @Autowired
    InnerSyncService innerSyncService;


    @Scheduled(fixedDelay = 1000L * 60 * 30)
    public void getQuery() {
        test40040();
        test40042();
        test40043();
        test40044();
    }

    private void test40044() {
        try {
            String sql = "select xnzz_id,swjg_dm,count(nsr_id) from bigdata_yhzx_qz_cy where swjgbz in (0,1) and zzsnslx=1 group by xnzz_id,swjg_dm limit 0 glimit 100000";
            String nowDate = TimeUtil.getStringDate(System.currentTimeMillis(), "yyyyMMdd", -1000L * 60 * 60 * 24);
            CommonFiledExtractor commonFiledExtractor = new CommonFiledExtractor(
                    Integer.parseInt(nowDate),
                    Integer.parseInt(nowDate),
                    "40044",
                    "10010",
                    "",
                    "",
                    "",
                    ""
            );
            doInsert(
                    sql,
                    new AggregationSpecialFiledExtractor(
                            (indexDayModel, data) -> {
                                Integer idCount = data.getJSONObject("nsr_id_count").getInteger("value");
                                Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
                                String swjgDm = data.getJSONObject("key").getString("swjg_dm");
                                indexDayModel.setTotalCount(idCount);
                                indexDayModel.setOrgId((long) xnzzId);
                                indexDayModel.setDimData1(swjgDm);
                            }),
                    commonFiledExtractor,
                    new GroupBySqlTransformRule(),
                    new WhereSqlTransformRule()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test40043() {
        try {
            String sql = "select xnzz_id,swjg_dm,count(nsr_id) from bigdata_yhzx_qz_cy where swjgbz in (0,1) group by xnzz_id,swjg_dm limit 0 glimit 100000";
            String nowDate = TimeUtil.getStringDate(System.currentTimeMillis(), "yyyyMMdd", -1000L * 60 * 60 * 24);
            CommonFiledExtractor commonFiledExtractor = new CommonFiledExtractor(
                    Integer.parseInt(nowDate),
                    Integer.parseInt(nowDate),
                    "40043",
                    "10010",
                    "",
                    "",
                    "",
                    ""
            );
            doInsert(
                    sql,
                    new AggregationSpecialFiledExtractor(
                            (indexDayModel, data) -> {
                                Integer idCount = data.getJSONObject("nsr_id_count").getInteger("value");
                                Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
                                String swjgDm = data.getJSONObject("key").getString("swjg_dm");
                                indexDayModel.setTotalCount(idCount);
                                indexDayModel.setOrgId((long) xnzzId);
                                indexDayModel.setDimData1(swjgDm);
                            }),
                    commonFiledExtractor,
                    new GroupBySqlTransformRule(),
                    new WhereSqlTransformRule()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test40042() {
        try {
            String sql = "select xnzz_id,swjg_dm,count(yh_id) from bigdata_yhzx_qz_cy where swjgbz in (0,1) group by xnzz_id,swjg_dm limit 0 glimit 100000";
            String nowDate = TimeUtil.getStringDate(System.currentTimeMillis(), "yyyyMMdd", -1000L * 60 * 60 * 24);
            CommonFiledExtractor commonFiledExtractor = new CommonFiledExtractor(
                    Integer.parseInt(nowDate),
                    Integer.parseInt(nowDate),
                    "40042",
                    "10010",
                    "",
                    "",
                    "",
                    ""
            );
            doInsert(
                    sql,
                    new AggregationSpecialFiledExtractor(
                            (indexDayModel, data) -> {
                                Integer idCount = data.getJSONObject("yh_id_count").getInteger("value");
                                Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
                                String swjgDm = data.getJSONObject("key").getString("swjg_dm");
                                indexDayModel.setTotalCount(idCount);
                                indexDayModel.setOrgId((long) xnzzId);
                                indexDayModel.setDimData1(swjgDm);
                            }),
                    commonFiledExtractor,
                    new GroupBySqlTransformRule(),
                    new WhereSqlTransformRule()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test40040() {
        try {
            String sql = "select xnzz_id,swjg_dm,count(id) from bigdata_yhzx_qz where swjgbz in (0,1) group by xnzz_id,swjg_dm limit 0 glimit 100000";
            String nowDate = TimeUtil.getStringDate(System.currentTimeMillis(), "yyyyMMdd", -1000L * 60 * 60 * 24);
            CommonFiledExtractor commonFiledExtractor = new CommonFiledExtractor(
                    Integer.parseInt(nowDate),
                    Integer.parseInt(nowDate),
                    "40040",
                    "10010",
                    "",
                    "",
                    "",
                    ""
            );
            doInsert(
                    sql,
                    new AggregationSpecialFiledExtractor(
                            (indexDayModel, data) -> {
                                Integer idCount = data.getJSONObject("id_count").getInteger("value");
                                Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
                                String swjgDm = data.getJSONObject("key").getString("swjg_dm");
                                indexDayModel.setTotalCount(idCount);
                                indexDayModel.setOrgId((long) xnzzId);
                                indexDayModel.setDimData1(swjgDm);
                            }),
                    commonFiledExtractor,
                    new GroupBySqlTransformRule(), new WhereSqlTransformRule()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 整体流程
     *
     * @param sql                             sql语句
     * @param inListSpecialFiledExtractorBase 对特殊字段的提取器
     * @param commonFiledExtractorBase        对公共字段的提取器
     * @param transformBases                  sql——dsl转换器
     * @throws Exception 异常
     */
    @SafeVarargs
    public final void doInsert(String sql,
                               SpecialFiledExtractorBase<JSONObject, List<IndexDayModel>> inListSpecialFiledExtractorBase,
                               CommonFiledExtractorBase<JSONObject, List<IndexDayModel>> commonFiledExtractorBase,
                               TransformBase<String>... transformBases) throws Exception {
        //转换实体类
        SqlEntry sqlEntry = SqlTransformDslUtil.getTransformedSqlEntry(sql);
        //转换成dsl
        String dsl = SqlTransformDslUtil.getTransformedDsl(sqlEntry, transformBases);
        //dsl请求elasticsearch
        JSONObject resultJsonObj = esTemplateRepository.queryListByDSL(sqlEntry.getFrom().getValue(), dsl, JSONObject::parseObject);
        //结果格式化
        List<IndexDayModel> formatData = SqlTransformDslUtil.getFormatData(resultJsonObj, inListSpecialFiledExtractorBase, commonFiledExtractorBase);
        //插入
        innerSyncService.insertIndexDay(formatData);
    }
}
