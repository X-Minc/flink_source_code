package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.KbsArticleDO;
import com.ifugle.rap.model.shuixiaomi.KbsArticleDOWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KbsArticleDOMapper  extends BaseMapper<KbsArticleDOWithBLOBs, Long> {
	int deleteByPrimaryKey(Long id);

	KbsArticleDOWithBLOBs selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(KbsArticleDOWithBLOBs record);

	int updateByPrimaryKeyWithBLOBs(KbsArticleDOWithBLOBs record);

	int updateByPrimaryKey(KbsArticleDO record);

	/**
	 * @auther: Liuzhengyang
	 * 数据初始化同步时,KBS_ARTICLE 的相关查询(order by create_time)
	 */
	public List<KbsArticleDOWithBLOBs> selectKbsArticleForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

	/**
	 * @auther: Liuzhengyang
	 * 数据实时同步调用,KBS_ARTICLE 的相关查询, lastCreateTime 是时间查询基点
	 */
	public List<KbsArticleDOWithBLOBs> selectKbsArticleForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first,
                                                               @Param("pageSize") Integer pageSize);

	/**
	 * @auther: Liuzhengyang
	 * 数据实时更新同步调用，KBS_ARTICLE 的相关查询,查询所有更新时间大于创建时间的
	 */
	public List<KbsArticleDOWithBLOBs> selectKbsArticleForUpdateSyncWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

	/**
	 * @auther: Liuzhengyang
	 * 数据实时更新同步调用，KBS_ARTICLE 的相关查询，查询所有更新时间大于服务器本地时间
	 */
	public List<KbsArticleDOWithBLOBs> selectKbsArticleForUpdateSyncWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

}
