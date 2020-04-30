package com.ifugle.rap.mapper.dsb;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.dsb.YhzxXnzzTpcQy;

public interface YhzxXnzzTpcQyMapper extends BaseMapper<YhzxXnzzTpcQy, Long> {

    int deleteByPrimaryKey(Long id);

    void insert(YhzxXnzzTpcQy record);

    void insertSelective(YhzxXnzzTpcQy record);

    YhzxXnzzTpcQy selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(YhzxXnzzTpcQy record);

    int updateByPrimaryKey(YhzxXnzzTpcQy record);


    List<YhzxXnzzTpcQy> selectYhzxXnzzTpcQyForSync(@Param("lastCreateTime") Date lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);
}
