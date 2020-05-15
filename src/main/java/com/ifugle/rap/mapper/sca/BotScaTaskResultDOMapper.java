/**
 * Copyright(C) 2020 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.mapper.sca;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.sca.BotScaTaskResultDO;

/**
 * @author jwj
 * @version $Id$
 * @since May 13, 2020 8:41:36 PM
 */
public interface BotScaTaskResultDOMapper extends BaseMapper<BotScaTaskResultDO, Long> {
	BotScaTaskResultDO selectByPrimaryKey(Long id);

	/**
	 * 数据初始化同步时,BOT_SCA_TASK_RESULT 的相关查询(order by create_time)
	 */
	public List<BotScaTaskResultDO> selectBotScaTaskResultForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

	/**
	 * 数据实时同步调用,BOT_SCA_TASK_RESULT 的相关查询,lastCreateTime 是时间查询基点
	 */
	public List<BotScaTaskResultDO> selectBotScaTaskResultForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first,
			@Param("pageSize") Integer pageSize);

	/**
	 * 数据更新实时同步调用,BOT_SCA_TASK_RESULT 的相关查询 , 查询所有更新时间大于创建时间
	 */
	public List<BotScaTaskResultDO> selectBotScaTaskResultForUpdateWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

	/**
	 * 数据更新同步调用, BOT_SCA_TASK_RESULT 的相关查询, 查询所有更新时间大于服务器时间
	 */
	public List<BotScaTaskResultDO> selectBotScaTaskResultForUpdateWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first,
			@Param("pageSize") Integer pageSize);
}
