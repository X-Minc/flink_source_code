package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.dingtax.YhzxxnzzcyDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface YhzxxnzzcyDOMapper  extends BaseMapper<YhzxxnzzcyDO, Long> {
    int deleteByPrimaryKey(Long id);

    YhzxxnzzcyDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(YhzxxnzzcyDO record);

    int updateByPrimaryKey(YhzxxnzzcyDO record);

    /**
     * @auther: Liuzhengyang
     * 数据初始化同步时,Yhzxxnzzcy 的相关查询(order by create_time)
     */
    public List<YhzxxnzzcyDO> selectYhzxxnzzcyForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * @auther: Liuzhengyang
     * 数据实时同步调用,Yhzxxnzzcy 的相关查询, lastCreateTime 是时间查询基点
     */
    public List<YhzxxnzzcyDO> selectYhzxxnzzcyForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * @auther: Liuzhengyang
     * 数据更新同步调用， Yhzxxnzzcy 的相关查询 ， 查找所有更新时间大于创建时间的数据
     */
    public List<YhzxxnzzcyDO> selectYhzxxnzzcyForUpdateSyncWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * @auther: Liuzhengyang
     * 数据更新同步调用，Yhzxxnzzcy 的相关查询 ， 查找所有更新时间大于服务器本地时间的数据
     */
    public List<YhzxxnzzcyDO> selectYhzxxnzzcyForUpdateSyncWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);


}
