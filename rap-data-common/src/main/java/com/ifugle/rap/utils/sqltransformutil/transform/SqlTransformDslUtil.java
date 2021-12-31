package com.ifugle.rap.utils.sqltransformutil.transform;

import com.ifugle.rap.utils.sqltransformutil.transform.base.DataType;
import com.ifugle.rap.utils.sqltransformutil.transform.base.KeyWord;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Minc
 * 将sql转换为dsl时用到的工具
 * @date 2021/12/30 15:09
 */
@Component
public class SqlTransformDslUtil {

    /**
     * 获得sql的实体类结构
     *
     * @param mergedList 合并后的数组
     * @return sql实体结构类
     * @throws Exception 异常
     */
    public SqlEntry getSqlStructure(List<String> mergedList) throws Exception {
        SqlEntry sqlEntry = new SqlEntry();
        boolean throwException = true;
        for (int i = 0; i < mergedList.size() - 1; i++) {
            throwException = false;
            DataType fitSqlDataType = sqlEntry.getFitSqlDataType(mergedList.get(i));
            if (fitSqlDataType != null) {
                fitSqlDataType.setValue(mergedList.get(i + 1));
            }
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
    public List<String> appendKeyWord(List<String> sqlParts) {
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
    public List<String> getSqlPart(String sql) {
        return Arrays.stream(sql.split(" ", -1)).filter(x -> !x.equals("")).collect(Collectors.toList());
    }
}
