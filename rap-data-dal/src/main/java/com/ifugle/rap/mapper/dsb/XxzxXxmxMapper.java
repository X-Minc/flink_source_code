package com.ifugle.rap.mapper.dsb;


import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.dingtax.XxzxXxmx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface XxzxXxmxMapper extends BaseMapper<XxzxXxmx, Long> {

    int deleteByPrimaryKey(Long id);

    void insert(XxzxXxmx record);

    void insertSelective(XxzxXxmx record);

    XxzxXxmx selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(XxzxXxmx record);

    List<XxzxXxmx> selectXxzxXxmxForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

    int updateByPrimaryKey(XxzxXxmx record);
}