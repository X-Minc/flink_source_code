package com.ifugle.rap.bigdata.task.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.CompanyAllTag;
import com.ifugle.rap.bigdata.task.CompanyOds;
import com.ifugle.rap.bigdata.task.YhzxXnzzNsrBq;
import com.ifugle.rap.bigdata.task.service.AllDataOdsTaskService;
import com.ifugle.rap.bigdata.task.service.CompanyOdsService;
import com.ifugle.rap.bigdata.task.service.EsCompanyAllTagService;
import com.ifugle.rap.bigdata.task.service.EsCompanyRealtimeService;
import com.ifugle.rap.bigdata.task.service.EsDepartOdsService;
import com.ifugle.rap.bigdata.task.service.EsService;
import com.ifugle.rap.bigdata.task.service.EsUserAllTagService;
import com.ifugle.rap.bigdata.task.util.EsKeyUtil;
import com.ifugle.rap.constants.DsbCode;
import com.ifugle.rap.constants.EsCode;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.rap.utils.ListUtil;
import com.ifugle.util.DateUtil;
import com.ifugle.util.NullUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 13:48
 */
@Service
@Slf4j
public class EsCompanyRealtimeServiceImpl implements EsCompanyRealtimeService {


    @Autowired
    private EsService esService;

    @Autowired
    private CompanyOdsService companyOdsService;

    @Autowired
    private EsDepartOdsService esDepartOdsService;

    @Autowired
    private EsUserAllTagService esUserAllTagService;

    @Autowired
    AllDataOdsTaskService allDataOdsTaskService;

    @Autowired
    EsCompanyAllTagService esCompanyAllTagService;
    /**
     * 企业状态未删除的状态码列表
     */
    private static final List<Byte> EXCLUDE_NSRZT = Arrays.asList(new Byte[]{7, 8, 10, 13});

    /**
     * 从增量企业数据更新到全量企业实时标签表
     * @param xnzz
     * @param startDate
     * @return
     */
    @Override
    public Set<Long> updateCompanyRealTimeByAdd(BiDmSwjg xnzz, Date startDate) {
        log.info("执行处理企业增量实时标签表开始：xnzzId = {}, startTime = {}", xnzz.getXnzzId(), DateUtil.toISO8601DateTime(startDate));
        String gxrq = DateUtil.format(new Date(), DateUtil.ISO8601_DATE);
        List<CompanyOds> companyList = null;
        Set<Long> updateBmIdsSet = Sets.newHashSet();
        long startTimeMillis = System.currentTimeMillis();

        log.info("执行处理企业增量实时标签表开始：xnzzId = {}", xnzz.getXnzzId());

        long start1 = System.currentTimeMillis();
        Map<Long, List<Long>> allBmParentIds = esDepartOdsService.getAllDepartBmIds(xnzz.getXnzzId());
        long time1 = (System.currentTimeMillis() - start1) / 1000;
        log.info("执行处理企业增量实时标签表，查询部门列表：xnzzId = {}， times = {}", xnzz.getXnzzId(), time1);

        int count = 0;
        Long startId = null;
        while (true) {
            long start2 = System.currentTimeMillis();
            companyList = companyOdsService.listByUpdateCompany(xnzz.getXnzzId(), startId, startDate, null);
            long time2 = (System.currentTimeMillis() - start2) / 1000;
            log.info("执行处理企业增量实时标签表，查询企业增量数据：xnzzId = {}， times = {}", xnzz.getXnzzId(), time2);
            if (NullUtil.isNull(companyList)) {
                break;
            }
            // 设置字段值
            Set<Long> updateBmIds = setField(companyList, xnzz, allBmParentIds);
            updateBmIdsSet.addAll(updateBmIds);

            // 获取调整部门的数据的原bmIds
            long start3 = System.currentTimeMillis();
            Set<Long> updateBmIdsForOld = updateBmIdCompany(companyList);
            if (NullUtil.isNotNull(updateBmIdsForOld)) {
                updateBmIdsSet.addAll(updateBmIdsForOld);
            }
            long time3 = (System.currentTimeMillis() - start3) / 1000;
            log.info("执行处理企业增量实时标签表，查询企业有调整部门的数据：xnzzId = {}， times = {}", xnzz.getXnzzId(), time3);

            // 更新到ES
            long start4 = System.currentTimeMillis();
            count += updateDataToEs(xnzz.getXnzzId(), companyList, gxrq);
            long time4 = (System.currentTimeMillis() - start4) / 1000;
            log.info("执行处理企业增量实时标签表，更新企业数据到ES：xnzzId = {}， times = {}", xnzz.getXnzzId(), time4);

            // 按纳税人ID查询出用户标签表数据，并更新企业字段
            long start5 = System.currentTimeMillis();
            allDataOdsTaskService.updateUserAllTagCompanyInfo(companyList);
            long time5 = (System.currentTimeMillis() - start5) / 1000;
            log.info("执行处理企业增量实时标签表，更新用户标签表中的企业字段：xnzzId = {}， times = {}", xnzz.getXnzzId(), time5);

            if (companyList.size() < EsCode.ES_PAGE_NUM) {
                break;
            }

            startId = companyList.get(companyList.size() - 1).getNsrId();
        }

        long time = (System.currentTimeMillis() - startTimeMillis) / 1000;
        log.info("执行处理企业增量实时标签表结束：xnzzId = {}，共更新数据{}条, 需要汇总的部门数量为{}，耗时{}s",
                xnzz.getXnzzId(), count, updateBmIdsSet.size(), time);

        return updateBmIdsSet;
    }


    /**
     * 设置字段值
     * @param companyList
     * @param xnzz
     * @param allBmParentIds
     * @return 返回需要进行汇总的部门
     */
    private Set<Long> setField(List<CompanyOds> companyList, BiDmSwjg xnzz, Map<Long, List<Long>> allBmParentIds) {
        // 获取企业标签
        List<Long> nsrIdList = companyList.stream().map(CompanyOds::getNsrId).collect(Collectors.toList());
        List<List<Long>> nsrIdsList = ListUtil.split(nsrIdList, 300);
        Map<Long, List<Long>> nsrBqsMap = Maps.newHashMap();

        long start = System.currentTimeMillis();
        for (List<Long> nsrIds : nsrIdsList) {
            // 根据nsrId获取每个企业的企业标签
            List<YhzxXnzzNsrBq> bqList = companyOdsService.listBqByNsrId(xnzz.getXnzzId(), nsrIds);
            for (YhzxXnzzNsrBq bq : bqList) {
                if (NullUtil.isNotNull(bq.getBqIds())) {
                    // 多个标签以逗号分隔，转换成List
                    List<Long> bqIdList = Lists.newArrayList();
                    String[] bqIds = bq.getBqIds().split(",");
                    for (String bqId : bqIds) {
                        bqIdList.add(Long.valueOf(bqId));
                    }
                    nsrBqsMap.put(bq.getNsrId(), bqIdList);
                }
            }
        }
        long time = (System.currentTimeMillis() - start) / 1000;
        log.info("执行处理企业增量实时标签表，查询企业标签列表：xnzzId = {}， times = {}", xnzz.getXnzzId(), time);

        Set<Long> updateBmIds = Sets.newHashSet();
        for (CompanyOds companyOds : companyList) {
            companyOds.setSsswjgDm(xnzz.getSwjgDm());
            companyOds.setBmIds(allBmParentIds.get(companyOds.getBmId()));
            companyOds.setBqIds(nsrBqsMap.get(companyOds.getNsrId()));
            // 添加需要进行汇总的部门
            if (NullUtil.isNotNull(companyOds.getBmIds())) {
                updateBmIds.addAll(companyOds.getBmIds());
            }
        }
        return updateBmIds;
    }

    /**
     * 部门更新的数据的原bmIds
     * @param companyList
     * @return
     */
    private Set<Long> updateBmIdCompany(List<CompanyOds> companyList) {
        Set<Long> updateBmIds = Sets.newHashSet();
        List<List<CompanyOds>> splitList = ListUtil.split(companyList, 5000);

        Map<String, CompanyOds> companyMap = null;
        List<String> keys = null;
        // 修改部门的企业
        Map<Long, CompanyOds> updateBmCompanyMap = Maps.newHashMap();

        for (List<CompanyOds> list : splitList) {
            companyMap = Maps.newHashMap();
            keys = Lists.newArrayList();
            for (CompanyOds company : list) {
                companyMap.put(EsKeyUtil.getCompanyOdsKey(company), company);
                keys.add(EsKeyUtil.getCompanyOdsKey(company));
            }

            List<CompanyAllTag> companyAllTagList = esCompanyAllTagService.listByKeyIds(keys);
            if (NullUtil.isNull(companyAllTagList)) {
                continue;
            }

            // 判断部门是否有修改
            for (CompanyAllTag allTag : companyAllTagList) {
                String key = EsKeyUtil.getCompanyAllTagKey(allTag);
                CompanyOds company = companyMap.get(key);
                if (NullUtil.isNull(company)) {
                    continue;
                }
                if (NullUtil.isNotNull(allTag.getBmId()) && !allTag.getBmId().equals(company.getBmId())) {
                    List<Long> bmIds = allTag.getBmIds();
                    if (NullUtil.isNotNull(bmIds)) {
                        updateBmIds.addAll(bmIds);
                    }
                    // 记录有修改部门的企业
                    updateBmCompanyMap.put(company.getNsrId(), company);
                }
            }
        }

        // 更新中间表企业用户部门ID
        if (updateBmCompanyMap.size() > 0) {
            esUserAllTagService.updateTpcQyryBm(updateBmCompanyMap);
        }

        return updateBmIds;
    }

    /**
     * 更新数据到ES
     *
     * @param xnzzId
     * @param companyList
     * @param gxrq
     * @return 返回更新数据条数
     */
    private int updateDataToEs(Long xnzzId, List<CompanyOds> companyList, String gxrq) {
        List<Long> nsrIdList = companyList.stream().map(CompanyOds::getNsrId).collect(Collectors.toList());

        // 查询企业其他字段
        Map<Long, CompanyOds> companyOdsMap = Maps.newHashMap();
        List<List<Long>> nsrIdsList = ListUtil.split(nsrIdList, 300);
        for (List<Long> nsrIds : nsrIdsList) {
            List<CompanyOds> companyOdsList = companyOdsService.listByIds(xnzzId, nsrIds);
            Map<Long, CompanyOds> map = companyOdsList.stream().collect(Collectors.toMap(CompanyOds::getNsrId, yhzxXnzzNsr -> yhzxXnzzNsr));
            companyOdsMap.putAll(map);
        }

        // 获取企业标签ID，按nsrId分组
        Map<Long, List<Long>> nsrBqsMap = Maps.newHashMap();
        for (List<Long> nsrIds : nsrIdsList) {
            List<YhzxXnzzNsrBq> bqList = companyOdsService.listBqByNsrId(xnzzId, nsrIds);
            for (YhzxXnzzNsrBq bq : bqList) {
                if (NullUtil.isNotNull(bq.getBqIds())) {
                    List<Long> bqIdList = Lists.newArrayList();
                    String[] bqIds = bq.getBqIds().split(",");
                    for (String bqId : bqIds) {
                        bqIdList.add(Long.valueOf(bqId));
                    }
                    nsrBqsMap.put(bq.getNsrId(), bqIdList);
                }
            }
        }

        Map<String, CompanyAllTag> companyTagList = Maps.newHashMap();
        for (CompanyOds companyOds : companyList) {
            CompanyAllTag companyTag = new CompanyAllTag();
            // 更新日期
            companyTag.setGxrq(gxrq);
            setCompanyTagData(companyTag, companyOds);
            companyTagList.put(EsKeyUtil.getCompanyAllTagKey(companyTag), companyTag);
        }

        int size = 0;
        if (companyTagList.size() > 0) {
            esService.multiInsertOrUpdate(EsIndexConstant.COMPANY_ALL_TAG, companyTagList);
            size = companyTagList.size();
        }
        return size;
    }

    /**
     * 设置企业标签表字段值
     *
     * @param companyTag
     * @param companyOds
     */
    private void setCompanyTagData(CompanyAllTag companyTag, CompanyOds companyOds) {
        BeanUtils.copyProperties(companyOds, companyTag);
        companyTag.setDjzclxTag(companyOds.getDjzclxDm());
        companyTag.setDzxwqyTag(companyOds.getDzxwqyBj());
        companyTag.setZzsnslxTag(companyOds.getZzsnslx());
        companyTag.setXxwlqyTag(companyOds.getXxwlqyBj());
        companyTag.setSwwblTag(companyOds.getSwwblh());
        // 激活标记
        if (DsbCode.Jhbj.YJH.equals(companyOds.getJhbj()) && !EXCLUDE_NSRZT.contains(companyOds.getNsrztDm())) {
            companyTag.setJhTag(EsCode.YES);
        } else {
            companyTag.setJhTag(EsCode.NO);
        }
        // 在册标记
        if (companyOds.getIsDelete() || EXCLUDE_NSRZT.contains(companyOds.getNsrztDm())) {
            companyTag.setZcTag(EsCode.NO);
        } else {
            companyTag.setZcTag(EsCode.YES);
        }
    }
}
