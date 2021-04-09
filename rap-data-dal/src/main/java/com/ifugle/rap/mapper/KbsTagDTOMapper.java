/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.dto.KbsTagDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LiuZhengyang
 * @version $Id: KbsTagDTOMapper.java 84708 2018-11-09 08:12:37Z HuangLei $
 * @since 2018年10月15日 14:12
 */
public interface KbsTagDTOMapper  extends BaseMapper<KbsTagDTO, Long> {

    /**
     * 数据初始化同步时,查出KBS_TAG里面的标签
     *
     * @param first
     * 开始起点
     * @param pageSize
     *
     * @return
     */
    public List<KbsTagDTO> selectKbsTagForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据增量同步时调用,lastCreateTime是时间查询基点
     *
     * @param lastCreateTime
     * @param first
     * @param pageSize
     *
     * @return
     */
    @Deprecated
    public List<KbsTagDTO> selectKbsTagForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first,
                                               @Param("pageSize") Integer pageSize);

    /**
     * 数据修改同步时调用（初始化）,分页查询所有更新时间大于创建时间
     *
     * @param first
     * @param pageSize
     *
     * @return
     */
    @Deprecated
    public List<KbsTagDTO> selectKbsTagForUpdateWithoutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据修改同步时调用,lastUpdateTime是时间查询基点
     * @param lastUpdateTime
     * @param first
     * @param pageSize
     * @return
     */
    @Deprecated()
    public List<KbsTagDTO> selectKbsTagForUpateWithLastUpdateTime(@Param("lastUpdateTime") String lastUpdateTime, @Param("first") Integer first,
                                                                  @Param("pageSize") Integer pageSize);

}
