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



    public List<DepartOds> allDepart(List<Long> xnzzIds, List<Long> bmIds);


    public SearchResponseEntity<DepartOds> scrollQueryByPage(Map<String, Object> querySearch, int scrollTime, int page, int size);


    public HitsResponseEntity<DepartOds> scrollQueryByPage(String scrollId, int scrollTime, int size);


    public void deleteScrollQueryByPage(String scrollId);
}
