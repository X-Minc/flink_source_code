package com.ifugle.rap.bigdata.task.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.DepartOds;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:20
 */
public interface EsDepartOdsService {
    /**
     * 按单个虚拟组织更新
     * @param xnzz
     * @param startDate
     * @return
     */
    Set<Long> insertOrUpdateDepartToEsByXnzz(BiDmSwjg xnzz, Date startDate);


    /**
     * 获取部门路径
     *
     * @param xnzzId
     *
     * @return
     */
    Map<Long, List<Long>> getAllDepartBmIds(Long... xnzzId);

    /**
     * 获取指定部门ID的部门路径
     * @param xnzzId
     * @param bmIds
     * @return
     */
    Map<Long, List<Long>> getAllDepartBmIds(Long xnzzId, List<Long> bmIds);


    List<DepartOds> allDepart(List<Long> xnzzIds, List<Long> bmIds);


    SearchResponseEntity<DepartOds> scrollQueryByPage(Map<String, Object> querySearch, int scrollTime, int page, int size);


    HitsResponseEntity<DepartOds> scrollQueryByPage(String scrollId, int scrollTime, int size);


    void deleteScrollQueryByPage(String scrollId);


    /**
     * 查询该虚拟组织在部门汇总表已存在的部门ID
     *
     * @param xnzzId
     *
     * @return
     */
    Set<Long> getDepartAggDwBmIds(Long xnzzId);

    /**
     * 获取parentId列表
     * @param xnzzId
     * @param bmIds
     * @return
     */
    Set<Long> listAllBmParentIds(Long xnzzId, List<Long> bmIds);

    /**
     * 根据部门id查询部门信息
     * @param xnzzId
     * @param bmIds
     * @return
     */
    List<DepartOds> listDepart(Long xnzzId, List<Long> bmIds);


}
