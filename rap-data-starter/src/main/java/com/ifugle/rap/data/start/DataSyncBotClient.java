package com.ifugle.rap.data.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import com.ifugle.rap.data.config.AppBotCondition;
import com.ifugle.rap.data.config.AppDsbCondition;
import com.ifugle.rap.service.DataSyncService;

/**
 * 税小蜜文件操作类
 * @author ifugle
 * @version $
 * @since 4月 12, 2021 17:36
 */
@Conditional(AppBotCondition.class)
@Service
public class DataSyncBotClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataSyncDsbClient.class);

    @Autowired
    private DataSyncService dataSyncService;


    public void syncBot(){
        LOGGER.info("Thread excute sync BOT data starting ");
        while (true) {
            try {
                LOGGER.info("Thread excute sync data ");
                Thread.sleep(10000);
                //从文件读取初始化运行状态，判断是否已经执行过数据初始化全量更新
                // 处理不断增加的数据的线程；初始化后的添加数据同步过来，该线程不存在问题。处理正常
                dataSyncService.dataSyncInsertIncrementData();
            } catch (Exception e) {
                LOGGER.error("Thread excute sync data error e = ", e);
            }
        }
    }
}
