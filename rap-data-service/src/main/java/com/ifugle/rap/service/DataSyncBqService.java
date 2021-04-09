/**
 * Copyright(C) 2020 Hangzhou Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service;

/**
 * @since 2020年03月10日 13:45
 * @version $Id$
 * @author HuZhihuai
 */
public interface DataSyncBqService {

    /**
     * @auther: Liuzhengyang
     * 数据实时同步
     */
    void dataSyncInsertIncrementData();

    /***
     * 初始化本地文件
     */
    void initLocalTime();


}
