package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.KbsReadingDO;
import com.ifugle.rap.model.shuixiaomi.KbsReadingDOWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface KbsReadingDOMapper  extends BaseMapper<KbsReadingDOWithBLOBs, Long> {
	int deleteByPrimaryKey(Long id);

	KbsReadingDOWithBLOBs selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(KbsReadingDOWithBLOBs record);

	int updateByPrimaryKeyWithBLOBs(KbsReadingDOWithBLOBs record);

	int updateByPrimaryKey(KbsReadingDO record);

	/**
	 * @auther: Liuzhengyang
	 * 数据实时同步调用,KBS_READING 的相关查询, lastCreateTime 是时间查询基点
	 */
	public List<KbsReadingDOWithBLOBs> selectKbsReadingForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first,
                                                               @Param("pageSize") Integer pageSize);

	/**
	 * @auther: Liuzhengyang
	 * 数据初始化同步时,KBS_READING 的相关查询(order by create_time)
	 */
	public List<KbsReadingDOWithBLOBs> selectKbsReadingForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

	/**
	 * 数据实时同步调用, KBS_READING 的相关查询，查询所有更新时间大于创建时间的
	 *
	 * @param first
	 * @param pageSize
	 *
	 * @return
	 */
	public List<KbsReadingDOWithBLOBs> selectKbsReadingForUpdateSyncWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

	/**
	 * 数据实时同步调用, KBS_READING 的相关查询，查询所有更新时间大于服务器本地时间的
	 *
	 * @param lastUpdateTime
	 * @param first
	 * @param pageSize
	 *
	 * @return
	 */
	public List<KbsReadingDOWithBLOBs> selectKbsReadingForUpdateSyncWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime,
                                                                                       @Param("first") Integer first, @Param("pageSize") Integer pageSize);
}
