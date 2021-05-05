package com.ifugle.rap.bigdata.task.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.service.BiDmSwjgService;
import com.ifugle.rap.mapper.bigdata.BiDmSwjgMapper;
import com.ifugle.rap.mapper.bigdata.YhzxXnzzBmMapper;
import com.ifugle.util.NullUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:05
 */
@Service
@Slf4j
public class BiDmSwjgServiceImpl implements BiDmSwjgService {

    @Autowired
    private BiDmSwjgMapper biDmSwjgMapper;

    @Autowired
    private YhzxXnzzBmMapper yhzxXnzzBmMapper;

    @Override
    public List<BiDmSwjg> listXnzzForAllInsert() {
        return biDmSwjgMapper.listXnzzForAllInsert(null);
    }

    @Override
    public BiDmSwjg getXnzzForUpdate(Long xnzzId) {
        List<BiDmSwjg> list = biDmSwjgMapper.listXnzzForUpdate(xnzzId);
        if (NullUtil.isNotNull(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<BiDmSwjg> listXnzzForUpdate() {
        return biDmSwjgMapper.listXnzzForUpdate(null);
    }
}
