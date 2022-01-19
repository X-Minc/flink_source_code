package com.ifugle.rap.data.task.sync;

import com.alibaba.fastjson.JSONObject;
import com.ifugle.rap.bigdata.task.service.BulkTemplateRepository;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.service.ElasticSearchBusinessService;
import com.ifugle.rap.service.utils.CompriseUtils;
import com.ifugle.rap.sqltransform.base.*;
import com.ifugle.rap.sqltransform.entry.BoardIndexDayModel;
import com.ifugle.rap.sqltransform.entry.IndexDayModel;
import com.ifugle.rap.sqltransform.entry.SqlEntry;
import com.ifugle.rap.sqltransform.entry.SqlTask;
import com.ifugle.rap.sqltransform.keyselector.MergeBeforeMd5KeySelector;
import com.ifugle.rap.sqltransform.samekeyaction.*;
import com.ifugle.rap.sqltransform.specialfiledextractor.SimpleSelectSpecialFiledExtractor;
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
public class SyncFactory extends BaseSqlTaskScheduleFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncFactory.class);
    private static final String SELECT_SQL = "select * from {table_name}  where id=\"{id}\"";
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
    public void runAllTask() throws Exception {
        super.runAllTask();
        sweepUp();
    }

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
        List<IndexDayModel> formatData;
        formatData = SqlTransformDslUtil.getFormatData(
                resultJsonObj,
                sqlTask.getSpecialFiledExtractorBase(),
                sqlTask.getCommonFiledExtractor());
        map.put(sqlTask.getTableType(), formatData == null ? new ArrayList<>() : formatData);
        return map;
    }

    @Override
    public void dealWithResult(Map<Integer, List<IndexDayModel>> map) throws Exception {
        for (Map.Entry<Integer, List<IndexDayModel>> entry : map.entrySet()) {
            List<IndexDayModel> finalMergedList;
            Integer key = entry.getKey();
            List<IndexDayModel> value = entry.getValue();
            //插入
            switch (key) {
                case 1:
                    if (value.size() > 0) {
                        innerSyncService.insertIndexDay(judgeDayOr30DayAndGetMidList(value, MID_INDEX_NAME_DAY, key));
                    }
                    break;
                case 2:
                    if (value.size() > 0) {
                        List<IndexDayModel> indexDayModelList = judgeDayOr30DayAndGetMidList(value, MID_INDEX_NAME_30DAYS, key);
                        innerSyncService.insertIndex30Day(indexDayModelList);
                    }
                    break;
                case 3:
                    List<IndexDayModel> mergedList = value.size() == 0 ? leftJoin(nowDayTotalList, value, null) : leftJoin(value, nowDayTotalList, new MergeTotalCountInLastMonthAndYesterdaySameKeyAction(new MergeBeforeMd5KeySelector()));
                    outPutEs(mergedList, MID_INDEX_NAME_MONTH);
                    //上个月的数据
                    List<IndexDayModel> indexListInLastMonth = getBeforeIndexModelList(mergedList,
                            MID_INDEX_NAME_MONTH,
                            new int[]{Calendar.MONTH},
                            new int[]{-1},
                            "yyyyMM",
                            new SimpleSelectSpecialFiledExtractor(),
                            null);
                    //上一年上个月的数据
                    List<IndexDayModel> indexListInLastMonthInLastYear = getBeforeIndexModelList(mergedList,
                            MID_INDEX_NAME_MONTH,
                            new int[]{Calendar.MONTH},
                            new int[]{-13},
                            "yyyyMM",
                            new SimpleSelectSpecialFiledExtractor(),
                            null);
                    finalMergedList = leftJoin(mergedList, indexListInLastMonth, new MergeLastMonthAndLastTwoMonthInMonthlyTaskSameKeyAction(new MergeBeforeMd5KeySelector()));
                    finalMergedList = leftJoin(finalMergedList, indexListInLastMonth, new RateSameKeyAction(new MergeBeforeMd5KeySelector(), false));
                    finalMergedList = leftJoin(finalMergedList, indexListInLastMonthInLastYear, new RateSameKeyAction(new MergeBeforeMd5KeySelector(), true));
                    if (finalMergedList.size() != 0) {
                        for (IndexDayModel remain : finalMergedList) {
                            remain.setCycleId(Integer.valueOf(remain.getCycleId().toString().substring(0, 6)));
                            remain.setNodeId(Integer.valueOf(remain.getNodeId().toString().substring(0, 6)));
                        }
                        innerSyncService.insertIndexMonth(finalMergedList);
                        nowDayTotalList.clear();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获得（day 和30day）上个月昨天，以及前天的数据数据集
     *
     * @param indexListInYesterday 昨天的数据机
     * @param index                索引
     * @param key                  关键值
     * @return 中间集合
     * @throws Exception 异常
     */
    private List<IndexDayModel> judgeDayOr30DayAndGetMidList(List<IndexDayModel> indexListInYesterday, String index, int key) throws Exception {
        List<IndexDayModel> finalMergedList;
        outPutEs(indexListInYesterday, index);
        //前天的数据
        List<IndexDayModel> indexListOnTheDatBeforeYesterday = getBeforeIndexModelList(indexListInYesterday,
                index,
                new int[]{Calendar.DAY_OF_MONTH},
                new int[]{-2},
                "yyyyMMdd",
                new SimpleSelectSpecialFiledExtractor(),
                null);
        //上个月昨天的数据
        List<IndexDayModel> indexListOnYesterdayInLastMonth = getBeforeIndexModelList(indexListInYesterday,
                index,
                new int[]{Calendar.DAY_OF_MONTH, Calendar.MONTH},
                new int[]{-1, -1},
                "yyyyMMdd",
                new SimpleSelectSpecialFiledExtractor(),
                null);
        if (key == 1) {
            nowDayTotalList.clear();
            nowDayTotalList.addAll(indexListInYesterday);
            //对昨天数据和
            finalMergedList = leftJoin(indexListInYesterday, indexListOnTheDatBeforeYesterday, new MergeYesterdayAndTheDayBeforeYesterdayInDailyTaskSameKeyAction(new MergeBeforeMd5KeySelector()));
        } else {
            finalMergedList = leftJoin(indexListInYesterday, indexListOnTheDatBeforeYesterday, new MergeYesterdayAndTheDayBeforeYesterdayInDays30TaskSameKeyAction(new MergeBeforeMd5KeySelector()));
        }
        finalMergedList = leftJoin(finalMergedList, indexListOnTheDatBeforeYesterday, new RateSameKeyAction(new MergeBeforeMd5KeySelector(), false));
        finalMergedList = leftJoin(finalMergedList, indexListOnYesterdayInLastMonth, new RateSameKeyAction(new MergeBeforeMd5KeySelector(), true));
        return finalMergedList;
    }

    /**
     * 输出 es，返回之前的数据
     *
     * @param formatData 数据集
     * @param indexName  索引名称
     */
    private void outPutEs(List<IndexDayModel> formatData, String indexName) throws Exception {
        StringBuilder dslInsert = new StringBuilder(32);
        for (IndexDayModel formatDatum : formatData) {
            DataRequest request = compriseUtils.IndexDetailDataRequest(formatDatum);
            dslInsert.append(elasticSearchBusinessService.formatSaveOrUpdateDSL(indexName, request));
        }
        elasticSearchBusinessService.bulkOperation(dslInsert.toString());
    }

    /**
     * 查询之前日期的数据集
     *
     * @param indexDayModels            day实体类
     * @param indexName                 索引名称
     * @param type                      {@link Calendar}的静态变量，比如 Calendar.DAY_OF_MONTH
     * @param offset                    偏移 的长度，支持正负数
     * @param format                    日期格式
     * @param specialFiledExtractorBase 特殊字段提取器
     * @param commonFiledExtractorBase  公共字段提取器
     * @return 偏移日期后的数据集合
     * @throws Exception 异常
     */
    private List<IndexDayModel> getBeforeIndexModelList(List<IndexDayModel> indexDayModels,
                                                        String indexName,
                                                        int[] type,
                                                        int[] offset,
                                                        String format,
                                                        SpecialFiledExtractorBase<JSONObject, List<IndexDayModel>> specialFiledExtractorBase,
                                                        CommonFiledExtractorBase<JSONObject, List<IndexDayModel>> commonFiledExtractorBase) throws Exception {
        List<IndexDayModel> beforeListData = new ArrayList<>();
        for (IndexDayModel indexDayModel : indexDayModels) {
            IndexDayModel clone = indexDayModel.clone();
            Integer date = Integer.valueOf(TimeUtil.getBeforeTime(type, offset, format));
            clone.setCycleId(date);
            clone.setNodeId(date);
            String indexKey = compriseUtils.getIndexKey(clone);
            String selectSql = SyncFactory.SELECT_SQL.replace("{table_name}", indexName).replace("{id}", MD5Util.stringToMD5(indexKey));
            SqlTask sqlTask = new SqlTask(selectSql, 1, specialFiledExtractorBase, commonFiledExtractorBase, true);
            Map<Integer, List<IndexDayModel>> integerListMap = doSearchAndGainResult(sqlTask, this.baseTransforms);
            beforeListData.addAll(integerListMap.get(1));
        }
        return beforeListData;
    }

    /**
     * 对相同的自定义key进行自定义操作
     *
     * @param remain        返回的集合
     * @param leave         丢弃的集合
     * @param sameKeyAction 合并集合key选择器以及合并操作
     * @return 集合
     */
    private List<IndexDayModel> leftJoin(List<IndexDayModel> remain, List<IndexDayModel> leave, SameKeyAction<IndexDayModel> sameKeyAction) throws Exception {
        if (sameKeyAction != null) {
            for (IndexDayModel indexDayModel : remain) {
                String key = sameKeyAction.getInKeySelector().getKey(indexDayModel);
                for (IndexDayModel dayModel : leave) {
                    String key1 = sameKeyAction.getInKeySelector().getKey(dayModel);
                    if (key.equals(key1)) {
                        sameKeyAction.sameKeyAction(indexDayModel, dayModel);
                    }
                }
            }
        }
        return remain;
    }

    /**
     * 初始化
     */
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

    /**
     * 批量插入elasticsearch
     */
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
     * 插入看板数据
     */
    private void sweepUp() throws Exception {
        try {
            List<BoardIndexDayModel> boardIndexDayModels = innerSyncService.sweepUp();
            if (boardIndexDayModels.size() != 0)
                innerSyncService.insertBoardIndexDay(boardIndexDayModels);
            LOGGER.info("看板数据插入成功！");
        } catch (Exception e) {
            throw new Exception("看板数据插入时发生错误！", e);
        }
    }
}
