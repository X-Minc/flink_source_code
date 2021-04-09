package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.BotMediaDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BotMediaDOMapper  extends BaseMapper<BotMediaDO, Long> {

    int deleteByPrimaryKey(Long id);

    BotMediaDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BotMediaDO record);

    int updateByPrimaryKey(BotMediaDO record);

    /**
     * 数据初始化同步时,BOT_MEDIA 的相关查询,（order by create_time）
     * @param first 起始页
     * @param pageSize 每页长度
     * @return
     */
    public List<BotMediaDO> selectBotMediaForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据实时同步调用，BOT_MEDIA 的相关查询, lastCreateTime 是时间查询基点
     * @param lastCreateTime 最后创建时间
     * @param first 起始页
     * @param pageSize 每页长度
     * @return
     */
    public List<BotMediaDO> selectBotMediaForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据更新同步调用， BOT_MEDIA 的相关查询 ， 查找所有更新时间大于服务器本地时间的数据
     * @param first 起始页
     * @param pageSize 每页长度
     * @param lastUpdateTime 最后更新时间
     * @return
     */
    public List<BotMediaDO> selectBotMediaWithLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize, @Param("lastUpdateTime") String lastUpdateTime);

    /**
     *  数据更新同步调用， BOT_MEDIA 的相关查询 ， 查找所有更新时间大于创建时间的数据
     * @param first 起始页
     * @param pageSize 每页长度
     * @return
     */
    public List<BotMediaDO> selectBotMediaWithOutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);


}
