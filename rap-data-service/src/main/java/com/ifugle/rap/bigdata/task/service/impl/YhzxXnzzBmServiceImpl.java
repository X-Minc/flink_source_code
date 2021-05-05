package com.ifugle.rap.bigdata.task.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.ifugle.rap.bigdata.task.DepartOds;
import com.ifugle.rap.bigdata.task.YhzxXnzzBm;
import com.ifugle.rap.bigdata.task.service.YhzxXnzzBmService;
import com.ifugle.rap.mapper.bigdata.YhzxXnzzBmMapper;
import com.ifugle.util.NullUtil;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:24
 */
@Service
public class YhzxXnzzBmServiceImpl implements YhzxXnzzBmService {
    @Autowired
    private YhzxXnzzBmMapper yhzxXnzzBmMapper;


    /**
     * 根据虚拟组织ID和修改时间获取部门数据
     *
     * @param xnzzId
     * @param bmIds
     * @param startDate
     *
     * @return
     */
    @Override
    public List<DepartOds> listBmByUpdate(Long xnzzId, List<Long> bmIds, Date startDate) {
        return yhzxXnzzBmMapper.listBmByUpdate(xnzzId, bmIds, startDate);
    }

    @Override
    public Map<Long, List<Long>> getAllBmParantIds(Long xnzzId) {
        List<YhzxXnzzBm> list = yhzxXnzzBmMapper.queryAllBmIds(xnzzId);
        Map<Long, Long> bmMap = new HashMap<>();
        for (YhzxXnzzBm bm : list) {
            bmMap.put(bm.getId(), bm.getParentId());
        }
        Map<Long, List<Long>> bmParentMap = new HashMap<>();
        for (Long bmId : bmMap.keySet()) {
            getBmParentId(bmId, bmMap, bmParentMap);
        }
        return bmParentMap;
    }

    @Override
    public Map<Long, List<Long>> getAllBmChildIds(Long xnzzId) {
        List<YhzxXnzzBm> list = yhzxXnzzBmMapper.queryAllBmIds(xnzzId);
        Map<Long, List<Long>> bmChildMap = new HashMap<>();
        List<Long> children = null;
        for (YhzxXnzzBm bm : list) {
            children = bmChildMap.get(bm.getParentId());
            if (children == null) {
                children = Lists.newArrayList();
            }
            children.add(bm.getId());
            bmChildMap.put(bm.getParentId(), children);
        }
        return bmChildMap;
    }

    @Override
    public void getBmParentId(Long bmId, Map<Long, Long> bmMap, Map<Long, List<Long>> bmParentMap) {
        Long parentId = bmMap.get(bmId);
        List<Long> parentIds = bmParentMap.get(bmId);
        // 如果当前部门没有获取过上级部门，则获取它的所有部门
        if (parentIds == null) {
            parentIds = new ArrayList<>();
            // 本级部门ID
            parentIds.add(bmId);
            if (NullUtil.isNotNull(parentId) && parentId != 0) {
                // 如果未达到根级部门，继续获取上级部门的上级部门
                List<Long> superParentIds = bmParentMap.get(parentId);
                if (superParentIds == null) {
                    getBmParentId(parentId, bmMap, bmParentMap);
                    superParentIds = bmParentMap.get(parentId);
                }
                parentIds.addAll(superParentIds);
            }
            bmParentMap.put(bmId, parentIds);
        }
    }

    @Override
    public Date getDbCurrentDate() {
        return yhzxXnzzBmMapper.getDbCurrentDate();
    }
}
