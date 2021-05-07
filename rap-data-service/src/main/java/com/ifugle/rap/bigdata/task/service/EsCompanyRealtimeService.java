package com.ifugle.rap.bigdata.task.service;

import java.util.Date;
import java.util.Set;

import com.ifugle.rap.bigdata.task.BiDmSwjg;

/**
 * @author WenYuan
 * @version $
 * @since 5月 07, 2021 13:48
 */
public interface EsCompanyRealtimeService {


    public Set<Long> updateCompanyRealTimeByAdd(BiDmSwjg xnzz, Date startDate);
}
