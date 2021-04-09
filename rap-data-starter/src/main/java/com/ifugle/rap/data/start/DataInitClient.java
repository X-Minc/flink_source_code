package com.ifugle.rap.data.start;

import com.ifugle.rap.service.DataSyncBqService;
import com.ifugle.rap.service.DataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class DataInitClient {

    @Autowired
    private DataSyncService dataSyncService;
    @Autowired
    private DataSyncBqService dataSyncBqService;

    private final static Logger LOGGER = LoggerFactory.getLogger(DataInitClient.class);

    /**
     *
     */
    public void init() {
        //初始化本地文件
        dataSyncService.initLocalTime();
        dataSyncBqService.initLocalTime();
    }

}
