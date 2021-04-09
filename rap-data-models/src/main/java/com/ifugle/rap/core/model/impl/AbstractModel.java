/**
 * Copyright(C) 2013 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.ifugle.rap.core.model.impl;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * POJO 模型抽象层，主要封装了常用的用户环境变量读取，以供持久层（如MyBatis）的 Mapper.xml 内引用
 *
 * @since Sep 28, 2013 3:26:05 PM
 * @version $Id: AbstractModel.java 33990 2017-05-17 16:24:03Z WuJianqiang $
 * @author WuJianqiang
 *
 */
public abstract class AbstractModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
