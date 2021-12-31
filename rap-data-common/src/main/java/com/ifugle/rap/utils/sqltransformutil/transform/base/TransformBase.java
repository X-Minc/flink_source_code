package com.ifugle.rap.utils.sqltransformutil.transform.base;

/**
 * @author Minc
 * @date 2021/12/31 17:41
 */
public interface TransformBase<IN, OUT> {
    OUT getTransformPart(IN in) throws Exception;
}
