package com.ifugle.rap.sqltransform.base;

/**
 * key选择器
 *
 * @author Minc
 * @date 2022/1/17 11:34
 */
public interface KeySelector<IN> {
    String getKey(IN in) throws Exception;
}