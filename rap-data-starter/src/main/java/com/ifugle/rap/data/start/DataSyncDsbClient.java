package com.ifugle.rap.data.start;

import com.ifugle.rap.data.config.AppDsbCondition;
import com.ifugle.rap.service.DataSyncBqService;
import com.ifugle.rap.service.DataSyncService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

/**
 * @auther: Liuzhengyang
 * 丁税宝数据同步client
 */
@Conditional(AppDsbCondition.class)
@Service
public class DataSyncDsbClient {

    @Autowired
    private DataSyncBqService dataSyncBqService;


    private final static Logger LOGGER = LoggerFactory.getLogger(DataSyncDsbClient.class);

    /**
     * @auther: Liuzhengyang
     * 用于数据增量更新，应用启动时自动调用
     */
    public void sync() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[DataSyncClient] execute dsb sync ... ");
        }
<<<<<<< HEAD
        //启动丁税宝开启
=======
>>>>>>> feature_mumu_1.7.0_sc
        new Thread(this::syncDsb).start();
    }


    /**
     * @auther: Liuzhengyang
     * 用于数据增量更新，应用启动时自动调用
     */
    public void syncDsb() {
        int i=0;
        while (true) {
            try {
                LOGGER.info("Thread excute sync dsb data start i= "+i);
                Thread.sleep(10000);
                //从文件读取初始化运行状态，判断是否已经执行过数据初始化全量更新
                // 处理不断增加的数据的线程；初始化后的添加数据同步过来，该线程不存在问题。处理正常
                dataSyncBqService.dataSyncInsertIncrementData();
            } catch (Exception e) {
                LOGGER.error("Thread excute sync dsb data error e = ", e);
            }
            i++;
        }
    }

}
