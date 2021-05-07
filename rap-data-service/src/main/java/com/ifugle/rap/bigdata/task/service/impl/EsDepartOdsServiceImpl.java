package com.ifugle.rap.bigdata.task.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.reflect.TypeToken;
import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.DepartOds;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;
import com.ifugle.rap.bigdata.task.service.EsDepartOdsService;
import com.ifugle.rap.bigdata.task.service.EsService;
import com.ifugle.rap.bigdata.task.service.YhzxXnzzBmService;
import com.ifugle.rap.bigdata.task.util.EsKeyUtil;
import com.ifugle.rap.constants.EsCode;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.rap.exception.DsbServiceException;
import com.ifugle.rap.utils.PageUtils;
import com.ifugle.util.DateUtil;
import com.ifugle.util.NullUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:21
 */
@Service
@Slf4j
public class EsDepartOdsServiceImpl implements EsDepartOdsService {


    @Autowired
    private YhzxXnzzBmService yhzxXnzzBmService;

    @Autowired
    private EsService esService;

    @Override
    public Set<Long> insertOrUpdateDepartToEsByXnzz(BiDmSwjg xnzz, Date startDate) {
        log.info("增量更新部门数据开始：xnzzId = {}, startTime = {}", xnzz.getXnzzId(), DateUtil.toISO8601DateTime(startDate));
        long startTimeMillis = System.currentTimeMillis();
        List<DepartOds> departList = yhzxXnzzBmService.listBmByUpdate(xnzz.getXnzzId(), null, startDate);
        if (NullUtil.isNull(departList)) {
            // 跳过没有部门更新的虚拟组织
            return Sets.newHashSet();
        }

        Map<Long, List<Long>> bmParentIds = yhzxXnzzBmService.getAllBmParantIds(xnzz.getXnzzId());
        Map<Long, List<Long>> bmChildIds = yhzxXnzzBmService.getAllBmChildIds(xnzz.getXnzzId());

        // 更新bmIds
        Set<Long> upBmIds = new HashSet<>();
        if (startDate == null) {
            upBmIds = updateBmIdsForAll(bmParentIds, departList);
        } else {
            // 增量部门更新
            upBmIds = updateBmIds(bmParentIds, bmChildIds, departList);
        }

        // 更新部门增量信息到ES
        updateEs(departList);

        // 更新部门汇总表的部门信息   注意：无需更新
        // updateDepartAggDwBm(xnzz.getXnzzId(), departList, bmParentIds);

        long time = (System.currentTimeMillis() - startTimeMillis) / 1000;
        log.info("增量更新部门数据结束：xnzzId = {}，更新数据{}条，需要汇总的部门数量为{}，耗时{}s",
                xnzz.getXnzzId(), departList.size(), upBmIds.size(), time);

        return upBmIds;
    }

    @Override
    public Map<Long, List<Long>> getAllDepartBmIds(Long... xnzzId) {
        List<Long> xnzzIds = null;
        if (NullUtil.isNotNull(xnzzId)) {
            xnzzIds = Lists.newArrayList(xnzzId);
        }
        Map<Long, List<Long>> bmParentMap = getAllBmIds(xnzzIds, null);
        return bmParentMap;
    }

    /**
     * 查询部门表，生成部门路径
     *
     * @param xnzzIds
     *
     * @return
     */
    private Map<Long, List<Long>> getAllBmIds(List<Long> xnzzIds, List<Long> bmIds) {
        List<DepartOds> allDepart = allDepart(xnzzIds, bmIds);
        Map<Long, List<Long>> bmParentMap = new HashMap<>();
        for (DepartOds ods : allDepart) {
            bmParentMap.put(ods.getId(), ods.getBmIds());
        }
        return bmParentMap;
    }

    @Override
    public List<DepartOds> allDepart(List<Long> xnzzIds, List<Long> bmIds) {
        int page = 1;
        int size = EsCode.ES_FIND_PAGE_NUM;

        Map<String, Object> querySearch = Maps.newHashMapWithExpectedSize(2);
        if (NullUtil.isNotNull(xnzzIds)) {
            querySearch.put("xnzz_id", xnzzIds);
        }
        if (NullUtil.isNotNull(bmIds)) {
            querySearch.put("id", bmIds);
        }
        SearchResponseEntity<DepartOds> entity = scrollQueryByPage(querySearch, 3, page, size);
        HitsResponseEntity<DepartOds> hits = entity.getHits();
        String scrollId = entity.getScrollId();
        int total = hits.getTotal().getValue();
        int totalPage = PageUtils.getTotalPage(total, size);
        List<DepartOds> departList = hits.getHits().stream().map(depart -> depart.getSource()).collect(Collectors.toList());

        try {
            while (totalPage > page) {
                page++;
                hits = scrollQueryByPage(scrollId, 3, size);
                List<DepartOds> list = hits.getHits().stream().map(depart -> depart.getSource()).collect(Collectors.toList());
                departList.addAll(list);
            }
        } catch (DsbServiceException e) {
            deleteScrollQueryByPage(scrollId);
            log.error("获取部门列表：error：{}", e.getMessage());
            throw new DsbServiceException(e.getMessage());
        }

        return departList;
    }

    @Override
    public SearchResponseEntity<DepartOds> scrollQueryByPage(Map<String, Object> querySearch, int scrollTime, int page, int size) {
        SearchResponseEntity<DepartOds> entity = esService.scrollQueryByPage(EsIndexConstant.DEPART_ODS,
                querySearch, new TypeToken<SearchResponseEntity<DepartOds>>() {
                }.getType(), scrollTime, page, size);
        return entity;
    }

    @Override
    public HitsResponseEntity<DepartOds> scrollQueryByPage(String scrollId, int scrollTime, int size) {
        SearchResponseEntity<DepartOds> entity = esService.scrollQueryByPage(scrollId, scrollTime,
                new TypeToken<SearchResponseEntity<DepartOds>>() {
                }.getType());
        HitsResponseEntity<DepartOds> hits = entity.getHits();
        if (NullUtil.isNull(hits) || NullUtil.isNull(hits.getHits()) || hits.getHits().size() < size) {
            esService.deleteScrollQueryByPage(scrollId);
        }
        return hits;
    }

    @Override
    public void deleteScrollQueryByPage(String scrollId) {
        esService.deleteScrollQueryByPage(scrollId);
    }

    /**
     * 全量部门新增覆盖，不判断变更
     * @param bmParentIds
     * @param departList
     * @return
     */
    private Set<Long> updateBmIdsForAll(Map<Long, List<Long>> bmParentIds, List<DepartOds> departList) {
        // 从ES查询部门数据，判断parent_id是否有修改
        Map<Long, List<Long>> updateList = Maps.newHashMap();
        Set<Long> updateBmIds = Sets.newHashSet();
        for (DepartOds depart : departList) {
            depart.setBmIds(bmParentIds.get(depart.getId()));
            updateBmIds.add(depart.getId());
        }
        return updateBmIds;
    }


    /**
     * 更新BmIds
     *
     * @param bmParentIds
     * @param bmChildIds
     * @param departList
     */
    private Set<Long> updateBmIds(Map<Long, List<Long>> bmParentIds, Map<Long, List<Long>> bmChildIds,
            List<DepartOds> departList) {
        // 从ES查询部门数据，判断parent_id是否有修改
        Map<Long, List<Long>> updateList = Maps.newHashMap();
        Set<Long> updateBmIds = Sets.newHashSet();
        for (DepartOds depart : departList) {
            depart.setBmIds(bmParentIds.get(depart.getId()));

            // 查询ES上的部门信息
            DepartOds esDepart = (DepartOds) esService.get(EsIndexConstant.DEPART_ODS,
                    EsKeyUtil.getDepartOdsKey(depart), DepartOds.class);
            if (NullUtil.isNotNull(esDepart)) {
                if (!depart.getParentId().equals(esDepart.getParentId())) {
                    // 记录下需要重新汇总的部门
                    updateBmIds.addAll(esDepart.getBmIds());
                    updateBmIds.addAll(depart.getBmIds());

                    // 记录更新后的bmIds
                    updateChildBmIds(depart.getId(), updateList, bmParentIds, bmChildIds);
                }
            } else {
                // 记录下需要重新汇总的部门
                updateBmIds.addAll(depart.getBmIds());

                // 记录更新后的bmIds
                updateChildBmIds(depart.getId(), updateList, bmParentIds, bmChildIds);
            }
        }

        // 如果存在上级部门修改的数据，根据bm_id更新相关ES表的bm_ids字段
        Map<String, Object> queryMap = null;
        Map<String, Object> updateMap = null;
        for (Long bmId : updateList.keySet()) {
            // 查询参数字段
            queryMap = Maps.newHashMap();
            queryMap.put("bm_id", bmId);
            // 更新参数字段
            updateMap = Maps.newHashMap();
            updateMap.put("bm_ids", updateList.get(bmId));

            esService.updateByQuery(EsIndexConstant.USER_ALL_TAG, queryMap, updateMap);
            // esService.updateByQuery(EsIndexConstant.COMPANY_ALL_TAG, queryMap, updateMap);
            // esService.updateByQuery(EsIndexConstant.DEPART_AGG_DW, queryMap, updateMap);
        }

        // 部门表及部门及下级更新bmIds
        for (Long bmId : updateList.keySet()) {
            // 查询参数字段
            queryMap = Maps.newHashMap();
            queryMap.put("id", bmId);
            // 更新参数字段
            updateMap = Maps.newHashMap();
            updateMap.put("bm_ids", updateList.get(bmId));
            esService.updateByQuery(EsIndexConstant.DEPART_ODS, queryMap, updateMap);
        }

        return updateBmIds;
    }


    /**
     * 更新部门增量信息到ES
     *
     * @param departList
     * @param bmIdsMap
     */
    private void updateEs(List<DepartOds> departList, Map<Long, List<Long>> bmIdsMap) {
        // 更新部门增量信息到ES
        Map<String, DepartOds> list = new HashMap<>();
        for (DepartOds depart : departList) {
            depart.setBmIds(bmIdsMap.get(depart.getId()));
            list.put(EsKeyUtil.getDepartOdsKey(depart), depart);
        }
        if (list.size() > 0) {
            esService.multiInsertOrUpdate(EsIndexConstant.DEPART_ODS, list);
        }
    }


    /**
     * 递归所有子节点
     * @param bmId
     * @param updateList
     * @param bmParentIds
     * @param bmChildIds
     */
    private void updateChildBmIds( Long bmId, Map<Long, List<Long>> updateList,Map<Long,
            List<Long>> bmParentIds, Map<Long, List<Long>> bmChildIds) {
        // 新的部门上级列表
        updateList.put(bmId, bmParentIds.get(bmId));
        // 如果该部门有下级，则也要更新下级部门的部门路径
        List<Long> children = bmChildIds.get(bmId);
        if (NullUtil.isNotNull(children)) {
            for (Long child : children) {
                // updateChildBmIds(child, updateList, bmParentIds, bmChildIds);
            }
        }
    }


    /**
     * 更新部门增量信息到ES
     *
     * @param departList
     */
    private void updateEs(List<DepartOds> departList) {
        // 更新部门增量信息到ES
        Map<String, DepartOds> list = new HashMap<>();
        for (DepartOds depart : departList) {
            list.put(EsKeyUtil.getDepartOdsKey(depart), depart);
        }
        if (list.size() > 0) {
            esService.multiInsertOrUpdate(EsIndexConstant.DEPART_ODS, list);
        }
    }
}
