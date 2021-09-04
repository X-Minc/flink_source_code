package com.ifugle.rap.bigdata.task.service;

import java.util.List;
import java.util.Map;
import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.DepartAggDw;
import com.ifugle.rap.bigdata.task.es.HitsResponseEntity;
import com.ifugle.rap.bigdata.task.es.SearchResponseEntity;


/**
 * @author XuWeigang
 * @since 2019年08月05日 10:27
 */
public interface EsDepartAggDwService {
    /**
     * 按部门汇总统计
     * @param xnzzList
     */
    void aggregateStatisticsByXnzz(List<BiDmSwjg> xnzzList);

    /**
     * 按部门汇总统计, 只更新需要汇总的虚拟组织及部门
     * @param bmIds
     */
    void aggregateStatisticsByBmIds(List<Long> bmIds);

    SearchResponseEntity<DepartAggDw> scrollQueryByPage(Map<String, Object> querySearch, int scrollTime, int page,
            int size);

    HitsResponseEntity<DepartAggDw> scrollQueryByPage(String scrollId, int scrollTime, int size);

    void deleteScrollQueryByPage(String scrollId);
}
