package com.ifugle.rap.data.start;

import com.ifugle.rap.service.DataSyncBqService;
import com.ifugle.rap.service.DataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 本地初始化磁盘时间文件
 */
@Service
public class DataInitClient {

    @Autowired
    private DataSyncService dataSyncService;

    @Autowired
    private DataSyncBqService dataSyncBqService;

    private final static Logger LOGGER = LoggerFactory.getLogger(DataInitClient.class);

    /**
     * 初始化文件
     */
    public void init() {
        //初始化本地文件
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("初始化本地文件>........");
        }
        //税小蜜初始化本地文件
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("bot starting init time ");
        }
        dataSyncService.initLocalTime();
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("dsb starting init time ");
        }
        //丁税宝初始化文件
        dataSyncBqService.initLocalTime();
    }

}
