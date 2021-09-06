package com.ifugle.rap.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ifugle.rap.bigdata.task.DepartAggDw;
import com.ifugle.rap.utils.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.ifugle.util.NullUtil;

/**
 * @author XuWeigang
 * @since 2019年09月10日 9:41
 */
@Service
@Lazy
public class DepartAggRedisService {
    private static final String DEPART_AGG_DW_KEY = "esDepartAggDwList";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 取出指定数量的部门汇总数据进行ES更新
     *
     * @param count
     *
     * @return
     */
    public Map<String, DepartAggDw> getDepartAggDwItems(int count) {
        Set<Map.Entry<Object, Object>> entrySet = stringRedisTemplate.opsForHash().entries(DEPART_AGG_DW_KEY).entrySet();
        Iterator<Map.Entry<Object, Object>> iterator = entrySet.iterator();
        // 从redis中取出指定数量的数据
        int index = 0;
        Map<String, DepartAggDw> dataMap = Maps.newHashMap();
        while (iterator.hasNext() && index < count) {
            Map.Entry<Object, Object> entry = iterator.next();
            String keyId = (String)entry.getKey();
            String value = (String)entry.getValue();
            if (NullUtil.isNull(value)) {
                continue;
            }

            DepartAggDw departAggDw = GsonUtil.fromJson(value, DepartAggDw.class);
            dataMap.put(keyId, departAggDw);
            index++;
        }
        return dataMap;
    }

    /**
     * 删除已更新到ES的数据
     *
     * @param deleteKeys
     */
    public void deleteItems(List<String> deleteKeys) {
        // 取出后需要从redis中删除该数据
        if (deleteKeys.size() > 0) {
            stringRedisTemplate.opsForHash().delete(DEPART_AGG_DW_KEY, deleteKeys.toArray(new String[deleteKeys.size()]));
        }
    }

    /**
     * 添加汇总完的数据到队列，用于更新到ES
     *
     * @param map
     */
    public void addDepartAggDwItems(Map<String, DepartAggDw> map) {
        Map<String, String> dataMap = Maps.newHashMap();
        for (String key : map.keySet()) {
            dataMap.put(key, GsonUtil.toJson(map.get(key)));
        }
        stringRedisTemplate.opsForHash().putAll(DEPART_AGG_DW_KEY, dataMap);
    }
}
