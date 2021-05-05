package com.ifugle.rap.bigdata.task.service;

import java.util.Date;
import java.util.Set;

import com.ifugle.rap.bigdata.task.BiDmSwjg;

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
}
