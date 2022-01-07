package com.ifugle.rap.data.task;

import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.bigdata.task.service.BulkTemplateRepository;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.commonfiledextractor.CommonFiledExtractor;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import com.ifugle.rap.sqltransform.entry.SqlEntry;
import com.ifugle.rap.sqltransform.entry.SqlTask;
import com.ifugle.rap.sqltransform.keySelector.DayMergeBeforeSelector;
import com.ifugle.rap.sqltransform.keySelector.Days30MergeBeforeSelector;
import com.ifugle.rap.sqltransform.keySelector.MergeDayAndMonthKeySelector;
import com.ifugle.rap.sqltransform.keySelector.MonthMergeBeforeSelector;
import com.ifugle.rap.sqltransform.specialfiledextractor.AggregationSpecialFiledExtractor;
import com.ifugle.rap.sqltransform.rule.WhereSqlTransformRule;
import com.ifugle.rap.sqltransform.specialfiledextractor.SelectSpecialFiledExtractor;
import com.ifugle.rap.sync.service.InnerSyncService;
import com.ifugle.rap.sqltransform.rule.GroupBySqlTransformRule;
import com.ifugle.rap.utils.MD5Util;
import com.ifugle.rap.utils.SqlTransformDslUtil;
import com.ifugle.rap.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 请严格按照空格分隔
 * 时间格式过滤只支持 yyyy-MM-dd HH:mm:ss格式
 * limit 为hits部分查询数据的大小
 * glimit为分组后显示bucket的数量
 * count()请加字段，且务必不需要空格
 * 如果要分day和30day以及月，请务必在where后加{time_condition}
 *
 * @author Minc
 * @date 2021/12/30 14:20
 */
@Component
public class SyncTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncTask.class);
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
        nsrIndex20040_1();
        nsrIndex20040_5();
        nsrIndex20070_1();
        nsrIndex20070_5();
        nsrIndex20010_5();
        nsrIndex20010_1();
        qzIndex40042();
        qzIndex40043();
        qzIndex40040();
    }

    /**
     * 纳税人—20040-1
     */
    private void nsrIndex20040_1() {
        try {
            //组织
            String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_dm LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
            String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_dm LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("20040")
                    .setDim1("1001")
                    .setDim2("1004")
                    .setDimData2("1");
            /*----------------------------------------------action-----------------------------------------------*/
            //组织
            enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
        } catch (Exception e) {
            LOGGER.error("纳税人指标20040-1产生错误！", e);
        }
    }

    /**
     * 纳税人—20040-5
     */
    private void nsrIndex20040_5() {
        try {
            //组织
            String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_dm LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
            String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_dm LIKE \"4*\" {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("20040")
                    .setDim1("1001")
                    .setDim2("1004")
                    .setDimData2("5");
            /*----------------------------------------------action-----------------------------------------------*/
            //组织
            enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
        } catch (Exception e) {
            LOGGER.error("纳税人指标20040-5产生错误！", e);
        }
    }

    /**
     * 纳税人—20070-1
     */
    private void nsrIndex20070_1() {
        try {
            //组织
            String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_dm NOT LIKE \"4*\" AND djzclx_dm IS NOT NULL AND djzclx_dm !=\"\" AND djzclx_dm!=0 {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
            String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) AND djzclx_dm NOT LIKE \"4*\" AND djzclx_dm IS NOT NULL AND djzclx_dm !=\"\" AND djzclx_dm!=0 {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("20070")
                    .setDim1("1001")
                    .setDim2("1004")
                    .setDimData2("1");
            /*----------------------------------------------action-----------------------------------------------*/
            //组织
            enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
        } catch (Exception e) {
            LOGGER.error("纳税人指标20070-1产生错误！", e);
        }
    }

    /**
     * 纳税人—20070-5
     */
    private void nsrIndex20070_5() {
        try {
            //组织
            String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_dm NOT LIKE \"4*\" AND djzclx_dm IS NOT NULL AND djzclx_dm !=\"\" AND djzclx_dm!=0 {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
            String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) AND djzclx_dm NOT LIKE \"4*\" AND djzclx_dm IS NOT NULL AND djzclx_dm !=\"\" AND djzclx_dm!=0 {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("20070")
                    .setDim1("1001")
                    .setDim2("1004")
                    .setDimData2("5");
            /*----------------------------------------------action-----------------------------------------------*/
            //组织
            enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
        } catch (Exception e) {
            LOGGER.error("纳税人指标20070-5产生错误！", e);
        }
    }

    /**
     * 纳税人—20010-1
     */
    private void nsrIndex20010_1() {
        try {
            //组织
            String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
            String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND jhbj=1 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("20010")
                    .setDim1("1001")
                    .setDim2("1004")
                    .setDimData2("1");
            /*----------------------------------------------action-----------------------------------------------*/
            //组织
            enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
        } catch (Exception e) {
            LOGGER.error("纳税人指标20010-1产生错误！", e);
        }
    }

    /**
     * 纳税人—20010-5
     */
    private void nsrIndex20010_5() {
        try {
            //组织
            String selfSql = "SELECT  xnzz_id,swjg_dm,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dm limit 0 glimit 100000";
            String superiorSql = "SELECT  xnzz_id,swjg_dms,COUNT(nsr_id) FROM  bigdata_yhzx_xnzz_nsr WHERE is_delete=0 AND swjgbz IN (1,2) {time_condition} GROUP BY  xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("20010")
                    .setDim1("1001")
                    .setDim2("1004")
                    .setDimData2("5");
            /*----------------------------------------------action-----------------------------------------------*/
            //组织
            enter(selfSql, superiorSql, commonFiledExtractorBuilder, false, "cjsj");
        } catch (Exception e) {
            LOGGER.error("纳税人指标20010-5产生错误！", e);
        }
    }

    /**
     * 群组—40040
     */
    private void qzIndex40040() {
        try {
            //组织
            String selfSql = "select xnzz_id,swjg_dm,count(qz_id) from bigdata_yhzx_qz where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
            String superiorSql = "select xnzz_id,swjg_dms,count(qz_id) from bigdata_yhzx_qz where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("40040")
                    .setDim1("1001");
            /*----------------------------------------------action-----------------------------------------------*/
            //组织
            enter(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
        } catch (Exception e) {
            LOGGER.error("群组指标40040产生错误！", e);
        }
    }

    /**
     * 群组—40042
     */
    private void qzIndex40042() {
        try {
            //组织
            String selfSql = "select xnzz_id,swjg_dm,count(yh_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
            String superiorSql = "select xnzz_id,swjg_dms,count(yh_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("40042")
                    .setDim1("1001");
            /*----------------------------------------------action-----------------------------------------------*/
            //组织
            enter(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
        } catch (Exception e) {
            LOGGER.error("群组指标40042产生错误！", e);
        }
    }

    /**
     * 群组—40043
     */
    private void qzIndex40043() {
        try {
            //组织
            String selfSql = "select xnzz_id,swjg_dm,count(nsr_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dm limit 0 glimit 100000";
            String superiorSql = "select xnzz_id,swjg_dms,count(nsr_id) from bigdata_yhzx_qz_cy where qlx=2 and is_deleted=0 and swjgbz in (1,2) {time_condition} group by xnzz_id,swjg_dms limit 0 glimit 100000";
            //通用字段提取
            CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder = CommonFiledExtractor
                    .builder()
                    .setIndex("40043")
                    .setDim1("1001");
            /*----------------------------------------------action-----------------------------------------------*/
            //组织
            enter(selfSql, superiorSql, commonFiledExtractorBuilder, true, "cjsj");
        } catch (Exception e) {
            LOGGER.error("群组指标40043产生错误！", e);
        }
    }

    /**
     * 入口
     *
     * @param selfSql                     本级sql
     * @param superiorSql                 上级sql
     * @param commonFiledExtractorBuilder 公共字段提取
     * @param isQz                        是否为群组指标
     * @throws Exception 异常
     */
    private void enter(String selfSql, String superiorSql, CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder, Boolean isQz, String timeFiled) throws Exception {
        List<AggregationSpecialFiledExtractor> specialSelfAgg = getSpecialSelfAgg(isQz);
        List<AggregationSpecialFiledExtractor> specialSuperiorAgg = getSpecialSuperiorAgg(isQz);
        List<CommonFiledExtractor> commonAgg = getCommonAgg(commonFiledExtractorBuilder, isQz);
        List<SqlTask> selfSqlTasks =
                SqlTransformDslUtil.doPreTransform(selfSql, true, !isQz, true, specialSelfAgg, commonAgg, timeFiled);
        List<SqlTask> superiorSqlTasks =
                SqlTransformDslUtil.doPreTransform(superiorSql, true, !isQz, true, specialSuperiorAgg, commonAgg, timeFiled);
        /*--------------------------------------------result-----------------------------------------------*/
        //组织
        Map<Integer, List<IndexDayModel>> selfSqlTasksResultMap = doSearchElasticSearch(selfSqlTasks, new GroupBySqlTransformRule(), new WhereSqlTransformRule());
        Map<Integer, List<IndexDayModel>> superiorSqlTasksResultMap = doSearchElasticSearch(superiorSqlTasks, new GroupBySqlTransformRule(), new WhereSqlTransformRule());
        /*--------------------------------------------dealWith------------------------------------------------*/
        //组织
        dealWithResult(selfSqlTasksResultMap);
        dealWithResult(superiorSqlTasksResultMap);
    }

    /**
     * 获得公共的字段提取器集合
     *
     * @param commonFiledExtractorBuilder 公共字段提取器
     * @param isQz                        是否为群组
     * @return 包含当天，30天，月的字段提取器集合
     * @throws CloneNotSupportedException 异常
     */
    private List<CommonFiledExtractor> getCommonAgg(CommonFiledExtractor.CommonFiledExtractorBuilder commonFiledExtractorBuilder, Boolean isQz) throws CloneNotSupportedException {
        List<CommonFiledExtractor> commonFiledExtractorList = new ArrayList<>();
        commonFiledExtractorList.add(commonFiledExtractorBuilder.setMonthTask(false).build());
        if (!isQz)
            commonFiledExtractorList.add(commonFiledExtractorBuilder.setMonthTask(false).build());
        commonFiledExtractorList.add(commonFiledExtractorBuilder.setMonthTask(true).build());
        return commonFiledExtractorList;
    }

    /**
     * 获得特殊的本级字段提取器集合
     *
     * @param isQz 是否为群组
     * @return 特殊的本级字段提取器集合（包含，当天，30天，当月）
     * @throws Exception 异常
     */
    private List<AggregationSpecialFiledExtractor> getSpecialSelfAgg(Boolean isQz) throws Exception {
        AggregationSpecialFiledExtractor aggregationSpecialFiledExtractor = new AggregationSpecialFiledExtractor(new AggregationSpecialFiledExtractor.DealWithTotalAndSelf());
        AggregationSpecialFiledExtractor aggregationSpecialFiledExtractorInc = new AggregationSpecialFiledExtractor(new AggregationSpecialFiledExtractor.DealWithIncAndSelf());
        List<AggregationSpecialFiledExtractor> aggregationSpecialFiledExtractorArrayList = new ArrayList<>();
        aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractor);
        if (!isQz)
            aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
        aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
        return aggregationSpecialFiledExtractorArrayList;
    }

    /**
     * 获得特殊的上级字段提取器
     *
     * @param isQz 是否为群组
     * @return 特殊的上级字段提取器集合（包含，当天，30天，当月）
     */
    private List<AggregationSpecialFiledExtractor> getSpecialSuperiorAgg(Boolean isQz) throws Exception {
        AggregationSpecialFiledExtractor aggregationSpecialFiledExtractor = new AggregationSpecialFiledExtractor(new AggregationSpecialFiledExtractor.DealWithTotalAndSuperior());
        AggregationSpecialFiledExtractor aggregationSpecialFiledExtractorInc = new AggregationSpecialFiledExtractor(new AggregationSpecialFiledExtractor.DealWithIncAndSuperior());
        List<AggregationSpecialFiledExtractor> aggregationSpecialFiledExtractorArrayList = new ArrayList<>();
        aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractor);
        if (!isQz)
            aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
        aggregationSpecialFiledExtractorArrayList.add(aggregationSpecialFiledExtractorInc);
        return aggregationSpecialFiledExtractorArrayList;
    }

    /**
     * 查询es获得结果
     *
     * @param sqlTasks       sql任务集合
     * @param transformBases sql——dsl转换器
     * @throws Exception 异常
     */
    @SafeVarargs
    private final Map<Integer, List<IndexDayModel>> doSearchElasticSearch(List<SqlTask> sqlTasks, TransformBase<String>... transformBases) throws Exception {
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
            map.put(sqlTask.getTableType(), formatData == null ? new ArrayList<>() : formatData);
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
            List<IndexDayModel> beforeList;
            List<IndexDayModel> mid;
            //插入
            switch (entry.getKey()) {
                case 1:
                    if (entry.getValue().size() > 0) {
                        beforeList = outPutEs(entry.getValue(), MID_INDEX_NAME_DAY, Calendar.DAY_OF_MONTH, -1, "yyyyMMdd");
                        nowDayTotalList = entry.getValue();
                        mid = leftJoin(entry.getValue(), beforeList, new DayMergeBeforeSelector());
                        innerSyncService.insertIndexDay(mid);
                    }
                    break;
                case 2:
                    if (entry.getValue().size() > 0) {
                        beforeList = outPutEs(entry.getValue(), MID_INDEX_NAME_30DAYS, Calendar.DAY_OF_MONTH, -1, "yyyyMMdd");
                        mid = leftJoin(entry.getValue(), beforeList, new Days30MergeBeforeSelector());
                        innerSyncService.insertIndex30Day(mid);
                    }
                    break;
                case 3:
                    List<IndexDayModel> mergedList = entry.getValue().size() == 0 ? leftJoin(nowDayTotalList, entry.getValue(), null) : leftJoin(entry.getValue(), nowDayTotalList, new MergeDayAndMonthKeySelector());
                    beforeList = outPutEs(mergedList, MID_INDEX_NAME_MONTH, Calendar.MONTH, -1, "yyyyMM");
                    mid = leftJoin(mergedList, beforeList, new MonthMergeBeforeSelector());
                    for (IndexDayModel remain : mid) {
                        remain.setCycleId(Integer.valueOf(remain.getCycleId().toString().substring(0, 6)));
                        remain.setNodeId(Integer.valueOf(remain.getNodeId().toString().substring(0, 6)));
                    }
                    innerSyncService.insertIndexMonth(mid);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 输出 es，返回之前的数据
     *
     * @param formatData 数据集
     * @param indexName  索引名称
     */
    private List<IndexDayModel> outPutEs(List<IndexDayModel> formatData, String indexName, Integer type, Integer offset, String format) throws Exception {
        List<IndexDayModel> beforeListData = new ArrayList<>();
        StringBuilder dslInsert = new StringBuilder(32);
        for (IndexDayModel formatDatum : formatData) {
            IndexDayModel clone = formatDatum.clone();
            Integer date = Integer.valueOf(TimeUtil.getBeforeTime(type, offset - 1, format));
            clone.setCycleId(date);
            clone.setNodeId(date);
            String indexKey = compriseUtils.getIndexKey(clone);
            String selectSql = SyncTask.selectSql.replace("{table_name}", indexName).replace("{id}", MD5Util.stringToMD5(indexKey));
            List<SqlTask> sqlTasks = new ArrayList<>();
            sqlTasks.add(new SqlTask(selectSql, 1, new SelectSpecialFiledExtractor(), null));
            Map<Integer, List<IndexDayModel>> integerListMap = doSearchElasticSearch(sqlTasks, new GroupBySqlTransformRule(), new WhereSqlTransformRule());
            beforeListData.addAll(integerListMap.get(1));
            DataRequest request = compriseUtils.IndexDetailDataRequest(formatDatum);
            dslInsert.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(indexName, request));
        }
        elasticSearchBusinessService.bulkOperation(dslInsert.toString());
        return beforeListData;
    }

    /**
     * 对相同的自定义key进行自定义操作
     *
     * @param remain      返回的集合
     * @param leave       丢弃的集合
     * @param keySelector 合并集合key选择器以及合并操作
     * @return 集合
     */
    private List<IndexDayModel> leftJoin(List<IndexDayModel> remain, List<IndexDayModel> leave, KeySelector<IndexDayModel> keySelector) throws Exception {
        if (keySelector != null) {
            for (IndexDayModel indexDayModel : remain) {
                String key = keySelector.getKey(indexDayModel);
                for (IndexDayModel dayModel : leave) {
                    String key1 = keySelector.getKey(dayModel);
                    if (key.equals(key1)) {
                        keySelector.sameKeyDone(indexDayModel, dayModel);
                    }
                }
            }
        }
        return remain;
    }
}
