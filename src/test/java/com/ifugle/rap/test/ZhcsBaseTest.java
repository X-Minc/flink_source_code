/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.test;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(locations = { "classpath:META-INF/applicationContext.xml",
        "classpath:META-INF/applicationContext-dal-sca.xml",
        "classpath:config/ScaMapperConfig.xml"})
@PropertySource("classpath:env/config-test.properties")
@ComponentScan(basePackages = { "com.ifugle.rap" })
public class ZhcsBaseTest {

    static {
//        System.setProperty("jdbc.driver","com.mysql.jdbc.Driver");
//        System.setProperty("jdbc.url","jdbc:mysql://47.97.195.59:33066/dsbtest?autoCommit=true&useUnicode=true&autoReconnect=true");
//        System.setProperty("jdbc.username","dsbtest");
//        System.setProperty("jdbc.password","rap1bpm2ifm3qrm4Test");
        System.setProperty("rap.es.url","es-cn-v0h0rhyly0004fg0r.public.elasticsearch.aliyuncs.com:9200");
        System.setProperty("rap.es.username","elastic");
        System.setProperty("rap.es.password","acUkc8kjpuLAFuqJzB4MHNdRGXpw6N");
        System.setProperty("rap.config.datasource","false");
        System.setProperty("rap.mvc.config","false");
        System.setProperty("rap.redis.server","47.97.195.59:16379");
        System.setProperty("rap.redis.auth.password","dsbwansui");
    }

}

