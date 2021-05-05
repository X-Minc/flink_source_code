/**
 * Copyright(C) 2019 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 * 
 */
package com.ifugle.rap.mapper.bigdata;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.ifugle.rap.bigdata.task.DepartOds;
import com.ifugle.rap.bigdata.task.YhzxXnzzBm;
import com.ifugle.rap.bigdata.task.YhzxXnzzBmChild;
import com.ifugle.rap.core.mapper.BaseMapper;

/**
 * @since 2019-07-23 11:40:11
 * @version $Id$
 * @author xuweigang
 */
public interface YhzxXnzzBmMapper extends BaseMapper<YhzxXnzzBm, Long> {

    List<YhzxXnzzBm> queryAllBmIds(@Param("xnzzId") Long xnzzId);

    /**
     * 根据修改时间获取部门数据
     *
     * @param xnzzId
     * @param bmIds
     * @param startDate
     * @return
     */
    List<DepartOds> listBmByUpdate(@Param("xnzzId") Long xnzzId, @Param("bmIds") List<Long> bmIds,
            @Param("startDate") Date startDate);

    /**
     * 查询部门表中所有的parent_id
     * @param xnzzId
     * @param bmIds
     * @return
     */
    List<Long> listAllBmParentIds(@Param("xnzzId") Long xnzzId, @Param("bmIds") List<Long> bmIds);


    /**
     * 查询部门表中指定部门集合的数据，按照部门全路径排序
     * @param xnzzId
     * @param bmIds
     * @return
     */
    List<DepartOds> listAggsBmByIds(@Param("xnzzId") Long xnzzId, @Param("bmIds") List<Long> bmIds);


    /**
     * 查询部门表二级部门的数据，按照部门全路径排序
     * @param xnzzId
     * @return
     */
    List<DepartOds> listAggsTwoClass(@Param("xnzzId") Long xnzzId);

    /**
     * 查询数据库当前日期
     * @return
     */
    Date getDbCurrentDate();

    List<YhzxXnzzBmChild> listByParentId(@Param("parentId") Long parentId, @Param("xnzzId") Long xnzzId,
            @Param("bmmc") String bmmc, @Param("bmsxs") Set<Byte> bmsxs);
}