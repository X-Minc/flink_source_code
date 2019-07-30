/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.redis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id ChangedPropertyData.java v 0.1 2018/11/12 HuangLei(wenyuan) Exp $
 */
public class ChangedPropertyData implements Serializable {

    private static final long serialVersionUID = 7107266068279125753L;
    /***
     * 要更新的ids
     */
    private List<Long> ids;

    /***
     * 要更新的docName
     */
    private String docName;

    /***
     * 要更新的属性map
     */
    private Map<String, Object> properties = new HashMap<String, Object>();

    public ChangedPropertyData(Long id) {
        this.ids = Arrays.asList(id);
    }

    public void addData(String propertyName, Object data) {
        properties.put(propertyName, data);
    }

    /**
     * @return the ids
     */
    public List<Long> getIds() {
        return ids;
    }

    /**
     * @param ids the ids to set
     */
    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    /**
     * @return the properties
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public ChangedPropertyData(List<Long> ids) {
        this.ids = Collections.unmodifiableList(ids);
    }
    public ChangedPropertyData() {
    }
}
