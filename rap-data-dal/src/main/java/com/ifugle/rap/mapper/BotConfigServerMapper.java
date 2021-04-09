/**
 * Copyright(C) 2018 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.mapper;

import com.ifugle.rap.core.mapper.BaseMapper;
import com.ifugle.rap.model.shuixiaomi.BotConfigServer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Yanlg
 * @version $Id: BotConfigServerMapper.java 92784 2019-01-30 11:50:20Z JiaoChao $
 * @since 2018-01-19 13:41:28
 */
public interface BotConfigServerMapper extends BaseMapper<BotConfigServer, Long> {
    /**
     * 数据初始化同步时,BOT_CONFIG_SERVER 的相关查询,（order by create_time）
     *
     * @param first 起始页
     * @param pageSize 每页长度
     */
    public List<BotConfigServer> selectBotConfigServerForInit(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据实时同步调用，BOT_CONFIG_SERVER 的相关查询, lastCreateTime 是时间查询基点
     *
     * @param lastCreateTime 最后创建时间
     * @param first 起始页
     * @param pageSize 每页长度
     */
    public List<BotConfigServer> selectBotConfigServerForSync(@Param("lastCreateTime") String lastCreateTime, @Param("first") Integer first, @Param("pageSize") Integer pageSize);

    /**
     * 数据更新同步调用， BOT_CONFIG_SERVER 的相关查询 ， 查找所有更新时间大于服务器本地时间的数据
     *
     * @param first 起始页
     * @param pageSize 每页长度
     * @param lastUpdateTime 最后更新时间
     */
    public List<BotConfigServer> selectBotConfigServerWithLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize, @Param("lastUpdateTime") String lastUpdateTime);

    /**
     * 数据更新同步初始化时调用，BOT_CONFIG_SERVER 的相关查询 ，查找所有更新时间大于创建时间的数据
     *
     * @param first 起始页
     * @param pageSize 每页长度
     */
    public List<BotConfigServer> selectBotConfigServerWithOutLastUpdateTime(@Param("first") Integer first, @Param("pageSize") Integer pageSize);

}
