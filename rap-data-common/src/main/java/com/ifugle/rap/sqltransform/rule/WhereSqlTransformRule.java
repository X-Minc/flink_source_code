package com.ifugle.rap.sqltransform.rule;

import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.baseenum.Compare;
import com.ifugle.rap.sqltransform.baseenum.KeyWord;
import com.ifugle.rap.sqltransform.entry.DataType;
import com.ifugle.rap.sqltransform.entry.SqlEntry;

/**
 * @author Minc
 * @date 2022/1/4 14:12
 */
public class WhereSqlTransformRule extends TransformBase<String> {
    private static final StringBuilder BUILDER = new StringBuilder();
    private static final String orModel = "{\"terms\":{\"{var}\":{value}}}";
    private static final String whereModel = "\"query\":{\"bool\":{\"must\":[{mustVar}],\"must_not\":[{mustNotVar}]}}";
    private static final String whereSize = "\"size\":{var}";
    private static final String rangeModel = "{\"range\":{\"{var}\":{\"{range}\":{value}}}}";
    private static final String equalAndNotModel = "{\"term\":{\"{var}\":{value}}}";
    private static final String likeModel = "{\"wildcard\":{\"{filed}\":{\"value\":{value}}}}";
    private static final String nullModel = "{\"exists\":{\"field\":\"{filed}\"}}";

    public WhereSqlTransformRule() {
        super(KeyWord.where);
    }


    @Override
    public String getTransformPart(SqlEntry sqlEntry) throws Exception {
        initBuilder();
        DataType where = sqlEntry.getWhere();
        if (where.getValue().equals("")) {
            return "";
        } else {
            String[] values = where.getValue().split("and");
            String wherePart = getQueryCondition(values);
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
        StringBuilder mustStringBuilder = new StringBuilder();
        StringBuilder mustNotStringBuilder = new StringBuilder();
        for (String value : values) {
            Compare fitCompareOperate = getFitCompareOperate(value);
            if (fitCompareOperate != null) {
                String[] splitData = value.split(fitCompareOperate.getCompareOperate().replace("(", "\\("), -1);
                if (fitCompareOperate.equals(Compare.notLike) || fitCompareOperate.equals(Compare.notTerm) || fitCompareOperate.equals(Compare.isNull)) {
                    if (fitCompareOperate.equals(Compare.notLike)) {
                        mustNotStringBuilder.append(likeModel.replace("{filed}", splitData[0])
                                        .replace("{value}", splitData[1]))
                                .append(",");
                    } else if (fitCompareOperate.equals(Compare.isNull)) {
                        mustNotStringBuilder.append(nullModel.replace("{filed}", splitData[0]))
                                .append(",");
                    } else {
                        mustNotStringBuilder.append(equalAndNotModel.replace("{var}", splitData[0])
                                        .replace("{value}", splitData[1]))
                                .append(",");
                    }
                } else {
                    if (fitCompareOperate.equals(Compare.term)) {
                        mustStringBuilder.append(equalAndNotModel.replace("{var}", splitData[0])
                                        .replace("{value}", splitData[1]))
                                .append(",");
                    } else if (fitCompareOperate.getCompareOperate().equals("in(")) {
                        mustStringBuilder.append(orModel
                                        .replace("{var}", splitData[0])
                                        .replace("{value}", "[" + splitData[1].replace(")", "") + "]"))
                                .append(",");
                    } else if (fitCompareOperate.equals(Compare.isNotNull)) {
                        mustStringBuilder.append(nullModel.replace("{filed}", splitData[0]))
                                .append(",");
                    } else {
                        if (splitData[1].contains("-")) {
                            String pre = splitData[1].substring(0, 11);
                            String suf = splitData[1].substring(11);
                            splitData[1] = pre + " " + suf;
                        }
                        mustStringBuilder.append(rangeModel
                                        .replace("{var}", splitData[0])
                                        .replace("{value}", splitData[1])
                                        .replace("{range}", fitCompareOperate.getMean()))
                                .append(",");
                    }
                }
            }
        }
        String must = "";
        if (mustStringBuilder.length() != 0)
            must = mustStringBuilder.substring(0, mustStringBuilder.length() - 1);
        String mustNot = "";
        if (mustNotStringBuilder.length() != 0)
            mustNot = mustNotStringBuilder.substring(0, mustNotStringBuilder.length() - 1);
        return whereModel.replace("{mustVar}", must).replace("{mustNotVar}", mustNot);
    }

    private void initBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

    private Compare getFitCompareOperate(String value) throws Exception {
        if (value.contains(Compare.isNotNull.getCompareOperate())) {
            return Compare.isNotNull;
        } else if (value.contains(Compare.isNull.getCompareOperate())) {
            return Compare.isNull;
        } else if (value.contains(Compare.gte.getCompareOperate()))
            return Compare.gte;
        else if (value.contains(Compare.gt.getCompareOperate()))
            return Compare.gt;
        else if (value.contains(Compare.lte.getCompareOperate()))
            return Compare.lte;
        else if (value.contains(Compare.lt.getCompareOperate())) {
            return Compare.lt;
        } else if (value.contains(Compare.in.getCompareOperate())) {
            return Compare.in;
        } else if (value.contains(Compare.notTerm.getCompareOperate())) {
            return Compare.notTerm;
        } else if (value.contains(Compare.term.getCompareOperate())) {
            return Compare.term;
        } else if (value.contains(Compare.notLike.getCompareOperate())) {
            return Compare.notLike;
        } else {
            if (value.contains(Compare.like.getCompareOperate())) return Compare.like;
            else return null;
        }
    }
}
