/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.elasticsearch.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author HuangLei(wenyuan)
 * @version $Id: DataRequest.java, v 0.1 2018年5月15日 上午11:31:18 HuangLei(wenyuan) Exp $
 */
public class DataRequest implements Serializable {

    /** serialVersionUID */
    private static final long   serialVersionUID = 3409451708804443549L;

    /***
     * es对应的类名称，对应mysql的表名
     */
    private String              catalogType;

    /***
     * 提交的es的列字段，key-value类型
     */
    private Map<String, Object> map              = new HashMap<String, Object>();

    /**
     * Getter method for property <tt>catalogType</tt>.
     * 
     * @return property value of catalogType
     */
    public String getCatalogType() {
        return catalogType;
    }

    /**
     * Setter method for property <tt>catalogType</tt>.
     * 
     * @param catalogType value to be assigned to property catalogType
     */
    public void setCatalogType(String catalogType) {
        this.catalogType = catalogType;
    }

    /**
     * Getter method for property <tt>map</tt>.
     * 
     * @return property value of map
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * Setter method for property <tt>map</tt>.
     * 
     * @param map value to be assigned to property map
     */
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "DataRequest{" + "catalogType='" + catalogType + '\'' + ", map=" + map + '}';
    }
}
