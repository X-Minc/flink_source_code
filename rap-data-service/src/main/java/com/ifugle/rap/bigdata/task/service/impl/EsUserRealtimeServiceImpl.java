package com.ifugle.rap.bigdata.task.service.impl;

import java.util.ArrayList;
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
import com.ifugle.rap.bigdata.task.CompanyOds;
import com.ifugle.rap.bigdata.task.UserAllTag;
import com.ifugle.rap.bigdata.task.service.CompanyOdsService;
import com.ifugle.rap.bigdata.task.service.EsDepartOdsService;
import com.ifugle.rap.bigdata.task.service.EsService;
import com.ifugle.rap.bigdata.task.service.EsUserAllTagService;
import com.ifugle.rap.bigdata.task.service.EsUserRealtimeService;
import com.ifugle.rap.bigdata.task.service.UserOdsService;
import com.ifugle.rap.bigdata.task.util.EsKeyUtil;
import com.ifugle.rap.constants.DsbCode;
import com.ifugle.rap.constants.EsCode;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.rap.constants.SjtjConfig;
import com.ifugle.rap.utils.ListUtil;
import com.ifugle.rap.utils.UserOds;
import com.ifugle.util.DateUtil;
import com.ifugle.util.NullUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 12:56
 */
@Service
@Slf4j
public class EsUserRealtimeServiceImpl implements EsUserRealtimeService {

    @Autowired
    private EsDepartOdsService esDepartOdsService;

    @Autowired
    UserOdsService userOdsService;

    @Autowired
    EsUserRealtimeService esUserRealtimeService;

    @Autowired
    EsUserAllTagService esUserAllTagService;

    @Autowired
    EsService esService;

    @Autowired
    CompanyOdsService companyOdsService;


    /**
     * 从增量用户数据更新到全量用户实时标签表
     * @param xnzz
     * @param startDate
     * @return 返回需要汇总的虚拟组织及其部门
     */
    @Override
    public Set<Long> updateUserRealTimeByAdd(BiDmSwjg xnzz, Date startDate) {
        log.info("执行处理用户增量实时标签表开始：xnzzId = {}, startTime = {}", xnzz.getXnzzId(), DateUtil.toISO8601DateTime(startDate));
        // 更新时间
        String gxrq = DateUtil.format(new Date(), DateUtil.ISO8601_DATE);
        // 中间表虚拟组织ID
        List<Long> tpcXnzzIds = SjtjConfig.getTpcXnzzIds();
        // 记录需要重新汇总的部门ID
        Set<Long> updateBmIdsSet = Sets.newHashSet();

        int dataSum = 0;
        long startTimeMillis = System.currentTimeMillis();

        log.info("执行处理用户增量实时标签表开始：xnzzId = {}", xnzz.getXnzzId());

        long start1 = System.currentTimeMillis();
        Map<Long, List<Long>> allBmParentIds = esDepartOdsService.getAllDepartBmIds(xnzz.getXnzzId());
        long time1 = (System.currentTimeMillis() - start1) / 1000;
        log.info("执行处理用户增量实时标签表，查询部门列表：xnzzId = {}，times = {}", xnzz.getXnzzId(), time1);

        // 更新user数据
        int count = updateForUser(xnzz, startDate, allBmParentIds, updateBmIdsSet, gxrq, "0");
        log.info("执行处理丁税宝用户增量实时数据：xnzzId = {}，更新了{}条数据", xnzz.getXnzzId(), count);
        dataSum += count;

        // 更新中间表数据
        if (tpcXnzzIds.contains(xnzz.getXnzzId())) {
            count = updateForUser(xnzz, startDate, allBmParentIds, updateBmIdsSet, gxrq, "1");
            count += updateForUser(xnzz, startDate, allBmParentIds, updateBmIdsSet, gxrq, "3");
            log.info("执行处理机构人员中间表用户增量实时数据：xnzzId = {}，更新了{}条数据", xnzz.getXnzzId(), count);
            dataSum += count;

            count = updateForUser(xnzz, startDate, allBmParentIds, updateBmIdsSet, gxrq, "2");
            log.info("执行处理企业人员中间表用户增量实时数据：xnzzId = {}，更新了{}条数据", xnzz.getXnzzId(), count);
            dataSum += count;
        }

        long time = (System.currentTimeMillis() - startTimeMillis) / 1000;
        log.info("执行处理用户增量实时标签表结束：xnzzId = {}，共更新数据{}条，需要重新汇总的部门数 = {}，耗时{}s",
                xnzz.getXnzzId(), dataSum, updateBmIdsSet.size(), time);

        return updateBmIdsSet;
    }


    /**
     * 增量更新用户实时数据到ES
     * @param xnzz
     * @param startDate
     * @param allBmParentIds
     * @param updateBmIdsSet
     * @param gxrq
     * @param type
     * @return
     */
    private int updateForUser(BiDmSwjg xnzz, Date startDate, Map<Long, List<Long>> allBmParentIds,
            Set<Long> updateBmIdsSet, String gxrq, String type) {
        int count = 0;
        Long startId = null;
        List<UserOds> userList = null;
        Map<String, UserAllTag> userAllTagMap = null;
        while (true) {
            long start1 = System.currentTimeMillis();
            if ("0".equals(type)) {
                // 查询用户表
                userList = userOdsService.listByUpdateUser(xnzz.getXnzzId(), startId, startDate, null);
            } else if ("1".equals(type)) {
                // 查询机构人员中间表（根据中间表机构人员更新时间判断）
                userList = userOdsService.listByUpdateTpcJgry(xnzz.getXnzzId(), startId, startDate, null);
            } else if ("2".equals(type)) {
                // 查询企业人员中间表
                userList = userOdsService.listByUpdateTpcQyry(xnzz.getXnzzId(), startId, startDate, null);
            } else if ("3".equals(type)) {
                // 查询机构人员中间表(根据部门映射更新时间判断)
                userList = userOdsService.listByUpdateTpcJgryByBmys(xnzz.getXnzzId(), startId, startDate, null);
            }
            long time1 = (System.currentTimeMillis() - start1) / 1000;
            log.info("执行处理用户增量实时标签表，查询用户增量数据：xnzzId = {}，times = {}", xnzz.getXnzzId(), time1);

            if (NullUtil.isNull(userList)) {
                // 如果没有需要更新的数据则跳过
                break;
            }

            // 设置字段值
            Set<Long> updateBmIds = setField(userList, xnzz, allBmParentIds);
            updateBmIdsSet.addAll(updateBmIds);

            // 获取调整部门的数据的原bmIds
            long start2 = System.currentTimeMillis();
            userAllTagMap = Maps.newHashMap();
            Set<Long> updateBmIdsForOld = updateBmIdUser(xnzz.getXnzzId(), userList, userAllTagMap);
            if (NullUtil.isNotNull(updateBmIdsForOld)) {
                updateBmIdsSet.addAll(updateBmIdsForOld);
            }
            long time2 = (System.currentTimeMillis() - start2) / 1000;
            log.info("执行处理用户增量实时标签表，查询部门有修改的数据：xnzzId = {}，times = {}", xnzz.getXnzzId(), time2);

            // 更新到ES
            count += updateDataToEsForUpdate(xnzz.getXnzzId(), userList, gxrq, userAllTagMap);

            if (userList.size() < EsCode.ES_PAGE_NUM) {
                break;
            }

            startId = userList.get(userList.size() - 1).getYhNsrId();
        }
        return count;
    }

    /**
     * 设置字段值
     * @param userList
     * @param xnzz
     * @param allBmParentIds
     * @return 返回需要进行汇总的部门
     */
    private Set<Long> setField(List<UserOds> userList, BiDmSwjg xnzz, Map<Long, List<Long>> allBmParentIds) {
        Set<Long> updateBmIds = Sets.newHashSet();
        for (UserOds userOds : userList) {
            userOds.setSsswjgDm(xnzz.getSwjgDm());
            userOds.setBmIds(allBmParentIds.get(userOds.getBmId()));

            // 添加需要进行汇总的部门
            if (NullUtil.isNotNull(userOds.getBmIds())) {
                updateBmIds.addAll(userOds.getBmIds());
            }
        }
        return updateBmIds;
    }


    /**
     * 获取用户修改bmId后需要重新汇总的原bmIds
     * @param xnzzId
     * @param userList
     * @param userAllTagMap
     * @return
     */
    private Set<Long> updateBmIdUser(Long xnzzId, List<UserOds> userList, Map<String, UserAllTag> userAllTagMap) {
        Set<Long> updateBmIds = Sets.newHashSet();
        List<List<UserOds>> splitList = ListUtil.split(userList, EsCode.ES_PARAM_NUM);

        List<String> keys = null;
        List<UserOds> addUserList = Lists.newArrayList();
        for (List<UserOds> list : splitList) {
            keys = Lists.newArrayList();
            for (UserOds user : list) {
                keys.add(EsKeyUtil.getUserOdsKey(user));
            }

            List<UserAllTag> userAllTagList = esUserAllTagService.listByKeyIds(keys);
            if (NullUtil.isNull(userAllTagList)) {
                continue;
            }

            Map<String, UserAllTag> userTagMap = userAllTagList.stream().collect(Collectors.toMap(
                    allTag -> EsKeyUtil.getUserAllTagKey(allTag),
                    allTag -> allTag,
                    (o, a) -> a));

            // 判断部门是否有修改
            for (UserOds user : list) {
                String key = EsKeyUtil.getUserOdsKey(user);
                UserAllTag allTag = userTagMap.get(key);
                if (NullUtil.isNull(allTag)) {
                    // 不存在于标签表中，记录新增用户数据
                    addUserList.add(user);
                    continue;
                }
                // 记录标签表已有的数据，用于保留活跃标记
                userAllTagMap.put(EsKeyUtil.getUserAllTagKey(allTag), allTag);

                if (NullUtil.isNotNull(allTag.getBmId()) && !allTag.getBmId().equals(user.getBmId())) {
                    List<Long> bmIds = allTag.getBmIds();
                    if (NullUtil.isNotNull(bmIds)) {
                        updateBmIds.addAll(bmIds);
                    }
                }
            }
        }
        if (addUserList.size() > 0) {
            // 新增用户数据补充企业信息字段
            updateCompanyInfoForUser(xnzzId, addUserList);
        }
        return updateBmIds;
    }

    /**
     * 新增用户数据补充企业信息字段
     *
     * @param xnzzId
     * @param userList
     */
    private void updateCompanyInfoForUser(Long xnzzId, List<UserOds> userList) {
        // 用户按nsrId分组
        Map<Long, List<UserOds>> nsrIdMap = userList.stream().filter(userOds -> NullUtil.isNotNull(userOds.getNsrId()))
                .collect(Collectors.groupingBy(UserOds::getNsrId));

        if (NullUtil.isNull(nsrIdMap)) {
            return;
        }

        // 根据nsrId分批查询企业信息并合并
        List<Long> nsrIdList = new ArrayList<>(nsrIdMap.keySet());
        List<List<Long>> nsrIdSplit = ListUtil.split(nsrIdList, 100);
        List<CompanyOds> companyOdsList = Lists.newArrayList();
        for (List<Long> nsrIds : nsrIdSplit) {
            List<CompanyOds> companyList = companyOdsService.listByIds(xnzzId, nsrIds);
            companyOdsList.addAll(companyList);
        }
        // 根据nsrId，更新该企业下用户的企业信息
        for (CompanyOds companyOds : companyOdsList) {
            List<UserOds> userOdsList = nsrIdMap.get(companyOds.getNsrId());
            for (UserOds user : userOdsList) {
                user.setDjzclxDm(companyOds.getDjzclxDm());
                user.setHyDm(companyOds.getHyDm());
                user.setZzsnslx(companyOds.getZzsnslx());
                user.setXxwlqyBj(companyOds.getXxwlqyBj());
            }
        }
    }
    /**
     * 更新数据到ES(增量更新)
     *
     * @param xnzzId
     * @param userList
     * @param gxrq
     * @param userAllTagMap
     * @return
     */
    private int updateDataToEsForUpdate(Long xnzzId, List<UserOds> userList, String gxrq, Map<String, UserAllTag> userAllTagMap) {
        Map<String, UserAllTag> userTagList = Maps.newHashMap();

        // 查询用户其他字段
        long start1 = System.currentTimeMillis();
        long time1 = (System.currentTimeMillis() - start1) / 1000;
        log.info("执行处理用户增量实时标签表，查询用户补充字段：xnzzId = {}，times = {}", xnzzId, time1);

        for (UserOds userOds : userList) {
            UserAllTag userTag = new UserAllTag();

            // 更新日期
            userTag.setGxrq(gxrq);
            setUserTagData(userTag, userOds);

            if (NullUtil.isNotNull(userAllTagMap)) {
                // 如果该数据已存在于标签表，将活跃标记和企业信息复制过来
                UserAllTag oldUserAllTag = userAllTagMap.get(EsKeyUtil.getUserAllTagKey(userTag));
                if (NullUtil.isNotNull(oldUserAllTag)) {
                    userTag.setHyTag(oldUserAllTag.getHyTag());
                    userTag.setWeekHyTag(oldUserAllTag.getWeekHyTag());
                    userTag.setMonthHyTag(oldUserAllTag.getMonthHyTag());
                    userTag.setHyrq(oldUserAllTag.getHyrq());
                    userTag.setDjzclxDm(oldUserAllTag.getDjzclxDm());
                    userTag.setHyDm(oldUserAllTag.getHyDm());
                    userTag.setZzsnslx(oldUserAllTag.getZzsnslx());
                    userTag.setXxwlqyBj(oldUserAllTag.getXxwlqyBj());
                }
            }
            userTagList.put(EsKeyUtil.getUserAllTagKey(userTag), userTag);
        }

        if (userTagList.size() > 0) {
            long start2 = System.currentTimeMillis();
            esService.multiInsertOrUpdate(EsIndexConstant.USER_ALL_TAG, userTagList);
            long time2 = (System.currentTimeMillis() - start2) / 1000;
            log.info("执行处理用户增量实时标签表，更新数据到ES：xnzzId = {}，times = {}", xnzzId, time2);
            return userTagList.size();
        } else {
            return 0;
        }
    }

    /**
     * 设置用户标签表字段值
     *
     * @param userTag
     * @param userOds
     */
    private void setUserTagData(UserAllTag userTag, UserOds userOds) {
        BeanUtils.copyProperties(userOds, userTag);
        userTag.setCysxTag(userOds.getCysx());
        userTag.setRylxTag(userOds.getRylxDm());
        userTag.setRzjbTag(userOds.getRzjb());

        // 设置中间表yhId及成员属性
        if (EsCode.EsYhType.TPC.equals(userOds.getYhType())) {
            if (NullUtil.isNull(userOds.getNsrId())) {
                // 机构人员
                userTag.setYhId(EsKeyUtil.getTpcYhId(userOds.getYhNsrId(), DsbCode.Cysx.CYSX_SWJRY));
                userTag.setCysxTag(DsbCode.Cysx.CYSX_SWJRY);
            } else {
                // 企业人员
                userTag.setYhId(EsKeyUtil.getTpcYhId(userOds.getYhNsrId(), DsbCode.Cysx.CYSX_QYRY));
                userTag.setCysxTag(DsbCode.Cysx.CYSX_QYRY);
            }
        }

        // 成员状态
        userTag.setCyztTag(userOds.getYhCyzt());

        // 激活标记
        if (DsbCode.Jhbj.YJH.equals(userOds.getJhbj()) || DsbCode.Cyzt.ACTIVE.equals(userOds.getYhCyzt())){
            userTag.setJhTag(EsCode.YES);
        } else {
            userTag.setJhTag(EsCode.NO);
        }
        // 认证标记
        if (NullUtil.isNotNull(userOds.getRzsj())){
            userTag.setRzTag(EsCode.YES);
        } else {
            userTag.setRzTag(EsCode.NO);
        }
        // 同步标记
        userTag.setTbTag(userOds.getTbzt());
        // 在册标记
        if (userOds.getYhIsDelete() || userOds.getIsDelete()) {
            userTag.setZcTag(EsCode.NO);
        } else {
            userTag.setZcTag(EsCode.YES);
        }
    }
}
