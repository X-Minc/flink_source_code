package com.ifugle.rap.bigdata.task.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifugle.rap.bigdata.task.service.EsCommonService;
import com.ifugle.rap.bigdata.task.service.EsTemplateRepository;
import com.ifugle.rap.elasticsearch.entity.BizException;
import com.ifugle.rap.elasticsearch.entity.BucketEntity;
import com.ifugle.rap.elasticsearch.entity.HitEntity;
import com.ifugle.rap.elasticsearch.entity.QueryEntity;
import com.ifugle.rap.elasticsearch.entity.SimpleHitEntity;
import com.ifugle.rap.elasticsearch.model.RealtimeSearchResponse;
import com.ifugle.util.JSONUtil;
import com.ifugle.util.NullUtil;

/**
 * @author XuWeigang
 * @since 2019年07月25日 9:39
 */
@Service
public class EsCommonServiceImpl implements EsCommonService {
    private static final Logger logger = LoggerFactory.getLogger(EsCommonServiceImpl.class);

    @Autowired
    private EsTemplateRepository<Map<String, Object>> templateRepository;

    public EsCommonServiceImpl() {

    }

    @Override
    public String insertOrUpdate(String index, String keyId, Map<String, Object> source) {
        if (source != null && index != null) {
            String json = JSONUtil.toJSON(source);
            HttpEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);

            if (NullUtil.isNotNull(keyId)) {
                // 进行URL编码
                keyId = encodeUrl(keyId);
            }

            Response response = this.templateRepository.putSyncObject(index, keyId, httpEntity);
            HttpEntity httpEntityRes = response.getEntity();

            try {
                String queryResult = EntityUtils.toString(httpEntityRes);
                ObjectMapper mapper = new ObjectMapper();
                SimpleHitEntity<Map<String, Object>> results = (SimpleHitEntity) mapper
                        .readValue(queryResult, new TypeReference<SimpleHitEntity<Map<String, Object>>>() {
                        });
                if (results != null) {
                    keyId = results.getId();
                }
            } catch (ParseException var12) {
                logger.error("", var12);
            } catch (IOException var13) {
                logger.error("", var13);
            }

            return keyId;
        } else {
            throw new BizException("[BusinessCommonService] input param is null,index or source ,please check it");
        }
    }

    @Override
    public Map<String, Object> get(String index, String keyId) {
        Map<String, Object> map = null;
        if (!StringUtils.isBlank(keyId) && index != null) {
            // 进行URL编码
            keyId = encodeUrl(keyId);

            HitEntity<Map<String, Object>> response = this.templateRepository.get(index, keyId);
            if (logger.isInfoEnabled()) {
                logger.info("[BusinessCommonService] query id =" + keyId);
            }

            if (response != null) {
                map = (Map) response.getSource();
            }

            return map;
        } else {
            throw new BizException("[BusinessCommonService] input param is null,index or keyId ,please check it");
        }
    }

    @Override
    public boolean delete(String index, String keyId) {
        if (!StringUtils.isBlank(keyId) && index != null) {
            // 进行URL编码
            keyId = encodeUrl(keyId);
            Response response = this.templateRepository.delete(index, keyId);
            return response != null;
        } else {
            throw new BizException("[BusinessCommonService] input param is null,index or keyId ,please check it");
        }
    }

    @Override
    public RealtimeSearchResponse search(String index, String queryDSL, Pageable pageable) {
        if (StringUtils.isBlank(queryDSL)) {
            try {
                queryDSL = this.searchQueryDSL((Map) null);
            } catch (Exception var7) {
                logger.error(MessageFormat.format("Query init error msg : {0}", var7));
            }
        }

        StringBuffer queryDSLSB = new StringBuffer(queryDSL);
        queryDSLSB.deleteCharAt(queryDSLSB.indexOf("{")).deleteCharAt(queryDSLSB.lastIndexOf("}"));
        Page<HitEntity<Map<String, Object>>> list =  this.templateRepository
                .queryDSLByPage(index, queryDSLSB.toString(),
                        PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize())).getPage();
        logger.debug(
                MessageFormat.format("[BusinessCommonService] api invoke search queryDSL: {0}, index : {1}, type:{2}", queryDSL, index));
        return this.analysisPageEntity(list);
    }

    @Override
    public RealtimeSearchResponse searchByDSL(String index, String queryDSL) {
        if (StringUtils.isBlank(queryDSL)) {
            logger.error("DSL can't be null");
            return null;
        } else {
            QueryEntity<Map<String, Object>> result = this.templateRepository.queryListByDSL(index, queryDSL);
            return this.analysisListEntity(result);
        }
    }

    private String searchQueryDSL(Map<String, Object> querySearch) throws Exception {
        StringBuffer queryBuffer = new StringBuffer(32);
        if (querySearch == null) {
            throw new Exception("Query can't be null");
        } else {
            queryBuffer.append("{\"bool\" : ");
            queryBuffer.append("{\"must\" : ");
            queryBuffer.append("[");
            Iterator iterator = querySearch.keySet().iterator();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                queryBuffer.append("{\"match\" : {\"" + key + "\" : \"" + querySearch.get(key) + "\"}}, ");
            }

            queryBuffer.deleteCharAt(queryBuffer.lastIndexOf(","));
            queryBuffer.append("]");
            queryBuffer.append("}}");
            logger.info(queryBuffer.toString() + "=================================");
            return queryBuffer.toString();
        }
    }

    private RealtimeSearchResponse analysisListEntity(QueryEntity<Map<String, Object>> results) {
        RealtimeSearchResponse mapList = null;
        List<Map<String, Object>> resultList = new ArrayList();
        if (results != null) {
            if (results != null && results.getHits() != null && !CollectionUtils.isEmpty(results.getHits().getHits())) {
                List<HitEntity<Map<String, Object>>> hits = results.getHits().getHits();
                Iterator var5 = hits.iterator();

                while (var5.hasNext()) {
                    HitEntity<Map<String, Object>> hitEntity = (HitEntity) var5.next();
                    Map<String, Object> map = (Map) hitEntity.getSource();
                    map.put("_score", hitEntity.getScore());
                    resultList.add(map);
                }
            }

            Map<String, List<RealtimeSearchResponse.Aggregation>> aggregations = new HashMap();
            List<RealtimeSearchResponse.Aggregation> aggregationsList = new ArrayList();
            if (results != null && results.getAggregations() != null && results.getAggregations().getRepeatedQuestions() != null) {
                List<BucketEntity<Map<String, Object>>> list = results.getAggregations().getRepeatedQuestions().getBuckets();
                if (!CollectionUtils.isEmpty(list)) {
                    Iterator var13 = list.iterator();

                    while (var13.hasNext()) {
                        BucketEntity<Map<String, Object>> bucketEntity = (BucketEntity) var13.next();
                        RealtimeSearchResponse.Aggregation agg = new RealtimeSearchResponse.Aggregation();
                        agg.setDocCount(bucketEntity.getDocCount());
                        agg.setKey(bucketEntity.getKey());
                        aggregationsList.add(agg);
                    }
                }

                aggregations.put("repeated_questions", aggregationsList);
            }

            mapList = new RealtimeSearchResponse();
            mapList.setHits(resultList);
            mapList.setAggregations(aggregations);
        }

        return mapList;
    }

    private RealtimeSearchResponse analysisPageEntity(Page<HitEntity<Map<String, Object>>> list) {
        RealtimeSearchResponse mapList = null;
        if (list != null && list.hasContent()) {
            List<Map<String, Object>> resultList = new ArrayList();
            Iterator var4 = list.iterator();

            while (var4.hasNext()) {
                HitEntity<Map<String, Object>> hitEntity = (HitEntity) var4.next();
                Map<String, Object> map = (Map) hitEntity.getSource();
                map.put("_score", hitEntity.getScore());
                resultList.add(map);
            }

            mapList = new RealtimeSearchResponse();
            mapList.setHits(resultList);
            mapList.setPage(list.getTotalPages());
            mapList.setTotal(list.getTotalElements());
        }

        return mapList;
    }

    /**
     * 进行URL编码
     *
     * @param value
     *
     * @return
     */
    private String encodeUrl(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("URL编码失败", e);
            return value;
        }
    }
}
