package com.ifugle.rap.canal.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifugle.rap.service.DataSyncService;

/**
 *
 * @auther: Liuzhengyang
 *
 * 数据同步clint
 */
@Service
public class DataInitClient {

    @Autowired
    private DataSyncService     dataSyncService;

    private final static Logger LOGGER = LoggerFactory.getLogger(DataInitClient.class);

    /**
     *
     * @auther: Liuzhengyang
     *
     * 用于数据全量同步,应用启动时自动调用
     */
    public void init() {
        //初始化本地文件
        dataSyncService.initLocalTime();
    }

}
