/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.canalconfig;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/***
 * 环境配置类 
 *
 * @author HuangLei(wenyuan)
 * @version $Id: ProfileSpringConfig.java, v 0.1 2018年5月16日 下午1:50:32 HuangLei(wenyuan) Exp $
 */

public class ProfileSpringConfig {

    private final Logger LOG = LoggerFactory.getLogger(ProfileSpringConfig.class);

    /** 开发环境的profile名称 */
    public static final String PROFILE_NAME_DEV = "dev";

    /** 测试环境的profile名称 */
    public static final String PROFILE_NAME_TEST = "test";

    /** 线上环境的profile名称 */
    public static final String PROFILE_NAME_PROD = "prod";

    String channel = System.getProperty("rap.channel");

    @Profile(PROFILE_NAME_DEV)
    @Bean(name = "propertyPlaceholderConfigurer")
    public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurerDev() {
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        if(StringUtils.equalsIgnoreCase("dingtalk",channel)) {
            ppc.setLocation(new ClassPathResource("env/dd-config-dev.properties"));
        }else{
            ppc.setLocation(new ClassPathResource("env/config-dev.properties"));
        }

        LOG.warn("config-dev.properties loaded");
        return ppc;
    }

    @Profile(PROFILE_NAME_TEST)
    @Bean(name = "propertyPlaceholderConfigurer")
    public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurerTest() {
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        if(StringUtils.equalsIgnoreCase("dingtalk",channel)) {
            ppc.setLocation(new ClassPathResource("env/dd-config-test.properties"));
        }else{
            ppc.setLocation(new ClassPathResource("env/config-test.properties"));
        }

        LOG.warn("config-test.properties loaded");
        return ppc;
    }

    @Profile(PROFILE_NAME_PROD)
    @Bean(name = "propertyPlaceholderConfigurer")
    public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurerProd() {
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        if(StringUtils.equalsIgnoreCase("dingtalk",channel)) {
            ppc.setLocation(new ClassPathResource("env/dd-config-prod.properties"));
        }else{
            ppc.setLocation(new ClassPathResource("env/config-prod.properties"));
        }

        LOG.warn("config-prod.properties loaded");
        return ppc;
    }

}
