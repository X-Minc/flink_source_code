package com.ifugle.rap.sqltransform.rule;


import com.ifugle.rap.sqltransform.base.TransformBase;
import com.ifugle.rap.sqltransform.entry.DataType;
import com.ifugle.rap.sqltransform.entry.SqlEntry;

/**
 * @author Minc
 * 默认sql转dsl逻辑
 * @date 2021/12/30 16:39
 */
public class GroupBySqlTransformRule implements TransformBase<String> {
    private static final StringBuilder BUILDER = new StringBuilder();
    //嵌套分组
    private static final String GROUP_MODEL_INNER = "\"aggregations\": {\"{var}\":{ \"aggregations\":{}, \"terms\": {\"field\": \"{var}\"}}}";
    //非嵌套
    private static final String GROUP_MODEL = "\"aggregations\":{\"group_by_field\":{\"composite\":{\"size\":{sizeValue},\"sources\":[{var}]}}}";
    //非嵌套对象
    private static final String INNER_OBJ = "{\"{var}\":{\"terms\":{\"field\":\"{var}\"}}}";

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
            return groupSize.equals("") ?
                    GROUP_MODEL.replace("\"size\":{sizeValue},", "").replace("{var}", BUILDER.toString()) :
                    GROUP_MODEL.replace("{sizeValue}", groupSize).replace("{var}", BUILDER.toString());
        }
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
