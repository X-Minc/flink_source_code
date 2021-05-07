package com.ifugle.rap.mapper.bigdata;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ifugle.rap.bigdata.task.YhzxXnzzTpcJgry;
import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.utils.UserOds;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 20:49
 */
public interface YhzxXnzzTpcJgryMapper extends BaseMapper<YhzxXnzzTpcJgry, Long> {
    /**
     * 根据修改时间查询(yhId != null)的第三方机构人员列表
     *
     * @param xnzzId
     * @param startDate
     * @param endDate
     * @param startId
     * @param pageNum
     *
     * @return
     */
    List<UserOds> listByDeleteTpcJgry(@Param("xnzzId") Long xnzzId, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("startId") Long startId, @Param("pageNum") int pageNum);

    List<UserOds> listByAddTpcJgry(Long xnzzId, Date startDate, Date endDate, Long startId, int pageNum);

    List<UserOds> listByUpdateTpcJgryByBmys(Long xnzzId, Date startDate, Date endDate, Long startId, int pageNum);
}
