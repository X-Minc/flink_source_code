package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.BotChatResponseMessageDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther: Liuzhengyang
 */
public interface BotChatResponseMessageDOMapper  extends BaseMapper<BotChatResponseMessageDO, Long> {

    int deleteByPrimaryKey(Long id);

    BotChatResponseMessageDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BotChatResponseMessageDO record);

    int updateByPrimaryKey(BotChatResponseMessageDO record);

    /**
     * @auther: Liuzhengyang
     * 数据实时同步调用,BOT_CHAT_RESPONSE_MESSAGE 的相关查询,lastCreateTime 是时间查询基点
     */
    public List<BotChatResponseMessageDO> selectBotChatResponseMessageForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);


    /**
     * @auther: Liuzhengyang
     * 数据初始化同步时,BOT_CHAT_RESPONSE_MESSAGE 的相关查询(order by ID)
     */
    public List<BotChatResponseMessageDO> selectBotChatResponseMessageForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

}
