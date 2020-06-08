/**
 * Copyright(C) 2017 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.mapper.dsb;

import java.util.Date;
import java.util.List;
import java.util.Set;
import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.dsb.YhzxXnzzBqNsr;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import org.apache.ibatis.annotations.Param;

/**
 * @author HuZhihuai
 * @version $Id: YhzxXnzzNsrBqMapper.java 98594 2019-05-10 15:52:02Z HuZhihuai $
 * @since 2017-05-27 20:57:51
 */
public interface YhzxXnzzNsrBqMapper extends BaseMapper<YhzxXnzzNsr, Long> {

    /**
     * 查询变化的
     *
     * @param lastCreateTime
     * @param first
     * @param pageSize
     *
     * @return
     */
    List<YhzxXnzzBqNsr> listChange(@Param("lastCreateTime") Date lastCreateTime,@Param("first") int first, @Param("pageSize") int pageSize);

    /**
     * 查询变化后的最终态
     *
     * @param nsrIds
     * @return
     */
    List<YhzxXnzzBqNsr> listByNsrId(@Param("nsrIds") Set<Long> nsrIds);
}