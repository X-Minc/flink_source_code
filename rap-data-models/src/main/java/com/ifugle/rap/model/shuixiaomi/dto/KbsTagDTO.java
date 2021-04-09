/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.shuixiaomi.dto;

import com.ifugle.rap.core.model.impl.EnhanceModel;

import java.util.Date;

/**
 * @author LiuZhengyang
 * @version $Id: KbsTagDTO.java 84708 2018-11-09 08:12:37Z HuangLei $
 * @since 2018年10月15日 14:09
 */
public class KbsTagDTO extends EnhanceModel<Long> {

    private long linkId;

    private String name;

    private Date creationDate;

    private Date modificationDate;

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public String toString() {
        return "KbsTagDTO{" + "linkId=" + linkId + ", name='" + name + '\'' + ", creationDate=" + creationDate + ", modificationDate=" + modificationDate + '}';
    }
}
