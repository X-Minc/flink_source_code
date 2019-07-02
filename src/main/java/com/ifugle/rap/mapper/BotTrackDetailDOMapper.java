package com.ifugle.rap.mapper;


import com.ifugle.rap.model.shuixiaomi.BotTrackDetailDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @auther: Liuzhengyang
 *
 */
@Component
public interface BotTrackDetailDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BotTrackDetailDO record);

    int insertSelective(BotTrackDetailDO record);

    BotTrackDetailDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BotTrackDetailDO record);

    int updateByPrimaryKey(BotTrackDetailDO record);

    /**
     * @auther: Liuzhengyang
     * 数据初始化同步时,BOT_TRACK_DETAIL 的相关查询(order by create_time)
     */
    public List<BotTrackDetailDO> selectBotTrackDetailForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * @auther: Liuzhengyang
     * 数据实时同步调用,BOT_TRACK_DETAIL 的相关查询,lastUpdateTime 是时间查询基点
     */
    public List<BotTrackDetailDO> selectBotTrackDetailForSync(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

}
