package com.ifugle.rap.bigdata.task.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.DepartAggDw;
import com.ifugle.rap.bigdata.task.DepartOds;
import com.ifugle.rap.bigdata.task.es.AggsResponseEntity;
import com.ifugle.rap.bigdata.task.es.DslRequestBuild;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;
import com.ifugle.rap.bigdata.task.service.EsDepartAggDwService;
import com.ifugle.rap.bigdata.task.service.EsDepartOdsService;
import com.ifugle.rap.bigdata.task.service.EsService;
import com.ifugle.rap.bigdata.task.util.EsAggsUtil;
import com.ifugle.rap.bigdata.task.util.EsKeyUtil;
import com.ifugle.rap.bigdata.task.util.EsValueUtil;
import com.ifugle.rap.constants.EsCode;
import com.ifugle.rap.constants.EsIndexConstant;
import com.ifugle.rap.utils.ListUtil;
import com.ifugle.util.NullUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author XuWeigang
 * @since 2019年08月05日 10:27
 */
@Service
@Slf4j
public class EsDepartAggDwServiceImpl implements EsDepartAggDwService {
    @Autowired
    private EsService esService;
    @Autowired
    private EsDepartOdsService esDepartOdsService;

    // @Autowired
    // private DepartAggRedisService departAggRedisService;

    /**
     * 按虚拟组织对各部门做全量汇总统计
     * @param xnzzList
     */
    @Override
    public void aggregateStatisticsByXnzz(List<BiDmSwjg> xnzzList) {
        log.info("按部门全量汇总开始");
        for (BiDmSwjg xnzz : xnzzList) {
            Long xnzzId = xnzz.getXnzzId();
            log.info("按部门全量汇总开始：xnzzId = {}", xnzzId);

            // 获取该虚拟组织所有上级部门ID，用于判断是否叶子节点部门
            Set<Long> parentIds = esDepartOdsService.listAllBmParentIds(xnzzId, null);

            // 所有部门的部门路径
            Map<Long, List<Long>> bmIdsMap = esDepartOdsService.getAllDepartBmIds(xnzzId);

            // 获取该虚拟组织的所有部门
            List<DepartOds> departList = esDepartOdsService.listDepart(xnzzId, null);

            Map<String, DepartAggDw> dataMap = new HashMap<>();
            // 对部门数据进行拆分，分批处理
            List<List<DepartOds>> departLists = ListUtil.split(departList, EsCode.BM_BATCH_NUM);
            // 分批进行部门汇总
            for (List<DepartOds> departs : departLists) {
                // 将汇总好的数据添加到dataMap
                aggregateStatistics(departs, parentIds, bmIdsMap, dataMap);
                // 将计算完的数据添加到redis
                // departAggRedisService.addDepartAggDwItems(dataMap);
                // 清除数据
                dataMap.clear();
            }
            log.info("按部门全量汇总结束：xnzzId = {}", xnzzId);
        }
        log.info("按部门全量汇总结束");
    }

    /**
     * 按部门ID批量汇总
     * @param bmIds
     */
    @Override
    public void aggregateStatisticsByBmIds(List<Long> bmIds) {
        if (NullUtil.isNull(bmIds)) {
            return;
        }

        log.info("按部门增量汇总开始：bmIds = {}", bmIds);
        // 获取部门信息
        List<DepartOds> departList = esDepartOdsService.listDepart(null, bmIds);

        // 所有上级部门ID
        Set<Long> parentIds = esDepartOdsService.listAllBmParentIds(null, bmIds);

        // 获取部门路径
        Map<Long, List<Long>> bmIdsMap = esDepartOdsService.getAllDepartBmIds(null, bmIds);

        Map<String, DepartAggDw> dataMap = new HashMap<>();
        // 按部门汇总后的数据添加到dataMap
        aggregateStatistics(departList, parentIds, bmIdsMap, dataMap);
        // // 将计算完的数据添加到redis
        // departAggRedisService.addDepartAggDwItems(dataMap);

        log.info("按部门增量汇总结束：bmIds = {}", bmIds);
    }

    /**
     * 按部门进行汇总
     * @param departList
     * @param parentIds
     * @param bmIdsMap
     * @param dataMap
     */
    private void aggregateStatistics(List<DepartOds> departList, Set<Long> parentIds,
            Map<Long, List<Long>> bmIdsMap, Map<String, DepartAggDw> dataMap) {
        Map<Long, Long> bmXnzzMap = departList.stream().collect(Collectors.toMap(DepartOds::getId, DepartOds::getXnzzId));
        List<Long> bmIds = new ArrayList<>(bmXnzzMap.keySet());

        log.info("按部门汇总：bmIds = {}", bmIds);

        // 部门初始数据
        initBm(departList, parentIds, bmIdsMap, dataMap);

        // 人员总数
        updateRyzs(bmXnzzMap, "ryzs", null, bmIds, null, dataMap);
        // 企业人员总数
        updateRyzs(bmXnzzMap, "qyryzs", Arrays.asList(new Byte[]{2, 5}), bmIds, null, dataMap);
        // 机构人员总数
        updateRyzs(bmXnzzMap, "jgryzs", Arrays.asList(new Byte[]{1, 3, 4}), bmIds, null, dataMap);
        // 自然人员总数
        updateRyzs(bmXnzzMap, "zrryzs", Arrays.asList(new Byte[]{6}), bmIds, null, dataMap);
        // 丁税宝用户人员总数
        updateRyzs(bmXnzzMap, "dsbryzs", null, bmIds, EsCode.EsYhType.USER, dataMap);

        // 人员激活总数
        updateRyjhzs(bmXnzzMap, "ryjhzs", null, bmIds, null, dataMap);
        // 企业人员激活总数
        updateRyjhzs(bmXnzzMap, "qyryjhzs", Arrays.asList(new Byte[]{2, 5}), bmIds, null, dataMap);
        // 机构人员激活总数
        updateRyjhzs(bmXnzzMap, "jgryjhzs", Arrays.asList(new Byte[]{1, 3, 4}), bmIds, null, dataMap);
        // 自然人员激活总数
        updateRyjhzs(bmXnzzMap, "zrryjhzs", Arrays.asList(new Byte[]{6}), bmIds, null, dataMap);
        // 丁税宝用户人员激活总数
        updateRyjhzs(bmXnzzMap, "dsbryjhzs", null, bmIds, EsCode.EsYhType.USER, dataMap);

        // 按人员分类查总数
        updateRylxzs(bmXnzzMap, bmIds, dataMap);
        // 按人员分类查激活总数
        updateRylxjhzs(bmXnzzMap, bmIds, dataMap);

        // 企业总数
        updateQyzs(bmXnzzMap, bmIds, dataMap, null);
        updateQyzs(bmXnzzMap, bmIds, dataMap, "qy");
        updateQyzs(bmXnzzMap, bmIds, dataMap, "gt");
        // 激活企业总数
        updateQyjhzs(bmXnzzMap, bmIds, dataMap, null);
        updateQyjhzs(bmXnzzMap, bmIds, dataMap, "qy");
        updateQyjhzs(bmXnzzMap, bmIds, dataMap, "gt");
        // 企业注销数
        updateQyzxs(bmXnzzMap, bmIds, dataMap);
        // 企业税务未办理数
        updateQywbls(bmXnzzMap, bmIds, dataMap);

        // 活跃数
        // updateHyzs(bmXnzzMap, bmIds, dataMap);
    }

    /**
     * 更新日活跃数和月活跃数
     * @param bmXnzzMap
     * @param bmIds
     * @param dataMap
     */
    private void updateHyzs(Map<Long, Long> bmXnzzMap, List<Long> bmIds, Map<String, DepartAggDw> dataMap) {
        // 企业日活跃成员数
        updateHyzs(bmXnzzMap, "qyryhyzs", Arrays.asList(new Byte[]{2, 5}), bmIds, dataMap, "hy_tag");
        // 机构日活跃成员数
        updateHyzs(bmXnzzMap, "jgryhyzs", Arrays.asList(new Byte[]{1, 3, 4}), bmIds, dataMap, "hy_tag");
        // 企业月活跃成员数
        updateHyzs(bmXnzzMap, "qyryhyzs_month", Arrays.asList(new Byte[]{2, 5}), bmIds, dataMap, "hymm_tag");
        // 机构月活跃成员数
        updateHyzs(bmXnzzMap, "jgryhyzs_month", Arrays.asList(new Byte[]{1, 3, 4}), bmIds, dataMap, "hymm_tag");
        // 企业周活跃成员数
        updateHyzs(bmXnzzMap, "qyryhyzs_week", Arrays.asList(new Byte[]{2, 5}), bmIds, dataMap, "hyweek_tag");
        // 机构周活跃成员数
        updateHyzs(bmXnzzMap, "jgryhyzs_week", Arrays.asList(new Byte[]{1, 3, 4}), bmIds, dataMap, "hyweek_tag");
    }

    /**
     * 部门基础数据
     * @param departList
     * @param parentBmIds
     * @return 返回需要做精确汇总的部门ID
     */
    private void initBm(List<DepartOds> departList, Set<Long> parentBmIds, Map<Long, List<Long>> bmIdsMap, Map<String, DepartAggDw> dataMap) {
        for (DepartOds depart : departList) {
            DepartAggDw aggDw = new DepartAggDw();
            BeanUtils.copyProperties(depart, aggDw);
            aggDw.setXnzzId(depart.getXnzzId());
            aggDw.setBmId(depart.getId());
            aggDw.setBmmc(depart.getBmmc());
            aggDw.setBmsx(depart.getBmsx());
            aggDw.setXssx(depart.getXssx());
            aggDw.setPBmId(depart.getParentId());
            aggDw.setIsDelete(depart.getIsDelete());
            aggDw.setBmIds(bmIdsMap.get(depart.getId()));
            if (parentBmIds.contains(aggDw.getBmId())) {
                // 包含子节点的部门不是叶子节点
                aggDw.setIsLeafBm(false);
            } else {
                // 不包含子节点的部门是叶子节点
                aggDw.setIsLeafBm(true);
            }
            // 汇总数量初始为0
            aggDw.initialize();
            dataMap.put(EsKeyUtil.getDepartAggDwKey(aggDw), aggDw);
        }
    }

    /**
     * 人员总数
     * @param bmXnzzMap
     * @param field
     * @param cysx
     * @param bmIds
     */
    private void updateRyzs(Map<Long, Long> bmXnzzMap, String field, List<Byte> cysx, List<Long> bmIds, Byte yhType, Map<String, DepartAggDw> dataMap) {
        // 查询条件
        Map<String, Object> params = Maps.newHashMap();
        params.put("bm_ids", bmIds);
        params.put("zc_tag", EsCode.YES);
        if (NullUtil.isNotNull(cysx)) {
            params.put("cysx_tag", cysx);
        }
        if (NullUtil.isNotNull(yhType)) {
            params.put("yh_type", yhType);
        }
        DslRequestBuild dslRequest = new DslRequestBuild(0, 0, params);
        // 汇总字段
        dslRequest.addAggsParam("bm_id", "terms", new String[]{"field", "size", "include"}, new Object[]{"bm_ids", bmIds.size(), bmIds});
        dslRequest.addAggsParam("ryzs", "cardinality", new String[]{"field", "precision_threshold"}, new Object[]{"yh_id", EsCode.PRECISION_THRESHOLD});
        AggsResponseEntity responseEntity = esService.aggregateQuery(EsIndexConstant.USER_ALL_TAG, dslRequest);
        List<Map<String, Object>> resultList = EsAggsUtil.getAggregationsResult(responseEntity, dslRequest.getAggsParams());

        for (Map item : resultList) {
            DepartAggDw depart = new DepartAggDw();
            depart.setBmId(EsValueUtil.getLongValue(item, "bm_id"));
            depart.setXnzzId(bmXnzzMap.get(depart.getBmId()));
            depart = dataMap.get(EsKeyUtil.getDepartAggDwKey(depart));
            if (NullUtil.isNull(depart)) {
                continue;
            }

            Integer ryzs = EsValueUtil.getIntValue(item, "ryzs");
            switch (field) {
                case "ryzs":
                    // 人员总数
                    depart.setRyzs(ryzs);
                    break;
                case "qyryzs":
                    // 企业人员总数
                    depart.setQyryzs(ryzs);
                    break;
                case "jgryzs":
                    // 机构人员总数
                    depart.setJgryzs(ryzs);
                    break;
                case "zrryzs":
                    // 自然人总数
                    depart.setZrryzs(ryzs);
                    break;
                case "dsbryzs":
                    depart.setDsbryzs(ryzs);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 人员激活总数
     * @param bmXnzzMap
     * @param field
     * @param cysx
     * @param bmIds
     */
    private void updateRyjhzs(Map<Long, Long> bmXnzzMap, String field, List<Byte> cysx, List<Long> bmIds, Byte yhType, Map<String, DepartAggDw> dataMap) {
        // 查询条件
        Map<String, Object> params = Maps.newHashMap();
        params.put("bm_ids", bmIds);
        params.put("zc_tag", EsCode.YES);
        params.put("jh_tag", EsCode.YES);
        if (NullUtil.isNotNull(cysx)) {
            params.put("cysx_tag", cysx);
        }
        if (NullUtil.isNotNull(yhType)) {
            params.put("yh_type", yhType);
        }
        DslRequestBuild dslRequest = new DslRequestBuild(0, 0, params);
        // 汇总字段
        dslRequest.addAggsParam("bm_id", "terms", new String[]{"field", "size", "include"}, new Object[]{"bm_ids", bmIds.size(), bmIds});
        dslRequest.addAggsParam("ryjhzs", "cardinality", new String[]{"field", "precision_threshold"}, new Object[]{"yh_id", EsCode.PRECISION_THRESHOLD});
        AggsResponseEntity responseEntity = esService.aggregateQuery(EsIndexConstant.USER_ALL_TAG, dslRequest);
        List<Map<String, Object>> resultList = EsAggsUtil.getAggregationsResult(responseEntity, dslRequest.getAggsParams());

        for (Map item : resultList) {
            DepartAggDw depart = new DepartAggDw();
            depart.setBmId(EsValueUtil.getLongValue(item, "bm_id"));
            depart.setXnzzId(bmXnzzMap.get(depart.getBmId()));
            depart = dataMap.get(EsKeyUtil.getDepartAggDwKey(depart));
            if (NullUtil.isNull(depart)) {
                continue;
            }

            Integer ryjhzs = EsValueUtil.getIntValue(item, "ryjhzs");
            switch (field) {
                case "ryjhzs":
                    // 激活人员总数
                    depart.setRyjhzs(ryjhzs);
                    break;
                case "qyryjhzs":
                    // 企业人员激活总数
                    depart.setQyryjhzs(ryjhzs);
                    break;
                case "jgryjhzs":
                    // 机构人员激活总数
                    depart.setJgryjhzs(ryjhzs);
                    break;
                case "zrryjhzs":
                    // 自然人激活总数
                    depart.setZrryjhzs(ryjhzs);
                    break;
                case "dsbryjhzs":
                    depart.setDsbryjhzs(ryjhzs);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 按人员分类查总数
     * @param bmXnzzMap
     * @param bmIds
     * @param dataMap
     */
    private void updateRylxzs(Map<Long, Long> bmXnzzMap, List<Long> bmIds, Map<String, DepartAggDw> dataMap) {
        Byte[] rylxs = new Byte[]{EsCode.EsRylx.FDDBR, EsCode.EsRylx.CWFZR, EsCode.EsRylx.BSR, EsCode.EsRylx.QTBSR, EsCode.EsRylx.GPY};
        for (Byte rylx : rylxs) {
            // 查询条件
            Map<String, Object> params = Maps.newHashMap();
            params.put("bm_ids", bmIds);
            // 只取企业人员
            params.put("cysx_tag", Arrays.asList(new Byte[]{2, 5}));
            params.put("zc_tag", EsCode.YES);
            params.put("rylx_tag", rylx);
            DslRequestBuild dslRequest = new DslRequestBuild(0, 0, params);
            // 汇总字段
            dslRequest.addAggsParam("bm_id", "terms", new String[]{"field", "size", "include"}, new Object[]{"bm_ids", bmIds.size(), bmIds});
            dslRequest.addAggsParam("ryzs", "cardinality", new String[]{"field", "precision_threshold"}, new Object[]{"yh_id", EsCode.PRECISION_THRESHOLD});
            AggsResponseEntity responseEntity = esService.aggregateQuery(EsIndexConstant.USER_ALL_TAG, dslRequest);
            List<Map<String, Object>> resultList = EsAggsUtil.getAggregationsResult(responseEntity, dslRequest.getAggsParams());

            for (Map item : resultList) {
                DepartAggDw depart = new DepartAggDw();
                depart.setBmId(EsValueUtil.getLongValue(item, "bm_id"));
                depart.setXnzzId(bmXnzzMap.get(depart.getBmId()));
                depart = dataMap.get(EsKeyUtil.getDepartAggDwKey(depart));
                if (NullUtil.isNull(depart)) {
                    continue;
                }

                Integer ryzs = EsValueUtil.getIntValue(item, "ryzs");
                if (EsCode.EsRylx.FDDBR.equals(rylx)) {
                    // 法定代表人
                    depart.setFddbrzs(ryzs);
                } else if (EsCode.EsRylx.CWFZR.equals(rylx)) {
                    // 财务负责人
                    depart.setCwfzrzs(ryzs);
                } else if (EsCode.EsRylx.BSR.equals(rylx)) {
                    // 办税人
                    depart.setBsrzs(ryzs);
                } else if (EsCode.EsRylx.QTBSR.equals(rylx)) {
                    // 其他办税人
                    depart.setQtbsrzs(ryzs);
                } else if (EsCode.EsRylx.GPY.equals(rylx)) {
                    // 购票员
                    depart.setGpyzs(ryzs);
                }
            }
        }
    }

    /**
     * 按人员分类查激活总数
     * @param bmXnzzMap
     * @param bmIds
     * @param dataMap
     */
    private void updateRylxjhzs(Map<Long, Long> bmXnzzMap, List<Long> bmIds, Map<String, DepartAggDw> dataMap) {
        Byte[] rylxs = new Byte[]{EsCode.EsRylx.FDDBR, EsCode.EsRylx.CWFZR, EsCode.EsRylx.BSR, EsCode.EsRylx.QTBSR, EsCode.EsRylx.GPY};
        for (Byte rylx : rylxs) {
            // 查询条件
            Map<String, Object> params = Maps.newHashMap();
            params.put("bm_ids", bmIds);
            params.put("zc_tag", EsCode.YES);
            params.put("jh_tag", EsCode.YES);
            // 只取企业人员
            params.put("cysx_tag", Arrays.asList(new Byte[]{2, 5}));
            params.put("rylx_tag", rylx);
            DslRequestBuild dslRequest = new DslRequestBuild(0, 0, params);
            // 汇总字段
            dslRequest.addAggsParam("bm_id", "terms", new String[]{"field", "size", "include"}, new Object[]{"bm_ids", bmIds.size(), bmIds});
            dslRequest.addAggsParam("ryjhzs", "cardinality", new String[]{"field", "precision_threshold"}, new Object[]{"yh_id", EsCode.PRECISION_THRESHOLD});
            AggsResponseEntity responseEntity = esService.aggregateQuery(EsIndexConstant.USER_ALL_TAG, dslRequest);
            List<Map<String, Object>> resultList = EsAggsUtil.getAggregationsResult(responseEntity, dslRequest.getAggsParams());

            for (Map item : resultList) {
                DepartAggDw depart = new DepartAggDw();
                depart.setBmId(EsValueUtil.getLongValue(item, "bm_id"));
                depart.setXnzzId(bmXnzzMap.get(depart.getBmId()));
                depart = dataMap.get(EsKeyUtil.getDepartAggDwKey(depart));
                if (NullUtil.isNull(depart)) {
                    continue;
                }

                Integer ryjhzs = EsValueUtil.getIntValue(item, "ryjhzs");
                if (EsCode.EsRylx.FDDBR.equals(rylx)) {
                    // 法定代表人
                    depart.setFddbrjhzs(ryjhzs);
                } else if (EsCode.EsRylx.CWFZR.equals(rylx)) {
                    // 财务负责人
                    depart.setCwfzrjhzs(ryjhzs);
                } else if (EsCode.EsRylx.BSR.equals(rylx)) {
                    // 办税人
                    depart.setBsrjhzs(ryjhzs);
                } else if (EsCode.EsRylx.QTBSR.equals(rylx)) {
                    // 其他办税人
                    depart.setQtbsrjhzs(ryjhzs);
                } else if (EsCode.EsRylx.GPY.equals(rylx)) {
                    // 购票员
                    depart.setGpyjhzs(ryjhzs);
                }
            }
        }
    }

    /**
     * 企业总数
     * @param bmXnzzMap
     * @param bmIds
     * @param dataMap
     * @param type
     */
    private void updateQyzs(Map<Long, Long> bmXnzzMap, List<Long> bmIds, Map<String, DepartAggDw> dataMap, String type) {
        // 查询条件
        Map<String, Object> params = Maps.newHashMap();
        params.put("bm_ids", bmIds);
        params.put("zc_tag", EsCode.YES);
        // 税务未办理户为否
        params.put("swwbl_tag", EsCode.NO);
        DslRequestBuild dslRequest = new DslRequestBuild(0, 0, params);

        if ("qy".equals(type)) {
            // 企业（非个体非未注册）：存在djzclx_tag且不为空、不为0、不以4开头
            dslRequest.setExistsParam("djzclx_tag");
            dslRequest.setNotParam("djzclx_tag", Arrays.asList(new String[]{"", "0"}));
            dslRequest.setNotPrefix("djzclx_tag", "4");
        } else if ("gt".equals(type)) {
            // 个体工商户：djzclx_tag以4开头
            dslRequest.setPrefix("djzclx_tag", "4");
        }

        // 汇总字段
        dslRequest.addAggsParam("bm_id", "terms", new String[]{"field", "size", "include"}, new Object[]{"bm_ids", bmIds.size(), bmIds});
        dslRequest.addAggsParam("qys", "value_count", new String[]{"field"}, new Object[]{"nsr_id"});
        AggsResponseEntity responseEntity = esService.aggregateQuery(EsIndexConstant.COMPANY_ALL_TAG, dslRequest);
        List<Map<String, Object>> resultList = EsAggsUtil.getAggregationsResult(responseEntity, dslRequest.getAggsParams());

        for (Map item : resultList) {
            DepartAggDw depart = new DepartAggDw();
            depart.setBmId(EsValueUtil.getLongValue(item, "bm_id"));
            depart.setXnzzId(bmXnzzMap.get(depart.getBmId()));
            depart = dataMap.get(EsKeyUtil.getDepartAggDwKey(depart));
            if (NullUtil.isNull(depart)) {
                continue;
            }

            if ("qy".equals(type)) {
                depart.setQyfgts(EsValueUtil.getIntValue(item, "qys"));
            } else if ("gt".equals(type)) {
                depart.setQygts(EsValueUtil.getIntValue(item, "qys"));
            } else {
                depart.setQys(EsValueUtil.getIntValue(item, "qys"));
            }
        }
    }

    /**
     * 激活企业总数
     * @param bmXnzzMap
     * @param bmIds
     * @param dataMap
     * @param type
     */
    private void updateQyjhzs(Map<Long, Long> bmXnzzMap, List<Long> bmIds, Map<String, DepartAggDw> dataMap, String type) {
        // 查询条件
        Map<String, Object> params = Maps.newHashMap();
        params.put("bm_ids", bmIds);
        params.put("zc_tag", EsCode.YES);
        params.put("jh_tag", EsCode.YES);
        // 税务未办理户为否
        params.put("swwbl_tag", EsCode.NO);
        DslRequestBuild dslRequest = new DslRequestBuild(0, 0, params);

        if ("qy".equals(type)) {
            // 企业（非个体非未注册）：存在djzclx_tag且不为空、不为0、不以4开头
            dslRequest.setExistsParam("djzclx_tag");
            dslRequest.setNotParam("djzclx_tag", Arrays.asList(new String[]{"", "0"}));
            dslRequest.setNotPrefix("djzclx_tag", "4");
        } else if ("gt".equals(type)) {
            // 个体工商户：djzclx_tag以4开头
            dslRequest.setPrefix("djzclx_tag", "4");
        }

        // 汇总字段
        dslRequest.addAggsParam("bm_id", "terms", new String[]{"field", "size", "include"}, new Object[]{"bm_ids", bmIds.size(), bmIds});
        dslRequest.addAggsParam("qyjhs", "value_count", new String[]{"field"}, new Object[]{"nsr_id"});
        AggsResponseEntity responseEntity = esService.aggregateQuery(EsIndexConstant.COMPANY_ALL_TAG, dslRequest);
        List<Map<String, Object>> resultList = EsAggsUtil.getAggregationsResult(responseEntity, dslRequest.getAggsParams());

        for (Map item : resultList) {
            DepartAggDw depart = new DepartAggDw();
            depart.setBmId(EsValueUtil.getLongValue(item, "bm_id"));
            depart.setXnzzId(bmXnzzMap.get(depart.getBmId()));
            depart = dataMap.get(EsKeyUtil.getDepartAggDwKey(depart));
            if (NullUtil.isNull(depart)) {
                continue;
            }

            if ("qy".equals(type)) {
                depart.setQyfgtjhs(EsValueUtil.getIntValue(item, "qyjhs"));
            } else if ("gt".equals(type)) {
                depart.setQygtjhs(EsValueUtil.getIntValue(item, "qyjhs"));
            } else {
                depart.setQyjhs(EsValueUtil.getIntValue(item, "qyjhs"));
            }
        }
    }

    /**
     * 企业注销数
     *
     * @param bmXnzzMap
     * @param bmIds
     * @param dataMap
     */
    private void updateQyzxs(Map<Long, Long> bmXnzzMap, List<Long> bmIds, Map<String, DepartAggDw> dataMap) {
        // 查询条件
        Map<String, Object> params = Maps.newHashMap();
        params.put("bm_ids", bmIds);
        params.put("is_delete", "false");
        params.put("nsrzt_dm", Arrays.asList(new Byte[]{7, 8, 10, 13}));
        DslRequestBuild dslRequest = new DslRequestBuild(0, 0, params);
        // 汇总字段
        dslRequest.addAggsParam("bm_id", "terms", new String[]{"field", "size", "include"}, new Object[]{"bm_ids", bmIds.size(), bmIds});
        dslRequest.addAggsParam("qyzxs", "value_count", new String[]{"field"}, new Object[]{"nsr_id"});
        AggsResponseEntity responseEntity = esService.aggregateQuery(EsIndexConstant.COMPANY_ALL_TAG, dslRequest);
        List<Map<String, Object>> resultList = EsAggsUtil.getAggregationsResult(responseEntity, dslRequest.getAggsParams());

        for (Map item : resultList) {
            DepartAggDw depart = new DepartAggDw();
            depart.setBmId(EsValueUtil.getLongValue(item, "bm_id"));
            depart.setXnzzId(bmXnzzMap.get(depart.getBmId()));
            depart = dataMap.get(EsKeyUtil.getDepartAggDwKey(depart));
            if (NullUtil.isNull(depart)) {
                continue;
            }

            depart.setQyzxs(EsValueUtil.getIntValue(item, "qyzxs"));
        }
    }

    /**
     * 企业税务未办理数
     *
     * @param bmXnzzMap
     * @param bmIds
     * @param dataMap
     */
    private void updateQywbls(Map<Long, Long> bmXnzzMap, List<Long> bmIds, Map<String, DepartAggDw> dataMap) {
        // 查询条件
        Map<String, Object> params = Maps.newHashMap();
        params.put("bm_ids", bmIds);
        params.put("zc_tag", EsCode.YES);
        params.put("swwbl_tag", EsCode.YES);
        DslRequestBuild dslRequest = new DslRequestBuild(0, 0, params);
        // 汇总字段
        dslRequest.addAggsParam("bm_id", "terms", new String[]{"field", "size", "include"}, new Object[]{"bm_ids", bmIds.size(), bmIds});
        dslRequest.addAggsParam("qywbls", "value_count", new String[]{"field"}, new Object[]{"nsr_id"});
        AggsResponseEntity responseEntity = esService.aggregateQuery(EsIndexConstant.COMPANY_ALL_TAG, dslRequest);
        List<Map<String, Object>> resultList = EsAggsUtil.getAggregationsResult(responseEntity, dslRequest.getAggsParams());

        for (Map item : resultList) {
            DepartAggDw depart = new DepartAggDw();
            depart.setBmId(EsValueUtil.getLongValue(item, "bm_id"));
            depart.setXnzzId(bmXnzzMap.get(depart.getBmId()));
            depart = dataMap.get(EsKeyUtil.getDepartAggDwKey(depart));
            if (NullUtil.isNull(depart)) {
                continue;
            }

            depart.setQywbls(EsValueUtil.getIntValue(item, "qywbls"));
        }
    }

    /**
     * 活跃人数统计
     * @param bmXnzzMap
     * @param field
     * @param cysx
     * @param bmIds
     * @param dataMap
     * @param hyField
     */
    private void updateHyzs(Map<Long, Long> bmXnzzMap, String field, List<Byte> cysx, List<Long> bmIds, Map<String, DepartAggDw> dataMap, String hyField) {
        // 查询条件
        Map<String, Object> params = Maps.newHashMap();
        params.put("bm_ids", bmIds);
        params.put("cysx_tag", cysx);
        params.put("zc_tag", EsCode.YES);
        params.put(hyField, EsCode.YES);
        DslRequestBuild dslRequest = new DslRequestBuild(0, 0, params);
        // 汇总字段
        dslRequest.addAggsParam("bm_id", "terms", new String[]{"field", "size", "include"}, new Object[]{"bm_ids", bmIds.size(), bmIds});
        dslRequest.addAggsParam("hyzs", "cardinality", new String[]{"field", "precision_threshold"}, new Object[]{"yh_id", EsCode.PRECISION_THRESHOLD});
        AggsResponseEntity responseEntity = esService.aggregateQuery(EsIndexConstant.USER_ALL_TAG, dslRequest);
        List<Map<String, Object>> resultList = EsAggsUtil.getAggregationsResult(responseEntity, dslRequest.getAggsParams());

        for (Map item : resultList) {
            DepartAggDw depart = new DepartAggDw();
            depart.setBmId(EsValueUtil.getLongValue(item, "bm_id"));
            depart.setXnzzId(bmXnzzMap.get(depart.getBmId()));
            depart = dataMap.get(EsKeyUtil.getDepartAggDwKey(depart));
            if (NullUtil.isNull(depart)) {
                continue;
            }

            Integer hyzs = EsValueUtil.getIntValue(item, "hyzs");
            switch (field) {
                case "qyryhyzs":
                    // 企业人员日活跃总数
                    depart.setQyryhyzs(hyzs);
                    break;
                case "jgryhyzs":
                    // 机构人员日活跃总数
                    depart.setJgryhyzs(hyzs);
                    break;
                case "qyryhyzs_month":
                    // 企业人员月活跃总数
                    depart.setQyryhyzsMonth(hyzs);
                    break;
                case "jgryhyzs_month":
                    // 机构人员月活跃总数
                    depart.setJgryhyzsMonth(hyzs);
                    break;
                case "qyryhyzs_week":
                    // 企业人员周活跃总数
                    depart.setQyryhyzsWeek(hyzs);
                    break;
                case "jgryhyzs_week":
                    // 机构人员周活跃总数
                    depart.setJgryhyzsWeek(hyzs);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public SearchResponseEntity<DepartAggDw> scrollQueryByPage(Map<String, Object> querySearch, int scrollTime, int page, int size) {
        SearchResponseEntity<DepartAggDw> entity = esService.scrollQueryByPage(EsIndexConstant.DEPART_AGG_DW,
                querySearch, new TypeToken<SearchResponseEntity<DepartAggDw>>() {
                }.getType(), scrollTime, page, size);
        return entity;
    }

    @Override
    public HitsResponseEntity<DepartAggDw> scrollQueryByPage(String scrollId, int scrollTime, int size) {
        SearchResponseEntity<DepartAggDw> entity = esService.scrollQueryByPage(scrollId, scrollTime,
                new TypeToken<SearchResponseEntity<DepartAggDw>>() {
                }.getType());
        HitsResponseEntity<DepartAggDw> hits = entity.getHits();
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
