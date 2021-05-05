package com.ifugle.rap.bigdata.task.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifugle.rap.elasticsearch.entity.HitEntity;
import com.ifugle.rap.elasticsearch.entity.QueryEntity;
import com.ifugle.rap.elasticsearch.model.PageResponse;

/**
 * @author XuWeigang
 * @since 2021年02月20日 11:04
 */
@Service
public class EsTemplateRepository<T> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RestClient restClient;

    public Response putSyncObject(String index, String id, HttpEntity entity) {
        Response indexResponse = null;

        try {
            String url = "/" + index;
            Map<String, String> paramMap = new HashMap();
            paramMap.put("pretty", "true");
            String method = "POST";
            if (StringUtils.isNotBlank(id)) {
                url = url + "/" + id;
                method = "PUT";
            }

            indexResponse = this.restClient.performRequest(method, url, paramMap, entity, new Header[0]);
        } catch (IOException var9) {
            this.logger.error(var9.getMessage(), var9);
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
        }

        return indexResponse;
    }

    public HitEntity<Map<String, Object>> get(String index, String id) {
        Response indexResponse = null;
        String url = "/" + index + "/" + id;

        try {
            Map<String, String> paramMap = new HashMap();
            paramMap.put("pretty", "true");
            indexResponse = this.restClient.performRequest("GET", url, paramMap, new Header[0]);
            HttpEntity entity = indexResponse.getEntity();
            ObjectMapper mapper = new ObjectMapper();
            String queryResult = EntityUtils.toString(entity);
            HitEntity<Map<String, Object>> results = (HitEntity)mapper.readValue(queryResult,
                    new TypeReference<HitEntity<Map<String, Object>>>() {
            });
            return results;
        } catch (Exception var11) {
            this.logger.error("[TemplateRepository] data is not exist url =" + url);
            return null;
        }
    }

    public Response delete(String index, String id) {
        Response indexResponse = null;

        try {
            String url = "/" + index + "/" + id;
            Map<String, String> paramMap = new HashMap();
            paramMap.put("pretty", "true");
            indexResponse = this.restClient.performRequest("DELETE", url, paramMap, new Header[0]);
        } catch (IOException var7) {
            this.logger.error(var7.getMessage(), var7);
        } catch (Exception var8) {
            this.logger.error(var8.getMessage(), var8);
        }

        return indexResponse;
    }

    public PageResponse<T> queryDSLByPage(String index, String query, Pageable pageable) {
        PageResponse<T> pageResponse = new PageResponse();
        StringBuffer queryDSL = new StringBuffer();

        try {
            String url = "/" + index + "/_search";


            int from = pageable.getPageNumber() * pageable.getPageSize();
            if (query != null && query.length() > 0) {
                queryDSL.append("{\"from\":").append(from).append(", \"size\" :").append(pageable.getPageSize());
                if (query.contains("\"query\"")) {
                    queryDSL.append(",");
                } else {
                    queryDSL.append(",\"query\":");
                }

                queryDSL.append(query).append("}");
                if (this.logger.isInfoEnabled()) {
                    this.logger.warn("queryDSL：" + queryDSL.toString());
                }
            }

            Map<String, String> paramMap = new HashMap();
            paramMap.put("pretty", "true");
            HttpEntity entity = new NStringEntity(queryDSL.toString(), ContentType.APPLICATION_JSON);
            Response indexResponse = this.restClient.performRequest("GET", url, paramMap, entity, new Header[0]);
            pageResponse = this.pareseJsonToPages(indexResponse.getEntity(), pageable);
        } catch (ParseException var12) {
            this.logger.error(var12.getMessage(), var12);
        } catch (IOException var13) {
            this.logger.error(var13.getMessage(), var13);
        } catch (Exception var14) {
            this.logger.error(var14.getMessage(), var14);
        }

        return pageResponse;
    }

    public PageResponse<T> pareseJsonToPages(HttpEntity entity, Pageable pageable) {
        PageResponse<T> pageResponse = new PageResponse();
        Page<HitEntity<T>> returnPage = null;
        List<HitEntity<T>> content = new ArrayList();
        ObjectMapper mapper = new ObjectMapper();

        try {
            String queryResult = EntityUtils.toString(entity);
            QueryEntity<T> results = (QueryEntity)mapper.readValue(queryResult, new TypeReference<QueryEntity<T>>() {
            });
            if (results != null && results.getHits().getHits().size() > 0) {
                List<HitEntity<T>> hits = results.getHits().getHits();
                content.addAll(hits);
            }

            Object total = results.getHits().getTotal();
            if (total instanceof Integer) {
                returnPage = new PageImpl(content, pageable, Long.valueOf((long)(Integer)total));
            } else if (total instanceof HashMap) {
                Long tol = Long.valueOf(((HashMap)total).get("value").toString());
                returnPage = new PageImpl(content, pageable, tol);
            }

            pageResponse.setPage(returnPage);
            pageResponse.setQueryEntitys(results);
        } catch (ParseException var11) {
            this.logger.error(var11.getMessage(), var11);
        } catch (IOException var12) {
            this.logger.error(var12.getMessage(), var12);
        } catch (Exception var13) {
            this.logger.error(var13.getMessage(), var13);
        }

        return pageResponse;
    }

    public QueryEntity<T> queryListByDSL(String index, String query) {
        StringBuffer urlSB = new StringBuffer(16);
        urlSB.append("/").append(index).append("/_search");
        QueryEntity<T> results = null;
        Map<String, String> paramMap = new HashMap();
        paramMap.put("pretty", "true");
        Response indexResponse = null;
        NStringEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);

        try {
            indexResponse = this.restClient.performRequest("GET", urlSB.toString(), paramMap, entity, new Header[0]);
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
                results = (QueryEntity)mapper.readValue(queryResult, new TypeReference<QueryEntity<T>>() {
                });
                return results;
            } catch (Exception var11) {
                this.logger.error(MessageFormat.format("queryListByDSL transport json to List error , msg : {0}", var11));
                return results;
            }
        }
    }
}
