package com.ifugle.rap.mapper.dsb;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.dsb.YhzxXnzzNsr;
import com.ifugle.rap.model.shuixiaomi.BotChatResponseMessageDO;

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
    List<YhzxXnzzNsr> selectYhzxXnzzNsrForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

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

}
