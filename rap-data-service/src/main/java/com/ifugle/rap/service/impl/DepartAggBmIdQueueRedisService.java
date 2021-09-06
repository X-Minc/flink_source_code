package com.ifugle.rap.service.impl;

import com.ifugle.rap.boot.redis.SKOVRedisTemplate;
import com.ifugle.util.NullUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author XuWeigang
 * @since 2019年09月10日 16:35
 */
@Service
public class DepartAggBmIdQueueRedisService {
    private static final String DEPART_AGG_BM_ID_QUEUE = "esDepartAggBmIdQueue";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SKOVRedisTemplate redisTemplate;

    /**
     * 将需要汇总的部门Id加入队列
     *
     * @param bmIds
     */
    public synchronized void addBmIdToQueue(List<Long> bmIds) {
        if (NullUtil.isNull(bmIds)) {
            return;
        }

        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        for (Long bmId : bmIds) {
            tuples.add(new DefaultTypedTuple<>(String.valueOf(bmId), (double)System.currentTimeMillis()));
        }

        ZSetOperations<String, String> stringStringZSetOperations = stringRedisTemplate.opsForZSet();
        stringStringZSetOperations.add(DEPART_AGG_BM_ID_QUEUE, tuples);
    }

    /**
     * 取出一批bmId进行汇总计算
     *
     * @param count
     *
     * @return
     */
    public synchronized List<Long> getBmIdsFromQueue(int count) {
        ZSetOperations<String, String> stringStringZSetOperations = stringRedisTemplate.opsForZSet();
        Set<String> bmIdsList = stringStringZSetOperations.rangeByScore(DEPART_AGG_BM_ID_QUEUE, 0, System.currentTimeMillis(), 0, count);
        if (bmIdsList == null) {
            bmIdsList = new HashSet<>();
        }
        // 取出后从队列删除
        if (NullUtil.isNotNull(bmIdsList)) {
            ListOperations<String, Object> opsForList = redisTemplate.opsForList();
            // 添加到线程正在处理数据的临时缓存中
            opsForList.rightPushAll(Thread.currentThread().getName(), bmIdsList.toArray());

            String[] bmIds = new String[bmIdsList.size()];
            bmIdsList.toArray(bmIds);
            stringStringZSetOperations.remove(DEPART_AGG_BM_ID_QUEUE, bmIds);
        } return bmIdsList.stream().map(Long::valueOf).collect(Collectors.toList());
    }
}
