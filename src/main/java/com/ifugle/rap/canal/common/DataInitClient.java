package com.ifugle.rap.canal.common;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ifugle.rap.service.DataSyncService;
import com.ifugle.rap.utils.CommonUtils;

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
        //从文件读取运行状态，判断是否已经执行过
        String status = CommonUtils.readlocalTimeFile("status");
        //表示已经运行过
        if (StringUtils.isNotBlank(status) && StringUtils.equals(status, "1")) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("[DataSyncClient] already execute init data ... ");
            }
        } else {
            try {
                dataSyncService.dataSyncInit();
            } catch (Exception e) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("[DataSyncClient] init thread exception = ", e);
                }
            }
            CommonUtils.writeLocalTimeFile("1", "status");
        }

    }

}
