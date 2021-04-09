package com.ifugle.rap.mapper.zhcs;

import com.ifugle.rap.model.zhcs.ZxArticle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ZxArticleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ZxArticle record);

    int insertSelective(ZxArticle record);

    ZxArticle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ZxArticle record);

    int updateByPrimaryKey(ZxArticle record);

    /***
     * 初始化同步使用
     * @param first
     * @param pageSize
     * @return
     */
    List<ZxArticle> selectZxArticleForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /***
     * 根据最后创建时间同步
     * @param lastCreateTime
     * @param first
     * @param pageSize
     * @return
     */
    List<ZxArticle> selectZxArticleForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /***
     * 全部修改时间大于开始时间同步
     * @param first
     * @param pageSize
     * @return
     */
    List<ZxArticle> selectZxArticleForUpdateWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /***
     * 根据大于最后更新时间同步
     * @param lastUpdateTime
     * @param first
     * @param pageSize
     * @return
     */
    List<ZxArticle> selectZxArticleForUpdateWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first,
                                                               @Param("pageSize") Integer pageSize);


}
