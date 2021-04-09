package com.ifugle.rap.mapper;


import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.BotUnawareDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @auther: Liuzhengyang
 *
 */
public interface BotUnawareDetailDOMapper  extends BaseMapper<BotUnawareDetailDO, Long> {
    int deleteByPrimaryKey(Long id);

    BotUnawareDetailDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BotUnawareDetailDO record);

    int updateByPrimaryKey(BotUnawareDetailDO record);

    /**
     * @auther: Liuzhengyang
     * 数据初始化同步时,BOT_UNAWARE_DETAIL 的相关查询(order by create_time)
     */
    public List<BotUnawareDetailDO> selectBotUnawareDetailForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据实时同步调用，BOT_UNAWARE_DETAIL 的相关查询, lastCreateTime 是时间查询基点
     *
     * @param lastCreateTime
     * @param first
     * @param pageSize
     * @auther: Liuzhengyang
     */
    public List<BotUnawareDetailDO> selectBotUnawareDetailForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据更新同步调用， BOT_UNAWARE_DETAIL 的相关查询 ， 查找所有更新时间大于服务器本地时间的数据
     * @param first first
     * @param pageSize pageSize
     * @param lastUpdateTime lastUpdateTime
     * @return
     */
    public List<BotUnawareDetailDO> selectBotUnawareDetailWithLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize, @Param("lastUpdateTime") String lastUpdateTime);

    /**
     * 数据更新同步调用， BOT_UNAWARE_DETAIL 的相关查询 ， 查找所有更新时间大于创建时间的数据
     * @param first first
     * @param pageSize pageSize
     * @return  List<BotUnawareDetailDO>
     */
    public List<BotUnawareDetailDO> selectBotUnawareDetailWitOuthLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);


}
