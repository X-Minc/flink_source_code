/**
 * Copyright(C) 2013 Fugle Technology Co. Ltd. All rights reserved.
 *
 */
package com.ifugle.rap.core.model.impl;

import java.io.Serializable;

import com.ifugle.rap.core.model.BaseModel;

/**
 * @since Sep 28, 2013 3:26:05 PM
 * @version $Id: DefaultModel.java 25541 2016-11-30 06:37:56Z HuZhihuai $
 * @author WuJianqiang
 *
 */
public class DefaultModel<ID extends Serializable> extends AbstractModel implements BaseModel<ID> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected ID id;

    /**
     *
     */
    public DefaultModel() {
        super();
    }

    /**
     * @param id
     */
    public DefaultModel(ID id) {
        super();
        this.id = id;
    }

    /* (non-Javadoc)
     * @see com.ifugle.rap.core.BaseModel#getId()
     */
    @Override
    public ID getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see com.ifugle.rap.core.BaseModel#setId(ID)
     */
    @Override
    public void setId(ID id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null || id == this) ? 0 : id.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        DefaultModel<ID> other = (DefaultModel<ID>) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
