package com.ifugle.rap.mapper;

import com.ifugle.rap.model.shuixiaomi.KbsQuestionArticleDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface KbsQuestionArticleDOMapper {
	int deleteByPrimaryKey(Long id);

	int insert(KbsQuestionArticleDO record);

	int insertSelective(KbsQuestionArticleDO record);

	KbsQuestionArticleDO selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(KbsQuestionArticleDO record);

	int updateByPrimaryKeyWithBLOBs(KbsQuestionArticleDO record);

	int updateByPrimaryKey(KbsQuestionArticleDO record);

	/**
	 * @auther: Liuzhengyang
	 * 数据实时同步调用,KBS_QUESTION_ARTICLE 的相关查询,lastCreateTime 是时间查询基点
	 */
	public List<KbsQuestionArticleDO> selectKbsQuestionArticleForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first,
			@Param("pageSize") Integer pageSize);

	/**
	 * @auther: Liuzhengyang
	 * 数据初始化同步时,KBS_QUESTION_ARTICLE 的相关查询(order by create_time)
	 */
	public List<KbsQuestionArticleDO> selectKbsQuestionArticleForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

	/**
	 * 数据实时更新同步调用, KBS_QUESTION_ARTICLE 的相关查询, 查询所有更新时间大于创建时间
	 *
	 * @param first
	 * @param pageSize
	 *
	 * @return
	 */
	public List<KbsQuestionArticleDO> selectKbsQuestionArticleForUpdateSyncWithoutLastUpdateTime(@Param("first") Integer first,
			@Param("pageSize") Integer pageSize);

	/**
	 * 数据实时更新同步调用， KBS_QUESTION_ARTICLE 的相关查询，查询所有更新时间大于服务器时间
	 *
	 * @param first
	 * @param pageSize
	 *
	 * @return
	 */
	public List<KbsQuestionArticleDO> selectKbsQuestionArticleForUpdateSyncWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime,
			@Param("first") Integer first, @Param("pageSize") Integer pageSize);
}
