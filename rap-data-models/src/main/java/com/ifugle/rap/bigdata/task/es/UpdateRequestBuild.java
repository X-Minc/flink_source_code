package com.ifugle.rap.bigdata.task.es;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.ifugle.rap.exception.DsbServiceException;
import com.ifugle.rap.utils.GsonUtil;
import com.ifugle.util.NullUtil;

/**
 * @author JiangYangfan
 * @version $Id: DslRequestBuild.java 98491 2019-05-10 02:41:13Z JiangYangfan $
 * @since 2019年05月09日 20:37
 */
public class UpdateRequestBuild {

    /**
     * 更新参数
     */
    private Map<String, Object> updateParams;

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

    /**
     * 请求体
     */
    private DslRequest request;

    /**
     * 父类查询条件
     */
    private QueryBean.HasParent hasParent;

    /**
     * 子类查询条件
     */
    private QueryBean.HasChild hasChild;

    public UpdateRequestBuild() {
    }

    public Map<String, Object> getUpdateParams() {
        return updateParams;
    }

    public void setUpdateParams(Map<String, Object> updateParams) {
        this.updateParams = updateParams;
    }

    public void setUpdateParam(String key, Object value) {
        if (this.updateParams == null) {
            this.updateParams = new HashMap<>();
        }
        this.updateParams.put(key, value);
    }

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

    public QueryBean.HasParent getHasParent() {
        return hasParent;
    }

    public void setHasParent(QueryBean.HasParent hasParent) {
        this.hasParent = hasParent;
    }

    public QueryBean.HasChild getHasChild() {
        return hasChild;
    }

    public void setHasChild(QueryBean.HasChild hasChild) {
        this.hasChild = hasChild;
    }

    public String build() {
        QueryBean queryBean = getQueryBean();

        // 增加父类查询条件
        if (NullUtil.isNotNull(this.hasParent)) {
            ArrayList<QueryBean.BoolBean.MustBean> must = Lists.newArrayListWithExpectedSize(1);
            QueryBean.BoolBean.MustBean mustBean = new QueryBean.BoolBean.MustBean();
            mustBean.setHasParent(this.hasParent);
            must.add(mustBean);

            if (NullUtil.isNull(queryBean)) {
                queryBean = new QueryBean(new QueryBean.BoolBean(must));
            } else {
                List<QueryBean.BoolBean.MustBean> queryMust = queryBean.getBool().getMust();
                if (NullUtil.isNull(queryMust)) {
                    queryBean.getBool().setMust(must);
                } else {
                    queryMust.addAll(must);
                }
            }
        }

        // 增加子类查询条件
        if (NullUtil.isNotNull(this.hasChild)) {
            ArrayList<QueryBean.BoolBean.MustBean> must = Lists.newArrayListWithExpectedSize(1);
            QueryBean.BoolBean.MustBean mustBean = new QueryBean.BoolBean.MustBean();
            mustBean.setHasChild(this.hasChild);
            must.add(mustBean);

            if (NullUtil.isNull(queryBean)) {
                queryBean = new QueryBean(new QueryBean.BoolBean(must));
            } else {
                List<QueryBean.BoolBean.MustBean> queryMust = queryBean.getBool().getMust();
                if (NullUtil.isNull(queryMust)) {
                    queryBean.getBool().setMust(must);
                } else {
                    queryMust.addAll(must);
                }
            }
        }

        Map<String, String> script = null;
        if (NullUtil.isNotNull(updateParams)) {
            StringBuilder paramBody = new StringBuilder();
            for (String key : updateParams.keySet()) {
                paramBody.append("ctx._source['");
                paramBody.append(key);
                paramBody.append("'] = ");
                String value = GsonUtil.toJson(updateParams.get(key));
                if (value.startsWith("\"")) {
                    // 如果是字符串格式，需要将"改为'
                    value = value.replaceFirst("\"", "'");
                    value = value.substring(0, value.length() - 1) + "'";
                }
                paramBody.append(value);
                paramBody.append(";");
            }

            script = new HashMap<>();
            script.put("inline", paramBody.toString());

        }
        // 转换json格式数据
        this.request = new DslRequest(script, queryBean);
        String requestText = GsonUtil.toJson(request);
        return requestText;
    }

    public QueryBean getQueryBean() {
        QueryBean queryBean = null;
        // 增加匹配值查询
        if (NullUtil.isNotNull(this.params)) {
            ArrayList<QueryBean.BoolBean.MustBean> must = new ArrayList<>();
            this.params.forEach((k, v) -> {
                QueryBean.BoolBean.MustBean mustBean = getMustBean(k, v);
                must.add(mustBean);
            });

            queryBean = new QueryBean(new QueryBean.BoolBean(must));
        }

        // 增加前缀匹配值查询
        if (NullUtil.isNotNull(this.prefixs)) {
            ArrayList<QueryBean.BoolBean.MustBean> must = new ArrayList<>();
            this.prefixs.forEach((k, v) -> {
                QueryBean.BoolBean.MustBean mustBean = getMustBeanByPrefix(k, v);
                must.add(mustBean);
            });

            if (NullUtil.isNull(queryBean)) {
                queryBean = new QueryBean(new QueryBean.BoolBean(must));
            } else {
                List<QueryBean.BoolBean.MustBean> queryMust = queryBean.getBool().getMust();
                queryMust.addAll(must);
            }
        }

        // 增加存在字段查询
        if (NullUtil.isNotNull(this.exists)) {
            ArrayList<QueryBean.BoolBean.MustBean> must = new ArrayList<>();
            this.exists.forEach((v) -> {
                QueryBean.BoolBean.MustBean mustBean = getMustBeanByExists("field", v);
                must.add(mustBean);
            });

            if (NullUtil.isNull(queryBean)) {
                queryBean = new QueryBean(new QueryBean.BoolBean(must));
            } else {
                List<QueryBean.BoolBean.MustBean> queryMust = queryBean.getBool().getMust();
                queryMust.addAll(must);
            }
        }

        // 增加或值查询
        if (NullUtil.isNotNull(this.shoulds)) {
            ArrayList<QueryBean.BoolBean.MustBean> should = new ArrayList<>();
            this.shoulds.forEach((k, v) -> {
                QueryBean.BoolBean.MustBean mustBean = getMustBean(k, v);
                should.add(mustBean);
            });

            if (NullUtil.isNull(queryBean)) {
                queryBean = new QueryBean(new QueryBean.BoolBean(null, null, should));
            } else {
                queryBean.getBool().setShould(should);
            }
        }

        // 增加不是/不等于值查询
        if (NullUtil.isNotNull(this.notParams)) {
            ArrayList<QueryBean.BoolBean.MustBean> mustNot = new ArrayList<>();
            this.notParams.forEach((k, v) -> {
                QueryBean.BoolBean.MustBean mustBean = getMustBean(k, v);
                mustNot.add(mustBean);
            });

            if (NullUtil.isNull(queryBean)) {
                queryBean = new QueryBean(new QueryBean.BoolBean(null, mustNot));
            } else {
                queryBean.getBool().setMustNot(mustNot);
            }
        }

        // 增加不是/不等于前缀匹配值查询
        if (NullUtil.isNotNull(this.notPrefixs)) {
            ArrayList<QueryBean.BoolBean.MustBean> mustNot = new ArrayList<>();
            this.notPrefixs.forEach((k, v) -> {
                QueryBean.BoolBean.MustBean mustBean = getMustBeanByPrefix(k, v);
                mustNot.add(mustBean);
            });

            if (NullUtil.isNull(queryBean)) {
                queryBean = new QueryBean(new QueryBean.BoolBean(null, mustNot));
            } else {
                List<QueryBean.BoolBean.MustBean> queryMustNot = queryBean.getBool().getMustNot();
                if (NullUtil.isNull(queryMustNot)) {
                    queryBean.getBool().setMustNot(mustNot);
                } else {
                    queryMustNot.addAll(mustNot);
                }
            }
        }

        // 增加不存在字段查询
        if (NullUtil.isNotNull(this.notExists)) {
            ArrayList<QueryBean.BoolBean.MustBean> mustNot = new ArrayList<>();
            this.notExists.forEach((v) -> {
                QueryBean.BoolBean.MustBean mustBean = getMustBeanByExists("field", v);
                mustNot.add(mustBean);
            });

            if (NullUtil.isNull(queryBean)) {
                queryBean = new QueryBean(new QueryBean.BoolBean(null, mustNot));
            } else {
                List<QueryBean.BoolBean.MustBean> queryMustNot = queryBean.getBool().getMustNot();
                if (NullUtil.isNull(queryMustNot)) {
                    queryBean.getBool().setMustNot(mustNot);
                } else {
                    queryMustNot.addAll(mustNot);
                }
            }
        }
        return queryBean;
    }

    private QueryBean.BoolBean.MustBean getMustBeanByExists(String k, String v) {
        Map<String, String> exist = new HashMap<>();
        exist.put(k, v);
        QueryBean.BoolBean.MustBean mustBean = new QueryBean.BoolBean.MustBean();
        mustBean.setExists(exist);
        return mustBean;
    }

    private QueryBean.BoolBean.MustBean getMustBeanByPrefix(String k, String v) {
        Map<String, String> prefix = new HashMap<>();
        prefix.put(k, v);
        QueryBean.BoolBean.MustBean mustBean = new QueryBean.BoolBean.MustBean();
        mustBean.setPrefix(prefix);
        return mustBean;
    }

    private QueryBean.BoolBean.MustBean getMustBean(String k, Object v) {
        Map<String, Collection> terms = null;
        Map<String, Object> match = null;
        Map<String, Number> term = null;
        Map<String, RangeBean> range = null;
        if (v instanceof Collection) {
            terms = new HashMap<>();
            terms.put(k, (Collection) v);
        } else if (v instanceof String) {
            match = new HashMap<>();
            match.put(k, v);
        } else if (v instanceof Number) {
            term = new HashMap<>();
            term.put(k, (Number) v);
        } else if (v instanceof RangeBean) {
            range = new HashMap<>();
            range.put(k, (RangeBean) v);
        } else {
            throw new DsbServiceException("ElasticSearch 封装查询参数异常");
        }

        return new QueryBean.BoolBean.MustBean(match, terms, term, range);
    }
}
