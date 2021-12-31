package com.ifugle.rap.utils.sqltransformutil.transform.group;

import com.ifugle.rap.utils.sqltransformutil.transform.base.TransformBase;

/**
 * @author Minc
 * @date 2021/12/31 15:08
 */
public interface GroupTransform extends TransformBase<String, String> {

    //如果sql为分组sql，则取分组大小
    Integer getGroupSize(String sql) throws Exception;

}
