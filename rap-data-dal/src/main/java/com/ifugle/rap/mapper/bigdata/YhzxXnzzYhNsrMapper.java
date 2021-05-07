/**
 * Copyright(C) 2019 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.mapper.bigdata;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ifugle.rap.bigdata.task.YhzxXnzzYhNsr;
import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.utils.UserOds;

/**
 * @author xuweigang
 * @version $Id$
 * @since 2019-07-23 11:40:11
 */
public interface YhzxXnzzYhNsrMapper extends BaseMapper<YhzxXnzzYhNsr, Long> {

    /**
     * 根据创建时间查询用户
     *
     * @param xnzzId
     * @param startDate
     * @param endDate
     * @param startId
     * @param pageNum
     *
     * @return
     */
    List<UserOds> listByAddUser(@Param("xnzzId") Long xnzzId, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("startId") Long startId, @Param("pageNum") int pageNum);

    /**
     * 根据修改时间查询用户
     *
     * @param xnzzId
     * @param startDate
     * @param endDate
     * @param startId
     * @param pageNum
     *
     * @return
     */
    List<UserOds> listByUpdateUser(@Param("xnzzId") Long xnzzId, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("startId") Long startId, @Param("pageNum") int pageNum);

    /**
     * 查询全部有效用户
     *
     * @param xnzzId
     * @param startId
     * @param pageNum
     *
     * @return
     */
    List<UserOds> listByAllUser(@Param("xnzzId") Long xnzzId, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("startId") Long startId, @Param("pageNum") int pageNum);
}