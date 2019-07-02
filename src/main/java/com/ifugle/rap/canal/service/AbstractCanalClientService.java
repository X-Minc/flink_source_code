/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.canal.service;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.ifugle.rap.service.utils.CanalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.Assert;

/**
 * @author LiuZhengyang
 * @version $Id: AbstractCanalClientService.java 84708 2018-11-09 08:12:37Z HuangLei $
 * @since 2018年10月15日 20:33
 */
public class AbstractCanalClientService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCanalClientService.class);

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {

        public void uncaughtException(Thread t, Throwable e) {
            logger.error("parse events has an error", e);
        }
    };

    private volatile boolean running = false;

    private Thread thread = null;

    private CanalConnector connector;

    private String destination;

    public AbstractCanalClientService(String destination) {
        this(destination, null);
    }

    public AbstractCanalClientService(String destination, CanalConnector connector) {
        this.destination = destination;
        this.connector = connector;
    }

    public void start() {
        Assert.notNull(connector, "connector is null");
        thread = new Thread(new Runnable() {

            public void run() {
                process();
            }
        });

        thread.setUncaughtExceptionHandler(handler);
        running = true;
        thread.start();
    }

    private void process() {
        int batchSize = 5 * 1024;
        while (running) {
            try {
                MDC.put("destination", destination);
                connector.connect();
                connector.subscribe();
                while (running) {
                    // 获取指定数量的数据
                    Message message = connector.getWithoutAck(batchSize);
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {

                    } else {
                        CanalUtil.AnalyzeEntry(message.getEntries());
                    }
                    // 提交确认
                    connector.ack(batchId);
                    // connector.rollback(batchId); // 处理失败, 回滚数据
                }
            } catch (Exception e) {
                //logger.error("process error!", e);
            } finally {
                connector.disconnect();
                MDC.remove("destination");
            }
        }
    }

}
