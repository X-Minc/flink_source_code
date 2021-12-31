package com.ifugle.rap.dsp;

import java.util.Arrays;

/**
 * @author Minc
 * @date 2021/12/30 15:44
 */
public class Test {
    public static void main(String[] args) {
        String testSql = "select xnzz_id,bm_id,count(bm_id) from table group by xnzz_id,bm_id";
        String pageStart = "\"from\": var";
        String pageEnd = "\"size\": var";
        String group_model = "\"aggregations\": {\"var\":{ \"aggregations\":{}, \"terms\": {\"field\": \"var\"}}}";
        String[] groupElements = testSql.substring(testSql.indexOf("group by") + 8).split(",", -1);
        String[] selectElements = testSql.substring(testSql.indexOf("select") + 6, testSql.indexOf("from")).split(",", -1);
        Object[] result = Arrays.stream(selectElements).filter(x -> x.contains("(")).map(String::trim).toArray();
        String replaceStart = pageStart.replace("var", "0");
        String replaceEnd = pageEnd.replace("var", "0");
        String groupPart = null;
        for (String groupElement : groupElements) {
            String elementModel = group_model.replace("var", groupElement.trim());
            groupPart = groupPart == null ? elementModel : groupPart.replace(" \"aggregations\":{}", elementModel);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append(replaceStart).append(",").append(replaceEnd).append(",").append(groupPart).append("}");
        System.out.println(stringBuilder);
    }
}
