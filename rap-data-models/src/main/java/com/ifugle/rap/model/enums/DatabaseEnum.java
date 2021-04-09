/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuZhengyang
 * @version $Id: DatabaseEnum.java 84708 2018-11-09 08:12:37Z HuangLei $
 * @since 2018年10月12日 10:56
 */
public enum DatabaseEnum {

    /**
     * mysql数据库
     */
    DATABASE_MYSQL("mysql"),;

    private String databaseName;

    DatabaseEnum(String databaseName) {
        this.databaseName = databaseName;
    }

    public static final List<String> DATABASE_NAME = new ArrayList<String>();

    static {
        for (DatabaseEnum db : DatabaseEnum.values()) {
            DATABASE_NAME.add(db.getDatabaseName().toLowerCase());
        }
    }

    public String getDatabaseName() {
        return databaseName;
    }

}
