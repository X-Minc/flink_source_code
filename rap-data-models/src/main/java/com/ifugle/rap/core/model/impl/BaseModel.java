/**
 * Copyright(C) 2013 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.ifugle.rap.core.model.impl;

import java.io.Serializable;

/**
 * @since Sep 28, 2013 7:40:08 PM
 * @version $Id: BaseModel.java 15463 2016-05-28 07:44:34Z WuJianqiang $
 * @author WuJianqiang
 *
 * @param <ID>
 */
public interface BaseModel<ID extends Serializable> {

    /**
     * @return the id
     */
    public ID getId();

    /**
     * @param id
     *            the id to set
     */
    public void setId(ID id);

}