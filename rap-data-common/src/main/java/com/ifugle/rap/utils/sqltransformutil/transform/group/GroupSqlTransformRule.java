package com.ifugle.rap.utils.sqltransformutil.transform.group;


import com.ifugle.rap.utils.sqltransformutil.transform.SqlEntry;
import com.ifugle.rap.utils.sqltransformutil.transform.SqlTransformDslUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Minc
 * 默认sql转dsl逻辑
 * @date 2021/12/30 16:39
 */
public class GroupSqlTransformRule implements GroupTransform {
    private static final StringBuilder BUILDER = new StringBuilder();
    //分页开始
    private static final String PAGE_START = "\"from\": var";
    //分页结束
    private static final String PAGE_END = "\"size\": var";
    //嵌套分组
    private static final String GROUP_MODEL_INNER = "\"aggregations\": {\"var\":{ \"aggregations\":{}, \"terms\": {\"field\": \"var\"}}}";
    //非嵌套
    private static final String GROUP_MODEL = "\"aggregations\":{\"group_by_field\":{\"composite\":{\"size\":varSize,\"sources\":[var]}}}";
    //非嵌套对象
    private static final String INNER_OBJ = "{\"var\":{\"terms\":{\"field\":\"var\"}}}";
    //是否进行了分组
    private boolean isGroupBy = false;

    @Autowired
    SqlTransformDslUtil sqlTransformDslUtil;

    @Override
    public String getTransformPart(String s) throws Exception {
        initBuilder();
        SqlEntry sqlStructure = getSqlEntry(s);
        boolean hasGroupBy = false;
        for (String groupElement : sqlStructure.getGroup_by().getValue().split(",")) {
            hasGroupBy = true;
            String elementModel = INNER_OBJ.replace("var", groupElement.trim());
            if (BUILDER.length() == 0) BUILDER.append(elementModel);
            else BUILDER.append(",").append(elementModel);
        }
        isGroupBy = hasGroupBy;
        return GROUP_MODEL.replace("var", BUILDER.toString());
    }

    @Override
    public Integer getGroupSize(String sql) throws Exception {
        if (!isGroupBy) {
            throw new Exception("未发现group by关键字，请检查sql");
        } else {
            SqlEntry sqlEntry = getSqlEntry(sql);
            return Integer.valueOf(sqlEntry.getLimit().getValue());
        }
    }

    // 通过sql获得sql结构的实体类
    private SqlEntry getSqlEntry(String sql) throws Exception {
        return sqlTransformDslUtil.getSqlStructure(sqlTransformDslUtil.appendKeyWord(sqlTransformDslUtil.getSqlPart(sql)));
    }

    //调用stringBuilder必须初始化
    private void initBuilder() {
        BUILDER.delete(0, BUILDER.length());
    }


}
