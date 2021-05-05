package com.ifugle.rap.mapper.bigdata;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ifugle.rap.bigdata.task.YhzxXnzzTpcQyry;
import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.utils.UserOds;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 20:51
 */
public interface YhzxXnzzTpcQyryMapper extends BaseMapper<YhzxXnzzTpcQyry, Long> {
    /**
     * 根据修改时间查询(yhId != null)的第三方企业人员列表
     *
     * @param xnzzId
     * @param startDate
     * @param endDate
     * @param startId
     * @param pageNum
     * @return
     */
    List<UserOds> listByDeleteTpcQyry(@Param("xnzzId") Long xnzzId, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("startId") Long startId, @Param("pageNum") int pageNum);
}
