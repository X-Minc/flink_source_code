package com.ifugle.rap.sqltransform.rule;


import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.baseenum.KeyWord;
import com.ifugle.rap.sqltransform.entry.DataType;
import com.ifugle.rap.sqltransform.entry.SqlEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Minc
 * 默认sql转dsl逻辑
 * @date 2021/12/30 16:39
 */
public class GroupBySqlTransformRule extends TransformBase<String> {
    private static final StringBuilder BUILDER = new StringBuilder();
    //嵌套分组
    private static final String GROUP_MODEL_INNER = "\"aggregations\": {\"{var}\":{ \"aggregations\":{}, \"terms\": {\"field\": \"{var}\"}}}";
    //非嵌套
    private static final String GROUP_MODEL = "\"aggregations\":{\"group_by_field\":{\"composite\":{\"size\":{sizeValue},\"sources\":[{var}]},\"aggregations\":{{agg}}}}";
    //非嵌套对象
    private static final String INNER_OBJ = "{\"{var}\":{\"terms\":{\"field\":\"{var}\"}}}";
    //count distinct
    private static final String COUNT_DISTINCT_MODEL = "\"count\":{\"cardinality\":{\"field\":\"{filed}\"}}";
    //sum
    private static final String SUM_MODEL = "\"sum\":{\"sum\":{\"field\":\"{filed}\"}}";

    public GroupBySqlTransformRule() {
        super(KeyWord.group_by);
    }

    @Override
    public String getTransformPart(SqlEntry sqlEntry) throws Exception {
        initBuilder();
        DataType group_by = sqlEntry.getGroup_by();
        if (group_by.getValue().equals("")) {
            return "";
        } else {
            boolean hasGroupBy = false;
            for (String groupElement : group_by.getValue().split(",")) {
                hasGroupBy = true;
                String elementModel = INNER_OBJ.replace("{var}", groupElement.trim());
                if (BUILDER.length() == 0) BUILDER.append(elementModel);
                else BUILDER.append(",").append(elementModel);
            }
            String groupSize = getGroupSize(sqlEntry, hasGroupBy);
//            String countDsl = getKeyDsl(COUNT_DISTINCT_MODEL, getKeyVar(sqlEntry, "count"));
//            String sumDsl = getKeyDsl(SUM_MODEL, getKeyVar(sqlEntry, "sum"));
            List<String> dslPart = new ArrayList<>();
//            dslPart.add(countDsl);
//            dslPart.add(sumDsl);
            String totalDsl = getKeyDsl(null, dslPart);
            return groupSize.equals("") ?
                    GROUP_MODEL.replace("{sizeValue}", "100000").replace("{var}", BUILDER.toString()).replace("{agg}", totalDsl) :
                    GROUP_MODEL.replace("{sizeValue}", groupSize).replace("{var}", BUILDER.toString()).replace("{agg}", totalDsl);
        }
    }

    private String getKeyDsl(String model, List<String> countVars) {
        StringBuilder stringBuilder = new StringBuilder();
        if (countVars != null) {
            for (String countVar : countVars) {
                if (!countVar.equals("")) {
                    if (stringBuilder.length() == 0) {
                        if (model != null) stringBuilder.append(model.replace("{filed}", countVar));
                        else stringBuilder.append(countVar);
                    } else {
                        if (model != null) stringBuilder.append(",").append(model.replace("{filed}", countVar));
                        else stringBuilder.append(",").append(countVar);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    private List<String> getKeyVar(SqlEntry sqlEntry, String key) {
        List<String> resultList = null;
        DataType select = sqlEntry.getSelect();
        String[] split = select.getValue().split(",", -1);
        if (split.length != 0) {
            for (String s : split) {
                if (s.contains(key)) {
                    if (resultList == null)
                        resultList = new ArrayList<>();
                    resultList.add(s.substring(s.indexOf("(") + 1, s.indexOf(")")));
                }
            }
        }
        return resultList;
    }

    private String getGroupSize(SqlEntry sqlEntry, Boolean isGroupBy) throws Exception {
        if (!isGroupBy) {
            throw new Exception("未发现group by关键字，请检查sql！");
        } else {
            String value = sqlEntry.getGlimit().getValue();
            if (value.equals("")) return "";
            else return value;
        }
    }

    //调用stringBuilder必须初始化
    private void initBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }

}
