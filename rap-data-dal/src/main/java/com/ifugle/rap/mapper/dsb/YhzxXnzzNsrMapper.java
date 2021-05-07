package com.ifugle.rap.mapper.dsb;

import com.ifugle.rap.bigdata.task.CompanyOds;
import com.ifugle.rap.bigdata.task.YhzxXnzzNsrBq;
import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface YhzxXnzzNsrMapper extends BaseMapper<YhzxXnzzNsr, Long> {

    int deleteByPrimaryKey(Long id);

    void insert(YhzxXnzzNsr record);

    void insertSelective(YhzxXnzzNsr record);

    YhzxXnzzNsr selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(YhzxXnzzNsr record);

    int updateByPrimaryKey(YhzxXnzzNsr record);


    /***
     * 初始化同步使用
     * @param first
     * @param pageSize
     * @return
     */
    List<YhzxXnzzNsr> selectYhzxXnzzNsrForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /***
     * 根据最后创建时间同步
     * @param lastCreateTime
     * @param first
     * @param pageSize
     * @return
     */
    List<YhzxXnzzNsr> selectYhzxXnzzNsrForSync(@Param("lastCreateTime") Date lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /***
     * 全部修改时间大于开始时间同步
     * @param first
     * @param pageSize
     * @return
     */
    List<YhzxXnzzNsr> selectYhzxXnzzNsrForUpdateWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /***
     * 根据大于最后更新时间同步
     * @param lastUpdateTime
     * @param first
     * @param pageSize
     * @return
     */
    List<YhzxXnzzNsr> selectYhzxXnzzNsrForUpdateWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first,
                                                                   @Param("pageSize") Integer pageSize);
    /**
     * 根据ID查询企业信息
     *
     * @param xnzzId
     * @param nsrIds
     *
     * @return
     */
    List<CompanyOds> listByIds(Long xnzzId, List<Long> nsrIds);
    /**
     * 根据修改时间查询时间范围内的企业
     *
     * @param xnzzId
     * @param startDate
     * @param endDate
     * @param startId
     * @param pageNum
     *
     * @return
     */
    List<CompanyOds> listByUpdateCompany(@Param("xnzzId") Long xnzzId, @Param("startDate") Date startDate,
            @Param("endDate") Date endDate, @Param("startId") Long startId, @Param("pageNum") int pageNum);

    /**
     * 根据ID查询企业标签
     *
     * @param nsrIds
     *
     * @return
     */
    List<YhzxXnzzNsrBq> listBqByNsrId(@Param("xnzzId") Long xnzzId, @Param("nsrIds") List<Long> nsrIds);
}
