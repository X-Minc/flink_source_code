package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * 多条件交集分页查询
 *
 * @author JiangYangfan
 * @version $Id: DslRequest.java 98491 2019-05-10 02:41:13Z JiangYangfan $
 * @since 2019年05月09日 19:40
 */
public class DslRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 指针
     */
    private Integer from;
    /**
     * 数量
     */
    private Integer size;

    /**
     * 返回参数选择
     */
    @SerializedName("_source")
    private List<String> source;

    /**
     * 更新参数
     */
    private Map<String, String> script;

    /**
     * 查询体
     */
    private QueryBean query;

    /**
     * 排序
     */
    private List<Map<String, Object>> sort;

    /**
     * 聚合体
     */
    private Map<String, Object> aggs;

    public DslRequest(Integer from, Integer size, List<String> source, QueryBean query, List<Map<String, Object>> sort, Map<String, Object> aggs) {
        this.from = from;
        this.size = size;
        this.source = source;
        this.query = query;
        this.sort = sort;
        this.aggs = aggs;
    }

    public DslRequest(Map<String, String> script, QueryBean query) {
        this.script = script;
        this.query = query;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> _source) {
        this.source = _source;
    }

    public QueryBean getQuery() {
        return query;
    }

    public void setQuery(QueryBean query) {
        this.query = query;
    }

    public List<Map<String, Object>> getSort() {
        return sort;
    }

    public void setSort(List<Map<String, Object>> sort) {
        this.sort = sort;
    }

    public Map<String, Object> getAggs() {
        return aggs;
    }

    public void setAggs(Map<String, Object> aggs) {
        this.aggs = aggs;
    }
}
