/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.data.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ifugle.rap.elasticsearch.core.ClusterNode;
import com.ifugle.rap.elasticsearch.core.ConfigParamConstant;

/**
 * @author HuangLei(wenyuan)
 * @version $Id: ElasticSearchConfig.java, v 0.1 2018年5月16日 下午3:38:14 HuangLei(wenyuan) Exp $
 */
@Configuration
// @ImportResource({ "classpath:elasticsearch/applicationContext-elasticsearch.xml" })
@PropertySource("classpath:elasticsearch/elasticsearch.properties")
@ComponentScan(basePackages = { "com.ifugle.rap.elasticsearch", "com.ifugle.rap" })
@EnableScheduling
@Order(Ordered.LOWEST_PRECEDENCE)
public class ElasticSearchConfig {

    public ElasticSearchConfig(Environment environment) {

    }

    @Bean
    public ClusterNode appClusterNode(Environment environment){
        List<String> hosts = new ArrayList<>();
        hosts.add(environment.getProperty(ConfigParamConstant.RAP_ES_HOST));
        return new ClusterNode(hosts,environment.getProperty(ConfigParamConstant.RAP_ES_USERNAME),environment.getProperty(ConfigParamConstant.RAP_ES_PASSWORD));
    }

}
