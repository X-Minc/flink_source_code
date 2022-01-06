package com.ifugle.rap.sqltransform.rule;

import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.baseenum.Compare;
import com.ifugle.rap.sqltransform.entry.DataType;
import com.ifugle.rap.sqltransform.entry.SqlEntry;

/**
 * @author Minc
 * @date 2022/1/4 14:12
 */
public class WhereSqlTransformRule implements TransformBase<String> {
    private static final StringBuilder BUILDER = new StringBuilder();
    private static final String orModel = "{\"terms\":{\"{var}\":{value}}}";
    private static final String whereModel = "\"query\":{\"bool\":{\"filter\":[{var}]}}";
    private static final String whereSize = "\"size\":{var}";
    private static final String rangeModel = "{\"range\":{\"{var}\":{\"{range}\":{value}}}}";
    private static final String equalModel = "{\"term\":{\"{var}\":{value}}}";

    @Override
    public String getTransformPart(SqlEntry sqlEntry) throws Exception {
        initBuilder();
        DataType where = sqlEntry.getWhere();
        if (where.getValue().equals("")) {
            return "";
        } else {
            String[] values = where.getValue().split("and");
            String queryCondition = getQueryCondition(values);
            String wherePart = whereModel.replace("{var}", queryCondition);
            Integer size = getWhereSize(sqlEntry);
            if (size != null) {
                String whereSizePart = whereSize.replace("{var}", size.toString());
                return wherePart + "," + whereSizePart;
            } else {
                return wherePart;
            }
        }
    }

    private Integer getWhereSize(SqlEntry sqlEntry) throws Exception {
        String value = sqlEntry.getLimit().getValue();
        if (value.equals("")) return null;
        else return Integer.parseInt(value);
    }

    private String getQueryCondition(String[] values) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            Compare fitCompareOperate = getFitCompareOperate(value);
            if (fitCompareOperate != null) {
                String[] splitData = value.split(fitCompareOperate.getCompareOperate().replace("(", "\\("), -1);
                if (fitCompareOperate.equals(Compare.term)) {
                    stringBuilder.append(equalModel.replace("{var}", splitData[0])
                                    .replace("{value}", splitData[1]))
                            .append(",");
                } else if (fitCompareOperate.getCompareOperate().equals("in(")) {
                    stringBuilder.append(orModel
                                    .replace("{var}", splitData[0])
                                    .replace("{value}", "[" + splitData[1].replace(")", "") + "]"))
                            .append(",");
                } else {
                    if (splitData[1].contains("-")) {
                        String pre = splitData[1].substring(0, 11);
                        String suf = splitData[1].substring(11);
                        splitData[1] = pre + " " + suf;
                    }
                    stringBuilder.append(rangeModel
                                    .replace("{var}", splitData[0])
                                    .replace("{value}", splitData[1])
                                    .replace("{range}", fitCompareOperate.getMean()))
                            .append(",");
                }
            }
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    private void initBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

    private Compare getFitCompareOperate(String value) throws Exception {
        if (value.contains(Compare.gte.getCompareOperate()))
            return Compare.gte;
        else if (value.contains(Compare.gt.getCompareOperate()))
            return Compare.gt;
        else if (value.contains(Compare.lte.getCompareOperate()))
            return Compare.lte;
        else if (value.contains(Compare.lt.getCompareOperate())) {
            return Compare.lt;
        } else if (value.contains(Compare.in.getCompareOperate())) {
            return Compare.in;
        } else {
            if (value.contains(Compare.term.getCompareOperate())) {
                return Compare.term;
            } else {
                return null;
            }
        }
    }
}
