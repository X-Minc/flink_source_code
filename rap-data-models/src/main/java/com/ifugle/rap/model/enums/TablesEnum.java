/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.enums;

import com.ifugle.rap.elasticsearch.enums.ChannelType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LiuZhengyang
 * @version $Id: TablesEnum.java 98228 2019-05-07 01:23:46Z HuangLei $
 * @since 2018年10月12日 14:01
 */
public enum TablesEnum {

    /**
     *
     */
    KBS_ARTICLE("_doc", ChannelType.KBS_ARTICLE),

    BOT_UNAWARE_DETAIL("_doc", ChannelType.BOT_UNAWARE_DETAIL),

    BOT_TRACK_DETAIL("_doc", ChannelType.BOT_TRACK_DETAIL),

    BOT_CHAT_RESPONSE_MESSAGE("_doc", ChannelType.BOT_CHAT_RESPONSE_MESSAGE),

    KBS_QUESTION_ARTICLE("_doc", ChannelType.KBS_QUESTION_ARTICLE),

    KBS_KEYWORD("_doc", ChannelType.KBS_KEYWORD),

    KBS_QUESTION("_doc", ChannelType.KBS_QUESTION),

    KBS_READING("_doc", ChannelType.KBS_READING),

    ZX_ARTICLE("_doc", ChannelType.ZHCS), //智慧财税

    BOT_MEDIA("_doc", ChannelType.BOT_MEDIA),

    BOT_BIZ_DATA("_doc", ChannelType.BOT_BIZ_DATA),

    BOT_CONFIG_SERVER("_doc",ChannelType.BOT_CONFIG_SERVER),

    YHZX_XNZZ_NSR("_doc", ChannelType.YHZX_XNZZ_NSR),

    YHZX_XNZZ_TPC_QY("_doc", ChannelType.YHZX_XNZZ_TPC_QY),

    BOT_OUTBOUND_TASK_DETAIL("_doc",ChannelType.BOT_OUTBOUND_TASK_DETAIL),

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
