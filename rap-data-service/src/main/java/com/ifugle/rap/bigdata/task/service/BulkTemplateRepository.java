package com.ifugle.rap.bigdata.task.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifugle.rap.bigdata.task.es.AggsResponseEntity;
import com.ifugle.rap.elasticsearch.core.ClusterNode;
import com.ifugle.rap.elasticsearch.entity.HitEntity;
import com.ifugle.rap.elasticsearch.entity.QueryEntity;
import com.ifugle.rap.exception.DsbEsRemoteException;
import com.ifugle.rap.exception.DsbEsServiceException;
import com.ifugle.rap.utils.GsonUtil;
import com.ifugle.util.NullUtil;

/**
 * @author XuWeigang
 * @since 2019年07月17日 15:16
 */
@Service
public class BulkTemplateRepository<T> {
    private static String METHOD_POST = "POST";
    private static String METHOD_PUT = "PUT";
    private static String METHOD_GET = "GET";
    private static String METHOD_DELETE = "DELETE";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RestClient restClient;
    @Autowired
    private ClusterNode clusterNode;

    @Value("${es.socket.timeout}")
    private Integer socketTimeOut;

    @Value("${es.url}")
    private String esUrl;

    @Value("${es.password}")
    private String password;

    @Value("${es.username}")
    private String username;

    @Value("${es.max.retry.timeout}")
    private Integer maxRetryTimeout;

    @Value("${es.connect.timeout}")
    private Integer connTimeOut;

    private RestClient getClient() {
        if (this.restClient == null) {
            String[] url_port = esUrl.split(":", -1);
            this.clusterNode.setHttpHosts(new HttpHost[]{new HttpHost(url_port[0], Integer.parseInt(url_port[1]))});
            this.clusterNode.setUsername(username);
            this.clusterNode.setPassword(password);
            this.clusterNode.setConnectTimeout(connTimeOut);
            this.clusterNode.setSocketTimeout(socketTimeOut);
            this.clusterNode.setMaxRetryTimeoutMillis(maxRetryTimeout);
            this.restClient = this.clusterNode.secureConnect();
        }
        return this.restClient;
    }

    public Response bulk(String index, HttpEntity entity) {
        Response indexResponse = null;

        try {
            String url = "/" + index + "/_bulk";
            Map<String, String> paramMap = new HashMap();
            // 优化json参数
            paramMap.put("pretty", "true");
            // 刷新策略，更新数据后等待数据可见
            paramMap.put("refresh", "true");

            indexResponse = getClient().performRequest(METHOD_POST, url, paramMap, entity, new Header[0]);
        } catch (IOException var9) {
            this.logger.error(var9.getMessage(), var9);
            throw new DsbEsServiceException("批量处理异常", var9);
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
            throw new DsbEsRemoteException("批量处理异常", var10);
        }

        return indexResponse;
    }

    public Response mget(String index, HttpEntity entity) {
        Response indexResponse = null;

        try {
            String url = "/" + index + "/_mget";
            Map<String, String> paramMap = new HashMap();
            // 优化json参数
            paramMap.put("pretty", "true");

            indexResponse = getClient().performRequest(METHOD_GET, url, paramMap, entity, new Header[0]);
        } catch (IOException var9) {
            this.logger.error(var9.getMessage(), var9);
            throw new DsbEsServiceException("批量keyId查询异常", var9);
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
            throw new DsbEsRemoteException("批量keyId查询异常", var10);
        }

        return indexResponse;
    }

    /**
     * 搜索查询，（分页支持两种，浅分页和快照分页）
     *
     * @param index
     * @param query
     * @param scrollTime 分钟数 0：浅分页，大于0：快照分页
     * @return
     */
    public Response queryListByDSL(String index, String query, int scrollTime) {
        StringBuffer urlSB = new StringBuffer(16);
        urlSB.append("/").append(index);
        urlSB.append("/_search");
        Map<String, String> paramMap = new HashMap();
        // 优化json参数
        paramMap.put("pretty", "true");
        if (scrollTime > 0) {
            paramMap.put("scroll", scrollTime + "m");
        }

        Response indexResponse = null;
        NStringEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);

        try {
            indexResponse = getClient().performRequest(METHOD_GET, urlSB.toString(), paramMap, entity, new Header[0]);
        } catch (Exception var12) {
            this.logger.error(var12.getMessage(), var12);
            throw new DsbEsRemoteException("分页查询异常", var12);
        }

        return indexResponse;
    }

    public Response scrollQuery(String request) {
        StringBuffer url = new StringBuffer(16);
        url.append("/_search").append("/scroll");
        Map<String, String> paramMap = new HashMap();
        // 优化json参数
        paramMap.put("pretty", "true");

        Response indexResponse = null;
        NStringEntity entity = new NStringEntity(request, ContentType.APPLICATION_JSON);

        try {
            indexResponse = getClient().performRequest(METHOD_GET, url.toString(), paramMap, entity, new Header[0]);
        } catch (Exception var12) {
            this.logger.error(var12.getMessage(), var12);
            throw new DsbEsRemoteException("快照分页查询异常", var12);
        }

        return indexResponse;
    }

    public Response deleteScrollQuery(String scrollId) {
        StringBuffer url = new StringBuffer(16);
        url.append("/_search").append("/scroll/").append(scrollId);
        Map<String, String> paramMap = new HashMap();
        // 优化json参数
        paramMap.put("pretty", "true");

        Response indexResponse = null;
        try {
            indexResponse = getClient().performRequest(METHOD_DELETE, url.toString(), paramMap, null, new Header[0]);
        } catch (Exception var12) {
            this.logger.error(var12.getMessage(), var12);
            throw new DsbEsRemoteException("删除快照分页异常", var12);
        }
        return indexResponse;
    }

    /**
     * 判断ES index是否存在
     *
     * @param index
     * @return
     */
    public boolean getIndexExist(String index) {
        try {
            String url = "/" + index + "/_mapping";
            Map<String, String> paramMap = new HashMap();
            // 优化json参数
            paramMap.put("pretty", "true");

            getClient().performRequest(METHOD_GET, url, paramMap, null, new Header[0]);
            // 存在对应index
            return true;
        } catch (IOException var9) {
            String message = var9.getMessage();
            if (message.contains("index_not_found_exception")) {
                // 不存在对应index
                return false;
            } else {
                this.logger.error(var9.getMessage(), var9);
                throw new DsbEsServiceException("获取ES的index信息异常", var9);
            }
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
            throw new DsbEsRemoteException("获取ES的index信息异常", var10);
        }
    }

    /**
     * 根据查询条件更新数据
     *
     * @param index
     * @param entity
     * @return
     */
    public Response updateByQuery(String index, HttpEntity entity, Map<String, String> paramMap) {
        Response indexResponse = null;

        try {
            StringBuffer url = new StringBuffer(16);
            url.append("/").append(index);
            url.append("/_update_by_query");

            if (NullUtil.isNull(paramMap)) {
                paramMap = new HashMap();
            }
            // 优化json参数
            paramMap.put("pretty", "true");

            indexResponse = getClient().performRequest(METHOD_POST, url.toString(), paramMap, entity, new Header[0]);
        } catch (IOException var9) {
            this.logger.error(var9.getMessage(), var9);
            throw new DsbEsServiceException("根据查询条件更新异常", var9);
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
            throw new DsbEsRemoteException("根据查询条件更新异常", var10);
        }

        return indexResponse;
    }

    /**
     * 根据task查询后台执行结果
     *
     * @param task
     * @return
     */
    public Response tasks(String task) {
        Response indexResponse = null;

        try {
            StringBuffer url = new StringBuffer(16);
            url.append("/_tasks");
            url.append("/").append(task);
            Map<String, String> paramMap = new HashMap();
            // 优化json参数
            paramMap.put("pretty", "true");

            indexResponse = getClient().performRequest(METHOD_GET, url.toString(), paramMap, null, new Header[0]);
        } catch (IOException var9) {
            this.logger.error(var9.getMessage(), var9);
            throw new DsbEsServiceException("根据task查询后台执行结果异常", var9);
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
            throw new DsbEsRemoteException("根据task查询后台执行结果异常", var10);
        }

        return indexResponse;
    }

    /**
     * 设置es参数
     *
     * @param index
     * @param entity
     * @return
     */
    public Response settings(String index, HttpEntity entity) {
        Response indexResponse = null;

        try {
            StringBuffer url = new StringBuffer(16);
            url.append("/").append(index);
            url.append("/_settings");
            Map<String, String> paramMap = new HashMap();
            // 优化json参数
            paramMap.put("pretty", "true");

            indexResponse = getClient().performRequest(METHOD_PUT, url.toString(), paramMap, entity, new Header[0]);
        } catch (IOException var9) {
            this.logger.error(var9.getMessage(), var9);
            throw new DsbEsServiceException("设置es参数异常", var9);
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
            throw new DsbEsRemoteException("设置es参数异常", var10);
        }

        return indexResponse;
    }

    /**
     * 手动刷新
     *
     * @param index
     * @return
     */
    public Response refresh(String index) {
        Response indexResponse = null;

        try {
            StringBuffer url = new StringBuffer(16);
            url.append("/").append(index);
            url.append("/_refresh");
            Map<String, String> paramMap = new HashMap();
            // 优化json参数
            paramMap.put("pretty", "true");

            indexResponse = getClient().performRequest(METHOD_POST, url.toString(), paramMap, null, new Header[0]);
        } catch (IOException var9) {
            this.logger.error(var9.getMessage(), var9);
            throw new DsbEsServiceException("刷新es异常", var9);
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
            throw new DsbEsRemoteException("刷新es异常", var10);
        }

        return indexResponse;
    }

    public <OUT> OUT queryListByDSL(String index, String query, DataFormat<OUT> dataFormat) {
        StringBuffer urlSB = new StringBuffer(16);
        urlSB.append("/").append(index).append("/_search");
        OUT results = null;
        Map<String, String> paramMap = new HashMap();
        // 优化json参数
        paramMap.put("pretty", "true");

        Response indexResponse = null;
        NStringEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);

        try {
            indexResponse = getClient().performRequest("GET", urlSB.toString(), paramMap, entity, new Header[0]);
        } catch (Exception var12) {
            this.logger.error(var12.getMessage(), var12);
        }

        if (indexResponse == null) {
            this.logger.error("queryListByDSL  DSL error , return null");
            return results;
        } else {
            ObjectMapper mapper = new ObjectMapper();

            try {
                String queryResult = EntityUtils.toString(indexResponse.getEntity());
                this.logger.debug("indexResponse result = " + queryResult);
                results = dataFormat.format(queryResult);
                return results;
            } catch (Exception var11) {
                this.logger.error(MessageFormat.format("queryListByDSL transport json to List error , msg : {0}", new Object[]{var11}));
                return results;
            }
        }
    }

    public interface DataFormat<OUT> {
        OUT format(String in);
    }

    public QueryEntity<T> queryListByDSL(String index, String query) {
        StringBuffer urlSB = new StringBuffer(16);
        urlSB.append("/").append(index).append("/_search");
        QueryEntity<T> results = null;
        Map<String, String> paramMap = new HashMap();
        // 优化json参数
        paramMap.put("pretty", "true");

        Response indexResponse = null;
        NStringEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);

        try {
            indexResponse = getClient().performRequest("GET", urlSB.toString(), paramMap, entity, new Header[0]);
        } catch (Exception var12) {
            this.logger.error(var12.getMessage(), var12);
        }

        if (indexResponse == null) {
            this.logger.error("queryListByDSL  DSL error , return null");
            return results;
        } else {
            ObjectMapper mapper = new ObjectMapper();

            try {
                String queryResult = EntityUtils.toString(indexResponse.getEntity());
                this.logger.debug("indexResponse result = " + queryResult);
                results = (QueryEntity) mapper.readValue(queryResult, new TypeReference<QueryEntity<T>>() {
                });
                return results;
            } catch (Exception var11) {
                this.logger.error(MessageFormat.format("queryListByDSL transport json to List error , msg : {0}", new Object[]{var11}));
                return results;
            }
        }
    }

    public AggsResponseEntity aggsqueryByDSL(String index, String query) {
        StringBuffer urlSB = new StringBuffer(16);
        urlSB.append("/").append(index);
        urlSB.append("/_search");
        AggsResponseEntity results = null;
        Map<String, String> paramMap = new HashMap();
        // 优化json参数
        paramMap.put("pretty", "true");

        Response indexResponse = null;
        NStringEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);

        try {
            indexResponse = getClient().performRequest("GET", urlSB.toString(), paramMap, entity, new Header[0]);
        } catch (Exception var12) {
            this.logger.error(var12.getMessage(), var12);
        }

        if (indexResponse == null) {
            this.logger.error("queryListByDSL  DSL error , return null, query {}", query);
            return results;
        } else {
            try {
                String queryResult = EntityUtils.toString(indexResponse.getEntity());
                results = GsonUtil.fromJson(queryResult, AggsResponseEntity.class);
                return results;
            } catch (Exception var11) {
                this.logger.error(MessageFormat.format("queryListByDSL transport json to List error , msg : {0}", new Object[]{var11}));
                return results;
            }
        }
    }

    /**
     * 根据查询条件删除数据
     *
     * @param index
     * @param entity
     * @return
     */
    public Response deleteByQuery(String index, HttpEntity entity) {
        Response indexResponse = null;

        try {
            String url = "/" + index + "/_delete_by_query";
            Map<String, String> paramMap = new HashMap();
            // 优化json参数
            paramMap.put("pretty", "true");
            // 刷新策略，更新数据后等待数据可见
            paramMap.put("refresh", "true");

            indexResponse = getClient().performRequest(METHOD_POST, url, paramMap, entity, new Header[0]);
        } catch (IOException var9) {
            this.logger.error(var9.getMessage(), var9);
            throw new DsbEsServiceException("根据查询条件删除数据异常", var9);
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
            throw new DsbEsRemoteException("根据查询条件删除数据异常", var10);
        }

        return indexResponse;
    }

    /**
     * 根据索引的_id获取数据
     *
     * @param index
     * @param id
     * @return
     */
    public HitEntity<Map<String, Object>> getById(String index, String id) {
        String url = "/" + index + "/_search";
        Map<String, String> paramMap = new HashMap();
        // 优化json参数
        paramMap.put("pretty", "true");

        StringBuilder query = new StringBuilder();
        query.append("{\"query\":{\"term\":{\"_id\":{\"value\":\"");
        query.append(id);
        query.append("\"}}}}");
        NStringEntity entity = new NStringEntity(query.toString(), ContentType.APPLICATION_JSON);

        Response indexResponse = null;
        try {
            indexResponse = getClient().performRequest("GET", url, paramMap, entity, new Header[0]);
            String queryResult = EntityUtils.toString(indexResponse.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            QueryEntity<Map<String, Object>> results = (QueryEntity) mapper.readValue(queryResult,
                    new TypeReference<QueryEntity<Map<String, Object>>>() {
                    });
            List<HitEntity<Map<String, Object>>> hits = results.getHits().getHits();
            if (NullUtil.isNotNull(hits)) {
                return hits.get(0);
            } else {
                return null;
            }
        } catch (Exception var12) {
            this.logger.error(var12.getMessage(), var12);
            return null;
        }
    }

    /**
     * 复制索引
     *
     * @param oldIndexName
     * @param newIndexName
     * @return
     */
    public boolean creatIndexAndMapping(String oldIndexName, String newIndexName) {
        String url = "/" + oldIndexName + "/_mapping";
        String url2 = "/" + newIndexName + "/";

        Response response = null;
        try {
            response = getClient().performRequest("GET", url2);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            //e.printStackTrace();
        }
        if (NullUtil.isNotNull(response) && response.getStatusLine().getStatusCode() == 200) {
            return true;
        }

        Response indexResponse = null;
        HttpEntity entity = new NStringEntity("", ContentType.APPLICATION_JSON);
        try {
            indexResponse = getClient().performRequest("GET", url, Collections.singletonMap("pretty", "true"), entity);
            String queryResult = EntityUtils.toString(indexResponse.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(queryResult).getJSONObject(oldIndexName);
            String rs = jsonObject.toJSONString();
            entity = new NStringEntity(rs, ContentType.APPLICATION_JSON);
            indexResponse = getClient().performRequest("PUT", url2, Collections.singletonMap("pretty", "true"), entity);
            queryResult = EntityUtils.toString(indexResponse.getEntity());
            jsonObject = JSONObject.parseObject(queryResult);
            return jsonObject.getBoolean("acknowledged");
        } catch (Exception var12) {
            this.logger.error(var12.getMessage(), var12);
            throw new DsbEsRemoteException("创建索引异常", var12);
        }
    }

}
