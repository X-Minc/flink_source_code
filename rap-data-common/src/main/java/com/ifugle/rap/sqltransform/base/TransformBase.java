package com.ifugle.rap.sqltransform.base;

import com.ifugle.rap.sqltransform.entry.SqlEntry;

/**
 * sql转换为dsl部分的接口
 *
 * @author Minc
 * @date 2021/12/31 17:41
 */
public interface TransformBase<OUT> {
    OUT getTransformPart(SqlEntry sqlEntry) throws Exception;
}
