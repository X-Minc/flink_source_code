/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.test;

import java.util.Set;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ifugle.rap.canalconfig.ElasticSearchConfig;
import com.ifugle.rap.core.config.annotation.AppContextConfig;
import com.ifugle.rap.core.config.annotation.AppContextInitializer;
import com.ifugle.rap.core.config.annotation.AppGlobalInitializer;

import javax.servlet.ServletConfig;


@WebAppConfiguration
@ActiveProfiles(profiles = "test")
@ContextConfiguration(initializers = { AppGlobalInitializer.class, AppContextInitializer.class }, classes = { ServletConfig.class,
        AppContextConfig.class,  ElasticSearchConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = { "com.ifugle" })
public class BaseTest {

    static {
        System.setProperty("rap.bot.admin.server.dd", "true");
        System.setProperty("rap.mq.rocket.username", "duLCaybnI9oOyOdZ");
        System.setProperty("rap.mq.rocket.password", "RCHE8NY4DkIldIei3O4ub61FwxEaGa");
        //System.setProperty("rap.mq.rocket.topic", "charpty");
        //开发库
        //System.setProperty("dsbdev", "mysql://dsbdev:rap1bpm2ifm3qrm4Dev@172.16.16.5:33066/dsbdev");
        //System.setProperty("rap.defaultJDBC", "dsbdev");
        //测试库
        //System.setProperty("bot_test", "mysql://bot_test:rap1bpm2ifm3qrm4Test@172.16.16.5:35066/bot_test");
        //System.setProperty("rap.defaultJDBC", "bot_test");
        //System.setProperty("dsbtest", "mysql://dsbtest:rap1bpm2ifm3qrm4Test@127.0.0.1:33066/dsbtest");
        //System.setProperty("rap.defaultJDBC", "dsbtest");
        // 隧道开发库
        //System.setProperty("dsbtest", "mysql://dsbtest:rap1bpm2ifm3qrm4Test@127.0.0.1:33066/dsbtest");
        System.setProperty("rap.sql.queryTimeout", "8");
        System.setProperty("rap.es.url", "es-cn-v0h0rhyly0004fg0r.public.elasticsearch.aliyuncs.com:9200");
        System.setProperty("rap.es.username", "elastic");
        System.setProperty("rap.es.password", "acUkc8kjpuLAFuqJzB4MHNdRGXpw6N");
        System.setProperty("log4j.logger.com.ifugle.rap.bot.summary.mapper", "error");
        System.setProperty("rap.mvc.config.default","false");
        System.setProperty("rap.config.datasource.default","false");
        System.setProperty("rap.service.config.default", "true");
    }

}

