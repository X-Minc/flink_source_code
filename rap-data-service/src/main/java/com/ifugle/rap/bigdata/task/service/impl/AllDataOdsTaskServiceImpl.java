package com.ifugle.rap.bigdata.task.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.ifugle.rap.bigdata.task.CompanyOds;
import com.ifugle.rap.bigdata.task.UserAllTag;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.ResponseEntity;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;
import com.ifugle.rap.bigdata.task.service.AllDataOdsTaskService;
import com.ifugle.rap.bigdata.task.service.EsService;
import com.ifugle.rap.bigdata.task.service.EsUserAllTagService;
import com.ifugle.rap.bigdata.task.util.EsKeyUtil;
import com.ifugle.rap.constants.EsCode;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.rap.exception.DsbServiceException;
import com.ifugle.rap.utils.ListUtil;
import com.ifugle.rap.utils.PageUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 13:59
 */
@Service
@Slf4j
public class AllDataOdsTaskServiceImpl implements AllDataOdsTaskService {

    @Autowired
    EsUserAllTagService esUserAllTagService;

    @Autowired
    private EsService esService;

    /**
     * 按纳税人ID查询出用户标签表数据，并更新企业字段
     * @param companyList
     */
    @Override
    public void updateUserAllTagCompanyInfo(List<CompanyOds> companyList) {
        List<List<CompanyOds>> companySplitList = ListUtil.split(companyList, EsCode.ES_PARAM_NUM);
        for (List<CompanyOds> companys : companySplitList) {
            Map<Long, CompanyOds> companyMap = companys.stream().collect(
                    Collectors.toMap(CompanyOds::getNsrId, companyOds -> companyOds));
            List<Long> nsrIds = new ArrayList<>(companyMap.keySet());

            int page = 1;
            int size = EsCode.ES_FIND_PAGE_NUM;
            Map<String, Object> querySearch = Maps.newHashMap();
            querySearch.put("nsr_id", nsrIds);

            SearchResponseEntity<UserAllTag> entity = esUserAllTagService.scrollQueryByPage(querySearch, 3, page, size);
            HitsResponseEntity<UserAllTag> hits = entity.getHits();
            String scrollId = entity.getScrollId();
            int total = hits.getTotal().getValue();
            int totalPage = PageUtils.getTotalPage(total, size);
            updateUserCompanyForHits(hits, companyMap);

            try {
                while (totalPage > page) {
                    page++;
                    hits = esUserAllTagService.scrollQueryByPage(scrollId, 3, size);
                    updateUserCompanyForHits(hits, companyMap);
                }
            } catch (DsbServiceException e) {
                esUserAllTagService.deleteScrollQueryByPage(scrollId);
                throw new DsbServiceException(e);
            }
        }
    }


    /**
     * 更新用户标签表中的企业信息字段
     * @param hits
     * @param companyMap
     */
    private void updateUserCompanyForHits(HitsResponseEntity<UserAllTag> hits, Map<Long, CompanyOds> companyMap) {
        Map<String, UserAllTag> updateMap = Maps.newHashMap();
        List<ResponseEntity<UserAllTag>> list = hits.getHits();
        for (ResponseEntity<UserAllTag> responseEntity : list) {
            UserAllTag allTag = responseEntity.getSource();
            CompanyOds company = companyMap.get(allTag.getNsrId());
            UserAllTag updateTag = new UserAllTag();
            updateTag.setDjzclxDm(company.getDjzclxDm());
            updateTag.setHyDm(company.getHyDm());
            updateTag.setZzsnslx(company.getZzsnslx());
            updateTag.setXxwlqyBj(company.getXxwlqyBj());
            updateMap.put(EsKeyUtil.getUserAllTagKey(allTag), updateTag);
        }
        if (updateMap.size() > 0) {
            esService.multiUpdate(EsIndexConstant.USER_ALL_TAG, updateMap);
        }
    }
}
