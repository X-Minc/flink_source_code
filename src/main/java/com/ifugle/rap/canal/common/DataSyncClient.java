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
public class DataSyncClient {

    @Autowired
    private DataSyncService dataSyncService;

    private final static Logger LOGGER = LoggerFactory.getLogger(DataSyncClient.class);

    /**
     * @auther: Liuzhengyang
     * 用于数据增量更新，应用启动时自动调用
     */
    public void sync() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[DataSyncClient] execute sync ... ");
        }
        while (true) {
            try {
                Thread.sleep(10000);
                //从文件读取初始化运行状态，判断是否已经执行过数据初始化全量更新
                String status = CommonUtils.readlocalTimeFile("status");
                //如果已经运行过,那么就开始执行增量更新操作
                if (StringUtils.isNotBlank(status) && StringUtils.equals(status, "1")) {
                    // 处理不断增加的数据的线程；初始化后的添加数据同步过来，该线程不存在问题。处理正常
                    dataSyncService.dataSyncInsertIncrementData();
                    // 处理存在的数据的更新情况，2个场景
                    // 一种 是没有更新过，
                    // 一种 是更新过历史：该线程处理要更新的数据有遗漏的情况，要特别处理时间差的问题.
                    // 两种场景更新有时间的场景和更新无时间的场景
                    dataSyncService.dataUpdateSync();
                } else {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("[DataSyncClient] don't execute init data ... ");
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Thread excute sync data error e = ", e);
            }

        }
    }

}
