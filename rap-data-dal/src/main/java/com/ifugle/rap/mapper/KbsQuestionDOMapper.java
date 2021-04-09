package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.KbsQuestionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface KbsQuestionDOMapper  extends BaseMapper<KbsQuestionDO, Long> {
	int deleteByPrimaryKey(Long id);

	KbsQuestionDO selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(KbsQuestionDO record);

	int updateByPrimaryKey(KbsQuestionDO record);

	/**
	 * @auther: Liuzhengyang
	 * 数据初始化同步时,KBS_QUESTION 的相关查询(order by create_time)
	 */
	public List<KbsQuestionDO> selectKbsQuestionForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

	/**
	 * @auther: Liuzhengyang
	 * 数据实时同步调用,KBS_QUESTION 的相关查询,lastCreateTime 是时间查询基点
	 */
	public List<KbsQuestionDO> selectKbsQuestionForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first,
                                                        @Param("pageSize") Integer pageSize);

	/**
	 * @auther: Liuzhengyang
	 * 数据更新实时同步调用,KBS_QUESTION 的相关查询 , 查询所有更新时间大于创建时间
	 */
	public List<KbsQuestionDO> selectKbsQuestionForUpdateWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

	/**
	 * @auther: Liuzhengyang
	 * 数据更新同步调用, KBS_QUESTION 的相关查询, 查询所有更新时间大于服务器时间
	 */
	public List<KbsQuestionDO> selectKbsQuestionForUpdateWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first,
                                                                            @Param("pageSize") Integer pageSize);

}
