package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.BotChatRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BotChatRequestMapper extends BaseMapper<BotChatRequest, Long> {

    int deleteByPrimaryKey(Long id);

    void insert(BotChatRequest record);

    void insertSelective(BotChatRequest record);

    BotChatRequest selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BotChatRequest record);

    int updateByPrimaryKey(BotChatRequest record);

    List<BotChatRequest> selectBotChatRequestForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);
}