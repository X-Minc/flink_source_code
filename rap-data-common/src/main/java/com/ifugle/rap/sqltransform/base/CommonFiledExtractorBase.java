package com.ifugle.rap.sqltransform.base;

/**
 * 将es获取得得数据转换为自定义类型的接口
 *
 * @author Minc
 * @date 2022/1/4 10:44
 */
public interface CommonFiledExtractorBase<IN, OUT> {

    /**
     * 对通用的属性进行赋值
     *
     * @param out 输出数据
     * @return 设置后的输出数据
     * @throws Exception 异常
     */
    OUT customSetData(OUT out) throws Exception;
}
