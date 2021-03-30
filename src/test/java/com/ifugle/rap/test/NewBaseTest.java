/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.ifugle.rap.core.config.annotation.AppContextConfig;
import com.ifugle.rap.core.config.annotation.AppContextInitializer;
import com.ifugle.rap.core.config.annotation.AppGlobalInitializer;
/**
 * @author LiuZhengyang
 * @version $Id$
 * @since 2019年02月27日 14:10
 */
@WebAppConfiguration
@ActiveProfiles(profiles = "test")
@ContextConfiguration(initializers = { AppGlobalInitializer.class, AppContextInitializer.class }, classes = {
        AppContextConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class NewBaseTest {

    static {
        System.setProperty("rap.mq.rocket.username", "duLCaybnI9oOyOdZ");
        System.setProperty("rap.mq.rocket.password", "RCHE8NY4DkIldIei3O4ub61FwxEaGa");
        System.setProperty("rap.mq.rocket.topic", "charpty");
        //开发库
        //System.setProperty("dsbdev", "mysql://dsbdev:rap1bpm2ifm3qrm4Dev@172.16.16.5:33066/dsbdev");
        //System.setProperty("rap.defaultJDBC", "dsbdev");
        //测试库
        System.setProperty("dsbtest", "mysql://dsbtest:rap1bpm2ifm3qrm4Test@172.16.16.5:33066/dsbtest");
        System.setProperty("rap.defaultJDBC", "dsbtest");
        //隧道开发库
        //System.setProperty("dsbtest", "mysql://dsbtest:rap1bpm2ifm3qrm4Test@127.0.0.1:33066/dsbtest");
        System.setProperty("rap.sql.queryTimeout", "8");
        System.setProperty("rap.es.url", "es-cn-v0h0rhyly0004fg0r.public.elasticsearch.aliyuncs.com:9200");
        System.setProperty("rap.es.username", "elastic");
        System.setProperty("rap.es.password", "acUkc8kjpuLAFuqJzB4MHNdRGXpw6N");
        System.setProperty("log4j.logger.com.ifugle.rap.bot.summary.mapper", "error");
        System.setProperty("rap.service.config.default", "true");
    }
}
