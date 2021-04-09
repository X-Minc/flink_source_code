package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.BotOutoundTaskDetail;
import com.ifugle.rap.model.shuixiaomi.BotOutoundTaskDetailWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BotOutoundTaskDetailMapper  extends BaseMapper<BotOutoundTaskDetailWithBLOBs, Long> {


    int deleteByPrimaryKey(Long id);

    BotOutoundTaskDetailWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BotOutoundTaskDetailWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(BotOutoundTaskDetailWithBLOBs record);

    int updateByPrimaryKey(BotOutoundTaskDetail record);

    List<BotOutoundTaskDetailWithBLOBs> selectBotOutoundTaskDetailForSync(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);
}
