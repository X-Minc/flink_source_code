/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.elasticsearch.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.ifugle.rap.elasticsearch.api.BusinessCommonApi;
import com.ifugle.rap.elasticsearch.core.ClusterNode;
import com.ifugle.rap.elasticsearch.model.BizException;
import com.ifugle.rap.elasticsearch.model.DataRequest;
import com.ifugle.rap.elasticsearch.model.ResultCode;
import com.ifugle.rap.model.elasticsearch.ElasticSearchResponseVO;
import com.ifugle.rap.model.elasticsearch.InstanceDTO;
import com.ifugle.util.JSONUtil;

/**
 * @author HuangLei(wenyuan)
 * @version $Id: ElasticSearchBusinessApi.java, v 0.1 2018年5月15日 上午11:28:41 HuangLei(wenyuan) Exp $
 */
@Service
public class ElasticSearchBusinessService implements ElasticSearchBusinessApi {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchBusinessService.class);

    private static final String INDEXNAME = "shuixiaomi";

    /**
     * 批量更新的DSL模板
     */
    private static final String SAMPLE_UPDATE_DSL = "{ \"update\": { \"_index\": \"%s\", \"_type\": \"%s\", \"_id\": \"%s\"} } \n\r" + "{ \"doc\" : %s }\n";

    /**
     * 批量插入的DSL模板
     */
    private static final String SAMPLE_INSERT_DSL = "{ \"create\": { \"_index\": \"%s\", \"_type\": \"%s\", \"_id\": \"%s\" }} \n";

    /***
     * 批量插入或者更新的模板
     */
    private static final String SAMPLE_UPDATE_OR_INSERT_DSL = "{ \"update\": { \"_index\": \"%s\", \"_type\": \"%s\", \"_id\": \"%s\" }} \n";


    @Autowired
    private ClusterNode clusterNode;

    @Autowired
    private BusinessCommonApi businessCommonApi;

    @Override
    public boolean exportDataMysqlToEs(String channelType, DataRequest request) {
        String keyId = businessCommonApi.insertOrUpdate(channelType, request.getCatalogType(), getId(request), request.getMap());
        if (StringUtils.isNotBlank(keyId)) {
            return true;
        } else {
            throw new BizException(ResultCode.ERROR, "[ElasticSearchBusinessService] invoke core elasticsearch api error");
        }

    }

    @Override
    public boolean checkDataExistsInEs(String channelType, DataRequest request) {
        Map<String, Object> map = null;
        try {
            map = businessCommonApi.get(channelType, request.getCatalogType(), getId(request));
        } catch (Exception ex) {
            logger.error("查询数据是否存在出错");
        }
        return StringUtils.isNotBlank(ObjectUtils.toString(map, ""));
    }

    /**
     * 批量执行操作
     *
     * @param query
     */
    public void bulkOperation(String query) {
        if (StringUtils.isBlank(query)) {
            return;
        }
        RestClient restClient = clusterNode.getRestClient();
        try {
            // 拼接URL
            String url = "/_bulk";
            // 配置请求参数
            Map<String, String> paramMap = new HashMap<>(2);
            String method = "POST";
            HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
            Response response = restClient.performRequest(method, url, paramMap, entity);
            AnalyzeResponse(response,query);
        } catch (Exception e) {
            logger.error("[ElasticSearchBusinessService] bulkOperation error", e);
        }
    }

    /**
     * 得到更新的DSL
     *
     * @param channelType
     * @param request
     *
     * @return
     */
    public String formatUpdateDSL(String channelType, DataRequest request) {
        Map<String, Object> map = request.getMap();
        String data = JSONUtil.toJSON(map);
        return String.format(SAMPLE_UPDATE_DSL, channelType, request.getCatalogType(), getId(request), data);
    }


    /**
     * 得到更新的DSL
     *
     * @param channelType
     * @param request
     *
     * @return
     */
    public String formatSaveOrUpdateDSL(String channelType, DataRequest request) {
        Map<String, Object> map = request.getMap();
        Map<String, Object> content = new HashMap<>();
        content.put("doc", map);
        content.put("doc_as_upsert", true);
        String data = JSONUtil.toJSON(content);
        StringBuilder dsl = new StringBuilder(String.format(SAMPLE_UPDATE_OR_INSERT_DSL, channelType, request.getCatalogType(), getId(request)));
        dsl.append(data);
        dsl.append(" \n");
        return dsl.toString();
    }

    /**
     * 得到插入的DSL
     *
     * @param channelType
     * @param request
     *
     * @return
     */
    public String formatInsertDSL(String channelType, DataRequest request) {
        StringBuilder dsl = new StringBuilder(String.format(SAMPLE_INSERT_DSL, channelType, request.getCatalogType(), getId(request)));
        Map<String, Object> map = request.getMap();
        String data = JSONUtil.toJSON(map);
        dsl.append(data);
        dsl.append(" \n");
        return dsl.toString();
    }

    private String getId(DataRequest request) {
        return request.getMap().containsKey("ID") ? request.getMap().get("ID").toString() : request.getMap().get("id").toString();
    }

    /**
     * 分析返回数据,判断是否成功执行
     *
     * @param response
     *
     * @return
     */
    private void AnalyzeResponse(Response response,String query) {
        HttpEntity httpEntityRes = response.getEntity();
        try {
            String queryResult = EntityUtils.toString(httpEntityRes);
            Gson gson = new Gson();
            ElasticSearchResponseVO vo = gson.fromJson(queryResult, ElasticSearchResponseVO.class);
            if (vo.isErrors()) {
                getErrorInstance(vo);
            } else {
                if(logger.isDebugEnabled()) {
                    logger.debug("成功发送");
                }
            }
            if(logger.isDebugEnabled()) {
                logger.debug("[ElasticSearchBusinessService] AnalyzeResponse response successful =" + queryResult + ",request object = " + query);
            }
        } catch (ParseException | IOException e) {
            logger.error("[ElasticSearchBusinessService] ParseException,code="+response.getStatusLine().getStatusCode(), e);
        }
    }

    /**
     * 找到错误的执行实例
     */
    private void getErrorInstance(ElasticSearchResponseVO vo) {
        for (Map<String, InstanceDTO> instances : vo.getItems()) {
            for (String key : instances.keySet()) {
                InstanceDTO instance = instances.get(key);
                logger.error(MessageFormat.format("{0}操作执行失败,失败原因为{1}", key, instance.getError()));
            }
        }
    }

}
