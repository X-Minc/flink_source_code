package com.ifugle.rap.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.ifugle.rap.model.shuixiaomi.BotOutoundTaskDetail;
import com.ifugle.rap.model.shuixiaomi.BotOutoundTaskDetailWithBLOBs;

@Component
public interface BotOutoundTaskDetailMapper {


    int deleteByPrimaryKey(Long id);

    int insert(BotOutoundTaskDetailWithBLOBs record);

    int insertSelective(BotOutoundTaskDetailWithBLOBs record);

    BotOutoundTaskDetailWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BotOutoundTaskDetailWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(BotOutoundTaskDetailWithBLOBs record);

    int updateByPrimaryKey(BotOutoundTaskDetail record);

    List<BotOutoundTaskDetailWithBLOBs> selectBotOutoundTaskDetailForSync(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);
}
