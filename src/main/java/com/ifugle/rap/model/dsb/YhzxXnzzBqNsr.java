/**
 * Copyright(C) 2020 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.dsb;

import java.util.Date;
import java.util.Set;

/**
 * @author HuZhihuai
 * @version $Id$
 * @since 2020年03月10日 13:54
 */
public class YhzxXnzzBqNsr {
    private Long id;
    private Long nsrId;
    private Long bqId;
    private Long xnzzId;
    private Date xgsj;
    private Set<Long> bqIds;

    public Long getBqId() {
        return bqId;
    }

    public void setBqId(Long bqId) {
        this.bqId = bqId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getXgsj() {
        return xgsj;
    }

    public void setXgsj(Date xgsj) {
        this.xgsj = xgsj;
    }

    public Long getNsrId() {
        return nsrId;
    }

    public void setNsrId(Long nsrId) {
        this.nsrId = nsrId;
    }

    public Long getXnzzId() {
        return xnzzId;
    }

    public void setXnzzId(Long xnzzId) {
        this.xnzzId = xnzzId;
    }

    public Set<Long> getBqIds() {
        return bqIds;
    }

    public void setBqIds(Set<Long> bqIds) {
        this.bqIds = bqIds;
    }
}