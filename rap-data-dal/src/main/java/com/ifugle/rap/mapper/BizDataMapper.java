package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.BizData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * bizdata表迁移mapper
 */
public interface BizDataMapper  extends BaseMapper<BizData, Long> {

    int deleteByPrimaryKey(Long id);

    BizData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BizData record);

    int updateByPrimaryKey(BizData record);

    /**
     * 数据初始化同步时,BotBizData 的相关查询(order by create_time)
     */
    List<BizData> selectBotBizDataForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据实时同步调用,BotBizData 的相关查询,lastCreateTime 是时间查询基点
     */
    List<BizData> selectBotBizDataForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first,
                                          @Param("pageSize") Integer pageSize);

    /**
     * 数据更新实时同步调用,BotBizData 的相关查询 , 查询所有更新时间大于创建时间
     */
    List<BizData> selectBotBizDataForUpdateWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据更新同步调用, BotBizData 的相关查询, 查询所有更新时间大于服务器时间
     */
    List<BizData> selectBotBizDataForUpdateWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first,
                                                              @Param("pageSize") Integer pageSize);

}
