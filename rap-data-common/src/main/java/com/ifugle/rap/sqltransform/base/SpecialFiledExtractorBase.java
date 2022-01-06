package com.ifugle.rap.sqltransform.base;

/**
 * 将es获取得得数据转换为自定义类型的接口
 *
 * @author Minc
 * @date 2022/1/4 10:44
 */
public interface SpecialFiledExtractorBase<IN, OUT> {

    /**
     * 对特有的属性进行赋值
     *
     * @param in 输入数据
     * @return 输出数据
     * @throws Exception 异常
     */
    OUT getFormatData(IN in) throws Exception;

}
