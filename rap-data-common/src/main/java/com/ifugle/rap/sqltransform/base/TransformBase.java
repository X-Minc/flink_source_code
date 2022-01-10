package com.ifugle.rap.sqltransform.base;

import com.ifugle.rap.sqltransform.baseenum.KeyWord;
import com.ifugle.rap.sqltransform.entry.SqlEntry;

/**
 * sql转换为dsl部分的接口
 *
 * @author Minc
 * @date 2021/12/31 17:41
 */
public abstract class TransformBase<OUT> {
    private final KeyWord operate_name;

    /**
     * 将sql实体类自定义转换
     *
     * @param sqlEntry sql实体类
     * @return 输出
     * @throws Exception 异常/
     */
    public abstract OUT getTransformPart(SqlEntry sqlEntry) throws Exception;

    public TransformBase(KeyWord operate_name) {
        this.operate_name = operate_name;
    }

    public KeyWord getOperate_name() {
        return operate_name;
    }
}
