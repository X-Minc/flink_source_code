package com.ifugle.rap.data.task;

import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.bigdata.task.service.BulkTemplateRepository;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.commonfiledextractor.CommonFiledExtractor;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import com.ifugle.rap.sqltransform.entry.SqlEntry;
import com.ifugle.rap.sqltransform.entry.SqlTask;
import com.ifugle.rap.sqltransform.specialfiledextractor.AggregationSpecialFiledExtractor;
import com.ifugle.rap.sqltransform.rule.WhereSqlTransformRule;
import com.ifugle.rap.sqltransform.specialfiledextractor.SelectSpecialFiledExtractor;
import com.ifugle.rap.sync.service.InnerSyncService;
import com.ifugle.rap.sqltransform.rule.GroupBySqlTransformRule;
import com.ifugle.rap.utils.MD5Util;
import com.ifugle.rap.utils.SqlTransformDslUtil;
import com.ifugle.rap.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

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
    private static final String selectSql = "select * from {table_name}  where id=\"{id}\"";
    private static List<IndexDayModel> nowDayTotalList = null;
    private static final String MID_INDEX_NAME_DAY = "bigdata_bi_index_day";
    private static final String MID_INDEX_NAME_30DAYS = "bigdata_bi_index_30days";
    private static final String MID_INDEX_NAME_MONTH = "bigdata_bi_index_month";
    @Autowired
    BulkTemplateRepository<Map<String, Object>> esTemplateRepository;

    @Autowired
    InnerSyncService innerSyncService;

    @Autowired
    private ElasticSearchBusinessService elasticSearchBusinessService;

    @Autowired
    private CompriseUtils compriseUtils;


    @Scheduled(fixedDelay = 1000L * 60 * 30)
    public void getQuery() {
//        test40044();
//        test40043();
//        test40042();
        test40040();
    }

//    private void test40044() {
//        try {
//            AggregationSpecialFiledExtractor aggregationSpecialFiledExtractor = new AggregationSpecialFiledExtractor(
//                    (indexDayModel, data) -> {
//                        Integer idCount = data.getJSONObject("nsr_id_count").getInteger("value");
//                        Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
//                        String swjgDm = data.getJSONObject("key").getString("swjg_dm");
//                        indexDayModel.setTotalCount(idCount);
//                        indexDayModel.setOrgId((long) xnzzId);
//                        indexDayModel.setDimData1(swjgDm);
//                    });
//            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
//                    .builder()
//                    .setIndex("40044")
//                    .setDim1("10010");
//            String sql = "select xnzz_id,swjg_dm,count(nsr_id) from bigdata_yhzx_qz_cy where swjgbz in (0,1) and zzsnslx=1 {timeCondition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
//            doPreTransform(sql,
//                    aggregationSpecialFiledExtractor,
//                    commonFiledExtractorBuilder.setMonthTask(false).build(),
//                    commonFiledExtractorBuilder.setMonthTask(false).build(),
//                    commonFiledExtractorBuilder.setMonthTask(true).build()
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void test40043() {
//        try {
//            String sql = "select xnzz_id,swjg_dm,count(nsr_id) from bigdata_yhzx_qz_cy where swjgbz in (0,1) {timeCondition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
//            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
//                    .builder()
//                    .setIndex("40043")
//                    .setDim1("10010");
//            AggregationSpecialFiledExtractor aggregationSpecialFiledExtractor = new AggregationSpecialFiledExtractor(
//                    (indexDayModel, data) -> {
//                        Integer idCount = data.getJSONObject("nsr_id_count").getInteger("value");
//                        Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
//                        String swjgDm = data.getJSONObject("key").getString("swjg_dm");
//                        indexDayModel.setTotalCount(idCount);
//                        indexDayModel.setOrgId((long) xnzzId);
//                        indexDayModel.setDimData1(swjgDm);
//                    });
//            doPreTransform(
//                    sql,
//                    aggregationSpecialFiledExtractor,
//                    commonFiledExtractorBuilder.setMonthTask(false).build(),
//                    commonFiledExtractorBuilder.setMonthTask(false).build(),
//                    commonFiledExtractorBuilder.setMonthTask(true).build());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void test40042() {
//        try {
//            String sql = "select xnzz_id,swjg_dm,count(yh_id) from bigdata_yhzx_qz_cy where swjgbz in (0,1) {timeCondition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
//            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
//                    .builder()
//                    .setIndex("40042")
//                    .setDim1("10010");
//            AggregationSpecialFiledExtractor aggregationSpecialFiledExtractor = new AggregationSpecialFiledExtractor(
//                    (indexDayModel, data) -> {
//                        Integer idCount = data.getJSONObject("yh_id_count").getInteger("value");
//                        Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
//                        String swjgDm = data.getJSONObject("key").getString("swjg_dm");
//                        indexDayModel.setTotalCount(idCount);
//                        indexDayModel.setOrgId((long) xnzzId);
//                        indexDayModel.setDimData1(swjgDm);
//                    });
//            doPreTransform(
//                    sql,
//                    aggregationSpecialFiledExtractor,
//                    commonFiledExtractorBuilder.setMonthTask(false).build(),
//                    commonFiledExtractorBuilder.setMonthTask(false).build(),
//                    commonFiledExtractorBuilder.setMonthTask(true).build());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void test40040() {
        try {
//            String sql = "select xnzz_id,swjg_dm,count(id),sum(yh_id) from bigdata_yhzx_qz where swjgbz in (0,1) {timeCondition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
            String sql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) from  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("20010")
                    .setDim1("1001");
            List<CommonFiledExtractor> commonFiledExtractorList = new ArrayList<>();
            commonFiledExtractorList.add(commonFiledExtractorBuilder.setMonthTask(false).build());
            commonFiledExtractorList.add(commonFiledExtractorBuilder.setMonthTask(false).build());
            commonFiledExtractorList.add(commonFiledExtractorBuilder.setMonthTask(true).build());
            //特殊字段提取
            AggregationSpecialFiledExtractor aggregationSpecialFiledExtractor = new AggregationSpecialFiledExtractor(
                    (indexDayModel, data) -> {
                        Integer idCount = data.getJSONObject("nsr_id_count").getInteger("value");
                        Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
                        String swjgDm = data.getJSONObject("key").getString("swjg_dms");
                        indexDayModel.setTotalCount(idCount);
                        indexDayModel.setOrgId((long) xnzzId);
                        indexDayModel.setDimData1(swjgDm);
                    });
            AggregationSpecialFiledExtractor aggregationSpecialFiledExtractorInc = new AggregationSpecialFiledExtractor(
                    (indexDayModel, data) -> {
                        Integer idCount = data.getJSONObject("nsr_id_count").getInteger("value");
                        Integer xnzzId = data.getJSONObject("key").getInteger("xnzz_id");
                        String swjgDm = data.getJSONObject("key").getString("swjg_dms");
                        indexDayModel.setIncCount(idCount);
                        indexDayModel.setOrgId((long) xnzzId);
                        indexDayModel.setDimData1(swjgDm);
                    });
            List<AggregationSpecialFiledExtractor> aggregationSpecialFiledExtractorArrayList = new ArrayList<>();
            aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractor);
            aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
            aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
            //执行计划
            List<SqlTask> sqlTasks = SqlTransformDslUtil.doPreTransform(
                    sql,
                    true,
                    true,
                    true,
                    aggregationSpecialFiledExtractorArrayList,
                    commonFiledExtractorList
            );
            Map<Integer, List<IndexDayModel>> resultMap = doSearchElasticSearch(sqlTasks,
                    new GroupBySqlTransformRule(),
                    new WhereSqlTransformRule());
            dealWithResult(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询es获得结果
     *
     * @param sqlTasks       sql任务集合
     * @param transformBases sql——dsl转换器
     * @throws Exception 异常
     */
    @SafeVarargs
    private final Map<Integer, List<IndexDayModel>> doSearchElasticSearch(List<SqlTask> sqlTasks,
                                                                          TransformBase<String>... transformBases) throws Exception {
        Map<Integer, List<IndexDayModel>> map = new HashMap<>();
        for (SqlTask sqlTask : sqlTasks) {
            //转换实体类
            SqlEntry sqlEntry = SqlTransformDslUtil.getTransformedSqlEntry(sqlTask.getSql());
            //转换成dsl
            String dsl = SqlTransformDslUtil.getTransformedDsl(sqlEntry, transformBases);
            //dsl请求elasticsearch
            JSONObject resultJsonObj = esTemplateRepository.queryListByDSL(sqlEntry.getFrom().getValue(), dsl, JSONObject::parseObject);
            //结果格式化
            List<IndexDayModel> formatData = SqlTransformDslUtil.getFormatData(
                    resultJsonObj,
                    sqlTask.getSpecialFiledExtractorBase(),
                    sqlTask.getCommonFiledExtractor());
            if (formatData.size() != 0)
                map.put(sqlTask.getTableType(), formatData);
        }
        return map;
    }

    /**
     * 处理获得结果
     *
     * @param map 结果数据
     * @throws Exception 异常
     */
    private void dealWithResult(Map<Integer, List<IndexDayModel>> map) throws Exception {
        for (Map.Entry<Integer, List<IndexDayModel>> entry : map.entrySet()) {
            //插入
            switch (entry.getKey()) {
                case 1:
                    outPutEs(entry.getValue(), MID_INDEX_NAME_DAY, Calendar.DAY_OF_MONTH, -1, "yyyyMMdd");
                    nowDayTotalList = entry.getValue();
                    break;
                //                        innerSyncService.insertIndexDay(formatData);
                case 2:
                    outPutEs(entry.getValue(), MID_INDEX_NAME_30DAYS, Calendar.DAY_OF_MONTH, -30, "yyyyMMdd");
                    break;
                //                        innerSyncService.insertIndex30Day(formatData);
                case 3:
                    outPutEs(entry.getValue(), MID_INDEX_NAME_MONTH, Calendar.MONTH, -1, "yyyyMM");
                    break;
                //                        innerSyncService.insertIndexMonth(mergeList(formatData, nowDayTotalList, keySelector));
                default:
                    break;
            }
        }
    }

    /**
     * 输出 es
     *
     * @param formatData 数据集
     * @param indexName  索引名称
     */
    private void outPutEs(List<IndexDayModel> formatData, String indexName, Integer type, Integer offset, String format) throws Exception {
        StringBuilder dslInsert = new StringBuilder(32);
        for (IndexDayModel formatDatum : formatData) {
            IndexDayModel clone = formatDatum.clone();
            Integer date = Integer.valueOf(TimeUtil.getBeforeTime(type, offset, format));
            clone.setCycleId(date);
            clone.setNodeId(date);
            String indexKey = compriseUtils.getIndexKey(clone);
            String replace = selectSql.replace("{table_name}", indexName).replace("{id}", MD5Util.stringToMD5(indexKey));
            List<SqlTask> sqlTasks = new ArrayList<>();
            sqlTasks.add(new SqlTask(replace, 1, new SelectSpecialFiledExtractor(), null));
            Map<Integer, List<IndexDayModel>> integerListMap = doSearchElasticSearch(sqlTasks, new GroupBySqlTransformRule(), new WhereSqlTransformRule());
            DataRequest request = compriseUtils.IndexDetailDataRequest(formatDatum);
            dslInsert.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(indexName, request));
        }
        elasticSearchBusinessService.bulkOperation(dslInsert.toString());
    }


    /**
     * 对自定义key进行自定义操作
     *
     * @param remain      返回的集合
     * @param leave       丢弃的集合
     * @param keySelector 合并集合key选择器以及合并操作
     * @return 集合
     */
    private List<IndexDayModel> dealMidWith(List<IndexDayModel> remain, List<IndexDayModel> leave, keySelector<IndexDayModel> keySelector) {
        List<IndexDayModel> midList;
        if (remain.size() > leave.size()) {
            midList = remain;
            remain = leave;
            leave = midList;
        }
        for (IndexDayModel indexDayModel : remain) {
            String key = keySelector.getKey(indexDayModel);
            for (IndexDayModel dayModel : leave) {
                String key1 = keySelector.getKey(dayModel);
                if (key.equals(key1)) {
                    keySelector.sameKeyDone(indexDayModel, dayModel);
                }
            }
        }
        return remain;
    }

    /**
     * 选择key的接口，提取key以及，对相同key的操作
     *
     * @param <IN>
     */
    public interface keySelector<IN> {
        String getKey(IN in);

        void sameKeyDone(IN remain, IN leave);
    }
}
