package com.ifugle.rap.sqltransform.base;

/**
 * 选择key的接口，提取key以及，对相同key的操作
 *
 * @param <IN>
 */
public interface KeySelector<IN> {
    String getKey(IN in);

    void sameKeyDone(IN remain, IN leave);
}