/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ifugle.rap.elasticsearch.enums.ChannelType;

/**
 * @author LiuZhengyang
 * @version $Id: TablesEnum.java 98228 2019-05-07 01:23:46Z HuangLei $
 * @since 2018年10月12日 14:01
 */
public enum TablesEnum {

    /**
     *
     */
    KBS_ARTICLE("KBS_ARTICLE", ChannelType.SHUIXIAOMI),

    BOT_UNAWARE_DETAIL("BOT_UNAWARE_DETAIL", ChannelType.SHUIXIAOMI),

    BOT_TRACK_DETAIL("BOT_TRACK_DETAIL", ChannelType.SHUIXIAOMI),

    BOT_CHAT_RESPONSE_MESSAGE("BOT_CHAT_RESPONSE_MESSAGE", ChannelType.SHUIXIAOMI),

    KBS_QUESTION_ARTICLE("KBS_QUESTION_ARTICLE", ChannelType.SHUIXIAOMI),

    KBS_QUESTION("KBS_QUESTION", ChannelType.SHUIXIAOMI),

    KBS_KEYWORD("KBS_KEYWORD", ChannelType.SHUIXIAOMI),

    KBS_READING("KBS_READING", ChannelType.SHUIXIAOMI),

    ZX_ARTICLE("ZHCS_ZX_ARTICLE", ChannelType.ZHCS), //智慧财税

    BOT_MEDIA("BOT_MEDIA", ChannelType.SHUIXIAOMI),

    BOT_BIZ_DATA("BOT_BIZ_DATA", ChannelType.SHUIXIAOMI),

    BOT_CONFIG_SERVER("BOT_CONFIG_SERVER",ChannelType.SHUIXIAOMI),

    YHZX_XNZZ_NSR("YHZX_XNZZ_NSR", ChannelType.DINGTAX),

    ;

    private String tableName;

    private ChannelType channelType;

    TablesEnum(String tableName, ChannelType channelType) {
        this.tableName = tableName;
        this.channelType = channelType;
    }

    public static final List<String> TABLE_NAMES = new ArrayList<String>();

    public static final Map<String, TablesEnum> TABLES = new HashMap<String, TablesEnum>(16);

    public static final Map<String, ChannelType> TABLE_CHANNEL = new HashMap<String, ChannelType>(16);

    static {
        for (TablesEnum tablesEnum : TablesEnum.values()) {
            TABLE_NAMES.add(tablesEnum.getTableName().toLowerCase());
            TABLES.put(tablesEnum.getTableName().toLowerCase(), tablesEnum);
            TABLE_CHANNEL.put(tablesEnum.getTableName().toLowerCase(), tablesEnum.getChannelType());
        }
    }

    public String getTableName() {
        return tableName;
    }

    public ChannelType getChannelType() {
        return channelType;
    }
}
