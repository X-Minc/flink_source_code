package com.ifugle.rap.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 指标日数据计算后的最新时间周期缓存
 *
 * @author XuWeigang
 * @since 2019年12月03日 15:08
 */
@Component
@RapCache(name = IndexDataDayCycleIdCache.INDEX_DATA_DAY_CYCLEID_CACHE, ttl = 60 * 60 * 24 * 3)
public class IndexDataDayCycleIdCache {
    public static final String INDEX_DATA_DAY_CYCLEID_CACHE = "sjtjIndexDataDayCycleIdCache";

    @CachePut(cacheNames = IndexDataDayCycleIdCache.INDEX_DATA_DAY_CYCLEID_CACHE, key = "'cycleId'")
    public Integer putCacha(Integer cycleId) {
        return cycleId;
    }

    @Cacheable(cacheNames = IndexDataDayCycleIdCache.INDEX_DATA_DAY_CYCLEID_CACHE, key = "'cycleId'")
    public Integer getCacha() {
        return null;
    }

    @CachePut(cacheNames = IndexDataDayCycleIdCache.INDEX_DATA_DAY_CYCLEID_CACHE, key = "'result_'+#cycleId")
    public Boolean putResultCacha(Integer cycleId, Boolean flag) {
        return flag;
    }

    @Cacheable(cacheNames = IndexDataDayCycleIdCache.INDEX_DATA_DAY_CYCLEID_CACHE, key = "'result_'+#cycleId")
    public Boolean getResultCacha(Integer cycleId) {
        return null;
    }
}
