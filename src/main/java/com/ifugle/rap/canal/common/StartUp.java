/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.canal.common;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.ResourcePropertySource;

import com.google.common.util.concurrent.AbstractIdleService;
import com.ifugle.rap.canalconfig.DalConfig;
import com.ifugle.rap.canalconfig.DsbDalConfig;
import com.ifugle.rap.canalconfig.ElasticSearchConfig;
import com.ifugle.rap.canalconfig.ProfileSpringConfig;
import com.ifugle.rap.canalconfig.TaskConfig;
import com.ifugle.rap.canalconfig.ScaDalConfig;
import com.ifugle.rap.constants.SystemConstants;

/***
 * 启动主入口，加载spring容器
 * 
 * @author HuangLei(wenyuan)
 * @version $Id: Container.java, v 0.1 2018年5月16日 下午1:50:06 HuangLei(wenyuan) Exp $
 */
@Import(ElasticSearchConfig.class)
public class StartUp extends AbstractIdleService {

    private ClassPathXmlApplicationContext context;

    private final static Logger            logger = LoggerFactory.getLogger(StartUp.class);

    public static void main(String[] args) {
        //加载容器
        final StartUp bootstrap = new StartUp();
        bootstrap.startAsync();
        try {
            final Object lock = new Object();
            synchronized (lock) {
                while (true) {
                    lock.wait();
                }
            }
        } catch (final InterruptedException ex) {
            logger.error("ignoreinterruption");
        }

    }

    /** 
     * @see com.google.common.util.concurrent.AbstractIdleService#startUp()
     */
    @Override
    protected void startUp() {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        try {
            System.setProperty("rap.mvc.config.default","false");
            context.getEnvironment().getPropertySources()
                .addFirst(new ResourcePropertySource("classpath:filtered.properties"));
            logger.info("filtered.properties loaded");
            if (Boolean.valueOf(System.getProperty(SystemConstants.ZHCS_ON))) {
                context.register(ProfileSpringConfig.class, TaskConfig.class, DalConfig.class, ScaDalConfig.class, ElasticSearchConfig.class);
            } else if (Boolean.valueOf(System.getProperty(SystemConstants.DSB_ON))) {
                context.register(ProfileSpringConfig.class, TaskConfig.class, DalConfig.class, DsbDalConfig.class, ElasticSearchConfig.class);
            } else {
                context.register(ProfileSpringConfig.class, TaskConfig.class, DalConfig.class, ElasticSearchConfig.class);
            }
            context.refresh();
            context.start();
            context.registerShutdownHook();

            final DataInitClient dataInitClient = context.getBean(DataInitClient.class);
            logger.info("dataInit start");
            dataInitClient.init();

            final DataSyncClient dataSyncClient = context.getBean(DataSyncClient.class);
            logger.info("dataSync start");
            dataSyncClient.sync();

            if (logger.isDebugEnabled()) {
                logger.debug("----------------provider service started successfully------------");
            }

        } catch (final IOException e) {
            logger
                .info("didn't find filtered.properties in classpath so not loading it in the AppContextInitialized");
            context.close();
        }

    }

    /** 
     * @see com.google.common.util.concurrent.AbstractIdleService#shutDown()
     */
    @Override
    protected void shutDown() throws Exception {
        context.stop();
        if (logger.isDebugEnabled()) {
            logger.debug("-----------------service stopped successfully-------------");
        }
    }

}
