/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.canalconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author HuangLei(wenyuan)
 * @version $Id: ElasticSearchConfig.java, v 0.1 2018年5月16日 下午3:38:14 HuangLei(wenyuan) Exp $
 */
@Configuration
@ImportResource({ "classpath:elasticsearch/applicationContext-elasticsearch.xml" })
@PropertySource("classpath:elasticsearch/elasticsearch.properties")
@ComponentScan(basePackages = { "com.ifugle.rap.elasticsearch", "com.ifugle.rap" })
@EnableScheduling
public class ElasticSearchConfig {

}
