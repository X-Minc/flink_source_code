package com.ifugle.rap.bigdata.task.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.reflect.TypeToken;
import com.ifugle.rap.bigdata.task.CompanyAllTag;
import com.ifugle.rap.bigdata.task.es.DslRequestBuild;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.MgetResponseEntity;
import com.ifugle.rap.bigdata.task.es.ResponseEntity;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;
import com.ifugle.rap.bigdata.task.service.EsCompanyAllTagService;
import com.ifugle.rap.bigdata.task.service.EsService;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.util.NullUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年07月26日 17:09
 */
@Service
@Slf4j
public class EsCompanyAllTagServiceImpl implements EsCompanyAllTagService {
    @Autowired
    private EsService<CompanyAllTag> esService;

    @Override
    public List<CompanyAllTag> listByKeyIds(List<String> keyIds) {
        List<ResponseEntity<CompanyAllTag>> result = esService.multiGet(EsIndexConstant.COMPANY_ALL_TAG,
                keyIds, new TypeToken<MgetResponseEntity<CompanyAllTag>>() {
                }.getType());
        if (NullUtil.isNull(result)) {
            return new ArrayList<>();
        }

        List<CompanyAllTag> companyAllTagList = result.stream().map(entity -> entity.getSource()).collect(Collectors.toList());
        return companyAllTagList;
    }

    @Override
    public SearchResponseEntity<CompanyAllTag> scrollQueryByPage(Map<String, Object> querySearch, int scrollTime, int page, int size) {
        SearchResponseEntity<CompanyAllTag> entity = esService.scrollQueryByPage(EsIndexConstant.COMPANY_ALL_TAG,
                querySearch, new TypeToken<SearchResponseEntity<CompanyAllTag>>() {
                }.getType(), scrollTime, page, size);
        return entity;
    }

    @Override
    public SearchResponseEntity<CompanyAllTag> scrollQueryByPage(DslRequestBuild build, int scrollTime, int page, int size) {
        SearchResponseEntity<CompanyAllTag> entity = esService
                .scrollQueryByPage(EsIndexConstant.COMPANY_ALL_TAG, new TypeToken<SearchResponseEntity<CompanyAllTag>>() {
                }.getType(), build, scrollTime, page, size);
        return entity;
    }

    @Override
    public HitsResponseEntity<CompanyAllTag> scrollQueryByPage(String scrollId, int scrollTime, int size) {
        SearchResponseEntity<CompanyAllTag> entity = esService.scrollQueryByPage(scrollId, scrollTime,
                new TypeToken<SearchResponseEntity<CompanyAllTag>>() {
                }.getType());
        HitsResponseEntity<CompanyAllTag> hits = entity.getHits();
        if (NullUtil.isNull(hits) || NullUtil.isNull(hits.getHits()) || hits.getHits().size() < size) {
            esService.deleteScrollQueryByPage(scrollId);
        }
        return hits;
    }

    @Override
    public void deleteScrollQueryByPage(String scrollId) {
        esService.deleteScrollQueryByPage(scrollId);
    }
}
