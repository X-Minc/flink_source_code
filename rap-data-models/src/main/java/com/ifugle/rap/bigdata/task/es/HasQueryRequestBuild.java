package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年12月24日 10:54
 */
public class HasQueryRequestBuild implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public String type;

    /**
     * 匹配参数
     * key:字段名
     * value:匹配值
     */
    private Map<String, Object> params;

    /**
     * 不匹配参数
     * key:字段名
     * value:匹配值
     */
    private Map<String, Object> notParams;

    /**
     * 存在参数字段
     * key:字段名
     * value:匹配值
     */
    private List<String> exists;

    /**
     * 不存在参数字段
     * key:字段名
     * value:匹配值
     */
    private List<String> notExists;

    /**
     * 匹配参数 或查詢
     * key:字段名
     * value:匹配值
     */
    private Map<String, Object> shoulds;

    /**
     * 前缀匹配参数
     * key:字段名
     * value:匹配值
     */
    private Map<String, String> prefixs;

    /**
     * 前缀不匹配参数
     * key:字段名
     * value:匹配值
     */
    private Map<String, String> notPrefixs;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setParam(String key, Object value) {
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        this.params.put(key, value);
    }

    public Map<String, Object> getNotParams() {
        return notParams;
    }

    public void setNotParams(Map<String, Object> notParams) {
        this.notParams = notParams;
    }

    public void setNotParam(String key, Object value) {
        if (this.notParams == null) {
            this.notParams = new HashMap<>();
        }
        this.notParams.put(key, value);
    }

    public List<String> getExists() {
        return exists;
    }

    public void setExists(List<String> exists) {
        this.exists = exists;
    }

    public void setExistsParam(String value) {
        if (this.exists == null) {
            this.exists = new ArrayList<>();
        }
        this.exists.add(value);
    }

    public List<String> getNotExists() {
        return notExists;
    }

    public void setNotExists(List<String> notExists) {
        this.notExists = notExists;
    }

    public void setNotExistsParam(String value) {
        if (this.notExists == null) {
            this.notExists = new ArrayList<>();
        }
        this.notExists.add(value);
    }

    public Map<String, Object> getShoulds() {
        return shoulds;
    }

    public void setShoulds(Map<String, Object> shoulds) {
        this.shoulds = shoulds;
    }

    public void setShouldParam(String key, Object value) {
        if (this.shoulds == null) {
            this.shoulds = new HashMap<>();
        }
        this.shoulds.put(key, value);
    }

    public Map<String, String> getPrefixs() {
        return prefixs;
    }

    public void setPrefixs(Map<String, String> prefixs) {
        this.prefixs = prefixs;
    }

    public Map<String, String> getNotPrefixs() {
        return notPrefixs;
    }

    public void setNotPrefixs(Map<String, String> notPrefixs) {
        this.notPrefixs = notPrefixs;
    }

    public void setNotPrefix(String key, String value) {
        if (this.notPrefixs == null) {
            this.notPrefixs = new HashMap<>();
        }
        this.notPrefixs.put(key, value);
    }

    public void setPrefix(String key, String value) {
        if (this.prefixs == null) {
            this.prefixs = new HashMap<>();
        }
        this.prefixs.put(key, value);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public QueryBean.HasParent getHasParent() {
        QueryBean queryBean = getQueryBean();
        QueryBean.HasParent hasParent = new QueryBean.HasParent();
        hasParent.setParentType(type);
        hasParent.setQuery(queryBean);
        return hasParent;
    }

    public QueryBean.HasChild getHasChild() {
        QueryBean queryBean = getQueryBean();
        QueryBean.HasChild hasChild = new QueryBean.HasChild();
        hasChild.setType(type);
        hasChild.setQuery(queryBean);
        return hasChild;
    }

    private QueryBean getQueryBean() {
        DslRequestBuild build = new DslRequestBuild();
        build.setParams(params);
        build.setNotParams(notParams);
        build.setPrefixs(prefixs);
        build.setNotPrefixs(notPrefixs);
        build.setExists(exists);
        build.setNotExists(notExists);
        build.setShoulds(shoulds);

        return build.getQueryBean();
    }
}
