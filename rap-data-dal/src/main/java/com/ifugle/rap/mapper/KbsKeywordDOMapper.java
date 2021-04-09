package com.ifugle.rap.mapper;



import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.KbsKeywordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface KbsKeywordDOMapper  extends BaseMapper<KbsKeywordDO, Long> {
    int deleteByPrimaryKey(Long id);

    KbsKeywordDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(KbsKeywordDO record);

    int updateByPrimaryKey(KbsKeywordDO record);

    /**
     * @auther: Liuzhengyang
     * 数据初始化同步时,KBS_KEYWORD 的相关查询(order by create_time)
     */
    public List<KbsKeywordDO> selectKbsKeywordForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * @auther: Liuzhengyang
     * 数据实时同步调用,KBS_KEYWORD 的相关查询, lastCreateTime 是时间查询基点
     */
    public List<KbsKeywordDO> selectKbsKeywordForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * @auther: Liuzhengyang
     * 数据更新同步调用， KBS_KEYWORD 的相关查询 ， 查找所有更新时间大于创建时间的数据
     */
    public List<KbsKeywordDO> selectKbsKeywordForUpdateSyncWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * @auther: Liuzhengyang
     * 数据更新同步调用，KBS_KEYWORD 的相关查询 ， 查找所有更新时间大于服务器本地时间的数据
     */
    public List<KbsKeywordDO> selectKbsKeywordForUpdateSyncWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);
}
