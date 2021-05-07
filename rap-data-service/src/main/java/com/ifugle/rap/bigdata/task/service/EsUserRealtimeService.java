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

    public Set<Long> updateUserRealTimeByAdd(BiDmSwjg xnzz, Date startDate);

}
