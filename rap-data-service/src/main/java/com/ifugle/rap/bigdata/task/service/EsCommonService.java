package com.ifugle.rap.bigdata.task.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.ifugle.rap.elasticsearch.model.RealtimeSearchResponse;

/**
 * @author XuWeigang
 * @since 2019年07月25日 9:38
 */
public interface EsCommonService {
    String insertOrUpdate(String index, String keyId, Map<String, Object> source);

    Map<String, Object> get(String index, String keyId);

    boolean delete(String index, String keyId);

    RealtimeSearchResponse search(String index, String queryDSL, Pageable pageable);

    RealtimeSearchResponse searchByDSL(String index, String queryDSL);
}
