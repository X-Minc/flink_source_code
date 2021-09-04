package com.ifugle.rap.bigdata.task.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ifugle.rap.bigdata.task.DepartOds;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:23
 */
public interface YhzxXnzzBmService {
    /**
     * 根据虚拟组织ID和修改时间获取部门数据
     *
     * @param xnzzId
     * @param bmIds
     * @param startDate
     *
     * @return
     */
    List<DepartOds> listBmByUpdate(Long xnzzId, List<Long> bmIds, Date startDate);

    /**
     * 获取所有部门及其上级列表
     *
     * @param xnzzId
     *
     * @return
     */
    Map<Long, List<Long>> getAllBmParantIds(Long xnzzId);
    /**
     * 查询所有父级节点的下级节点列表
     *
     * @param xnzzId
     *
     * @return
     */
    Map<Long, List<Long>> getAllBmChildIds(Long xnzzId);

    /**
     * 根据bmMap中的部门，生成部门路径列表
     *
     * @param bmId
     * @param bmMap
     * @param bmParentMap
     */
    void getBmParentId(Long bmId, Map<Long, Long> bmMap, Map<Long, List<Long>> bmParentMap);

    /**
     * 查询数据库当前日期
     *
     * @return
     */
    Date getDbCurrentDate();

    /**
     * 查询部门表中所有的parent_id
     *
     * @param xnzzId
     * @param bmIds
     *
     * @return
     */
    List<Long> listAllBmParentIds(Long xnzzId, List<Long> bmIds);
}
