package com.ifugle.rap.utils;

import com.ifugle.rap.sqltransform.base.CommonFiledExtractorBase;
import com.ifugle.rap.sqltransform.base.SpecialFiledExtractorBase;
import com.ifugle.rap.sqltransform.commonfiledextractor.CommonFiledExtractor;
import com.ifugle.rap.sqltransform.entry.DataType;
import com.ifugle.rap.sqltransform.baseenum.KeyWord;
import com.ifugle.rap.sqltransform.entry.SqlEntry;
import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.entry.SqlTask;
import com.ifugle.rap.sqltransform.entry.TimeCondition;
import com.ifugle.rap.sqltransform.specialfiledextractor.SingleAggregationSpecialFiledExtractor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Minc
 * 将sql转换为dsl时用到的工具
 * @date 2021/12/30 15:09
 */
public class SqlTransformDslUtil {
    private static final StringBuilder BUILDER = new StringBuilder();

    /**
     * 预处理
     *
     * @param sql                                    sql，请在where条件中加上{time_condition}
     * @param day                                    是否查询day
     * @param days30                                 是否查询30day
     * @param month                                  是否按照month查询
     * @param singleAggregationSpecialFiledExtractor 特殊字段提取集合，数量和公共字段集合保持一致
     * @param commonFiledExtractors                  公共字段提取集合，数量和特殊字段集合保持一致
     * @throws Exception 异常
     */
    public static List<SqlTask> doPreTransform(String sql,
                                               Boolean day,
                                               Boolean days30,
                                               Boolean month,
                                               List<SingleAggregationSpecialFiledExtractor> singleAggregationSpecialFiledExtractor,
                                               List<CommonFiledExtractor> commonFiledExtractors,
                                               String timeFiled,
                                               boolean isLevel) throws Exception {
        sql = sql.toLowerCase(Locale.ROOT);
        List<SqlTask> sqls = new ArrayList<>();
        for (int i = 0; i < Math.max(commonFiledExtractors.size(), singleAggregationSpecialFiledExtractor.size()); i++) {
            if (day) {
                TimeCondition dayCondition = TimeCondition.valueOf("day");
                sqls.add(new SqlTask(
                                sql.replace("{time_condition}", dayCondition.getCondition()),
                                dayCondition.getTableType(),
                                singleAggregationSpecialFiledExtractor.get(i),
                                commonFiledExtractors.get(i),
                                isLevel
                        )
                );
                day = false;
                continue;
            }
            if (days30) {
                TimeCondition days30Condition = TimeCondition.valueOf("days30");
                String date = TimeUtil.getStringDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss", -1000L * 60 * 60 * 24 * 30);
                sqls.add(new SqlTask(
                                sql.replace("{time_condition}", days30Condition.getCondition().replace("{time}", date).replace("{time_filed}", timeFiled)),
                                days30Condition.getTableType(),
                                singleAggregationSpecialFiledExtractor.get(i),
                                commonFiledExtractors.get(i),
                                isLevel
                        )
                );
                days30 = false;
                continue;
            }
            if (month) {
                TimeCondition monthCondition = TimeCondition.valueOf("month");
                String time = TimeUtil.getStringDate(System.currentTimeMillis(), "yyyy-MM");
                sqls.add(new SqlTask(
                                sql.replace("{time_condition}", monthCondition.getCondition().replace("{time}", time).replace("{time_filed}", timeFiled)),
                                monthCondition.getTableType(),
                                singleAggregationSpecialFiledExtractor.get(i),
                                commonFiledExtractors.get(i),
                                isLevel
                        )
                );
                month = false;
            }
        }
        return sqls;
    }

    /**
     * 获得sql转换后的实体类
     *
     * @param sql sql语句
     * @return sql转换后的实体类
     * @throws Exception 异常
     */
    public static SqlEntry getTransformedSqlEntry(String sql) throws Exception {
        List<String> sqlPart = getSqlPart(sql);
        List<String> list = appendKeyWord(sqlPart);
        return getSqlStructure(list);
    }

    /**
     * 将sql实体类转换为dsl
     *
     * @param sqlStructure   sql实体类
     * @param transformBases 转换操作
     * @return dsl语句
     * @throws Exception 异常
     */
    @SafeVarargs
    public static String getTransformedDsl(SqlEntry sqlStructure, TransformBase<String>... transformBases) throws Exception {
        BUILDER.delete(0, BUILDER.length()).append("{");
        for (TransformBase<String> transformBase : transformBases) {
            String transformPart = transformBase.getTransformPart(sqlStructure);
            if (BUILDER.length() == 1) {
                BUILDER.append(transformPart);
            } else {
                if (!transformPart.equals(""))
                    BUILDER.append(",").append(transformPart);
            }
        }
        BUILDER.append("}");
        return BUILDER.toString();
    }

    /**
     * 将sql实体类转换为dsl
     *
     * @param sqlStructure   sql实体类
     * @param transformBases 转换操作
     * @return dsl语句
     * @throws Exception 异常
     */
    public static String getTransformedDsl(SqlEntry sqlStructure, Queue<TransformBase<String>> transformBases) throws Exception {
        BUILDER.delete(0, BUILDER.length()).append("{");
        for (TransformBase<String> transformBase : transformBases) {
            String transformPart = transformBase.getTransformPart(sqlStructure);
            if (BUILDER.length() == 1) {
                BUILDER.append(transformPart);
            } else {
                if (!transformPart.equals(""))
                    BUILDER.append(",").append(transformPart);
            }
        }
        BUILDER.append("}");
        return BUILDER.toString();
    }

    /**
     * 将结果转换为自定义规格结果
     *
     * @param in                        结果
     * @param specialFiledExtractorBase 特殊提取器
     * @param commonFiledExtractorBase  公共字段提取器
     * @param <IN>                      结果类型
     * @param <OUT>                     返回类
     * @return 规格化后的类型
     */
    public static <IN, OUT> OUT getFormatData(IN in,
                                              SpecialFiledExtractorBase<IN, OUT> specialFiledExtractorBase,
                                              CommonFiledExtractorBase<IN, OUT> commonFiledExtractorBase) throws Exception {
        if (specialFiledExtractorBase == null && commonFiledExtractorBase == null)
            throw new Exception("未设置数据提取器！");
        OUT out = null;
        if (specialFiledExtractorBase != null)
            out = specialFiledExtractorBase.getFormatData(in);
        if (commonFiledExtractorBase != null)
            out = commonFiledExtractorBase.customSetData(out);
        return out;
    }

    /**
     * 获得sql的实体类结构
     *
     * @param mergedList 合并后的数组
     * @return sql实体结构类
     * @throws Exception 异常
     */
    private static SqlEntry getSqlStructure(List<String> mergedList) throws Exception {
        SqlEntry sqlEntry = new SqlEntry();
        boolean throwException = true;
        DataType nowDataType = sqlEntry.getFitSqlDataType("select");
        DataType nextDataType;
        for (int i = 0; i < mergedList.size() - 1; i++) {
            try {
                nextDataType = sqlEntry.getFitSqlDataType(mergedList.get(i + 1));
            } catch (Exception e) {
                nextDataType = null;
            }
            if (nextDataType == null && nowDataType != null) {
                throwException = false;
                nowDataType.setValue(nowDataType.getValue() + mergedList.get(i + 1));
            } else
                nowDataType = nextDataType;
        }
        if (throwException) {
            throw new Exception("找不到关键字！请检查sql！");
        } else {
            return sqlEntry;
        }
    }

    /**
     * 合并关键字，类似于group by或者order by
     *
     * @return 合并关键字后的数组
     */
    private static List<String> appendKeyWord(List<String> sqlParts) throws Exception {
        for (int i = 0; i < sqlParts.size(); i++) {
            if (KeyWord.group_by.getKeyword().startsWith(sqlParts.get(i)) ||
                    KeyWord.order_by.getKeyword().startsWith(sqlParts.get(i))) {
                if (sqlParts.get(i + 1).equals("by")) {
                    String remove = sqlParts.remove(i + 1);
                    sqlParts.set(i, sqlParts.get(i) + " " + remove);
                }
            }
        }
        return sqlParts;
    }

    /**
     * 按照空格拆分sql，过滤空元素
     *
     * @return 将sql拆分后的数组
     */
    private static List<String> getSqlPart(String sql) throws Exception {
        if (!sql.startsWith("select"))
            throw new Exception("必须以select开头！请检查sql！");
        return Arrays.stream(sql.split(" ", -1)).filter(x -> !x.equals("")).collect(Collectors.toList());
    }
}