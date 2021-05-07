package com.ifugle.rap.bigdata.task.service;

import java.util.Date;
import java.util.Set;

import com.ifugle.rap.bigdata.task.BiDmSwjg;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 12:56
 */
public interface EsUserRealtimeService {

    /**
     * 从增量用户数据更新到全量用户实时标签表
     * @param xnzz
     * @param startDate
     * @return 返回需要汇总的虚拟组织及其部门
     */
    Set<Long> updateUserRealTimeByAdd(BiDmSwjg xnzz, Date startDate);

}
