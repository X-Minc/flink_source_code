package com.ifugle.rap.data.task;

import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.bigdata.task.service.BulkTemplateRepository;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.sqltransform.base.KeySelector;
import com.ifugle.rap.sqltransform.base.SqlTaskScheduleFactory;
import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import com.ifugle.rap.sqltransform.entry.SqlEntry;
import com.ifugle.rap.sqltransform.entry.SqlTask;
import com.ifugle.rap.sqltransform.keySelector.DayMergeBeforeSelector;
import com.ifugle.rap.sqltransform.keySelector.Days30MergeBeforeSelector;
import com.ifugle.rap.sqltransform.keySelector.MergeDayAndMonthKeySelector;
import com.ifugle.rap.sqltransform.keySelector.MonthMergeBeforeSelector;
import com.ifugle.rap.sqltransform.specialfiledextractor.SelectSpecialFiledExtractor;
import com.ifugle.rap.sync.service.InnerSyncService;
import com.ifugle.rap.utils.MD5Util;
import com.ifugle.rap.utils.SqlTransformDslUtil;
import com.ifugle.rap.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Minc
 * @date 2022/1/10 10:43
 */
@Component
public class SyncFactory extends SqlTaskScheduleFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncFactory.class);
    private static final String selectSql = "select * from {table_name}  where id=\"{id}\"";
    private static final List<IndexDayModel> nowDayTotalList = new ArrayList<>();
    private static final String MID_INDEX_NAME_DAY = "bigdata_bi_index_day";
    private static final String MID_INDEX_NAME_30DAYS = "bigdata_bi_index_30days";
    private static final String MID_INDEX_NAME_MONTH = "bigdata_bi_index_month";
    private static final int INSERT_THRESHOLD = 500;
    private static final String DAY_INDEX = "bigdata_bi_index_day";
    private static final String DAYS30_INDEX = "bigdata_bi_index_30days";
    private static final String MONTH_INDEX = "bigdata_bi_index_month";
    @Autowired
    private BulkTemplateRepository<Map<String, Object>> esTemplateRepository;

    @Autowired
    private InnerSyncService innerSyncService;

    @Autowired
    private ElasticSearchBusinessService elasticSearchBusinessService;

    @Autowired
    private CompriseUtils compriseUtils;

    @Override
    public Map<Integer, List<IndexDayModel>> doSearchAndGainResult(SqlTask sqlTask, Queue<TransformBase<String>> transformBases) throws Exception {
        Map<Integer, List<IndexDayModel>> map = new HashMap<>();
        //转换实体类
        SqlEntry sqlEntry = SqlTransformDslUtil.getTransformedSqlEntry(sqlTask.getSql());
        //转换成dsl
        String dsl = SqlTransformDslUtil.getTransformedDsl(sqlEntry, transformBases);
        //dsl请求elasticsearch
        JSONObject resultJsonObj = esTemplateRepository.queryListByDSL(sqlEntry.getFrom().getValue(), dsl, JSONObject::parseObject);
        //结果格式化
        List<IndexDayModel> formatData = null;
        try {
            formatData = SqlTransformDslUtil.getFormatData(
                    resultJsonObj,
                    sqlTask.getSpecialFiledExtractorBase(),
                    sqlTask.getCommonFiledExtractor());
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put(sqlTask.getTableType(), formatData == null ? new ArrayList<>() : formatData);
        return map;
    }

    @Override
    public void dealWithResult(Map<Integer, List<IndexDayModel>> map) throws Exception {
        for (Map.Entry<Integer, List<IndexDayModel>> entry : map.entrySet()) {
            List<IndexDayModel> beforeList;
            List<IndexDayModel> mid;
            //插入
            switch (entry.getKey()) {
                case 1:
                    if (entry.getValue().size() > 0) {
                        beforeList = outPutEs(entry.getValue(), MID_INDEX_NAME_DAY, Calendar.DAY_OF_MONTH, -1, "yyyyMMdd");
                        nowDayTotalList.clear();
                        nowDayTotalList.addAll(entry.getValue());
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
                    if (mid.size() != 0) {
                        for (IndexDayModel remain : mid) {
                            remain.setCycleId(Integer.valueOf(remain.getCycleId().toString().substring(0, 6)));
                            remain.setNodeId(Integer.valueOf(remain.getNodeId().toString().substring(0, 6)));
                        }
                        innerSyncService.insertIndexMonth(mid);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void init() throws Exception {
        StringBuilder dslInsert = new StringBuilder(32);
        List<IndexDayModel> indexDayList = innerSyncService.getIndexDayList();
        List<IndexDayModel> index30DaysList = innerSyncService.getIndex30DaysList();
        List<IndexDayModel> indexMonthList = innerSyncService.getIndexMonthList();
        //es初始化
        batchInsertToElasticsearch(dslInsert, indexDayList, DAY_INDEX);
        batchInsertToElasticsearch(dslInsert, index30DaysList, DAYS30_INDEX);
        batchInsertToElasticsearch(dslInsert, indexMonthList, MONTH_INDEX);
        //mysql初始化
        innerSyncService.insertIndexDay(indexDayList);
        innerSyncService.insertIndex30Day(index30DaysList);
        innerSyncService.insertIndexMonth(indexMonthList);
    }

    private void batchInsertToElasticsearch(StringBuilder dslInsert, List<IndexDayModel> indexDayModelList, String indexName) throws Exception {
        try {
            Queue<IndexDayModel> indexDayModels = new LinkedList<>(indexDayModelList);
            int count = 0;
            IndexDayModel model;
            while ((model = indexDayModels.poll()) != null) {
                if (count <= INSERT_THRESHOLD && indexDayModels.peek() != null) {
                    DataRequest request = compriseUtils.IndexDetailDataRequest(model);
                    dslInsert.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(indexName, request));
                    count++;
                } else {
                    elasticSearchBusinessService.bulkOperation(dslInsert.toString());
                    dslInsert.delete(0, dslInsert.length());
                    LOGGER.info(indexName + "索引初始化" + count + "条同步成功！");
                    count = 0;
                }
            }
        } catch (Exception e) {
            throw new Exception(indexName + "初始化同步失败！", e);
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
            String selectSql = SyncFactory.selectSql.replace("{table_name}", indexName).replace("{id}", MD5Util.stringToMD5(indexKey));
            SqlTask sqlTask = new SqlTask(selectSql, 1, new SelectSpecialFiledExtractor(), null);
            Map<Integer, List<IndexDayModel>> integerListMap = doSearchAndGainResult(sqlTask, this.baseTransforms);
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
