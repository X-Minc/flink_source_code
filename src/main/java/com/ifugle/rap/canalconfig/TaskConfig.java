/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.canalconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/***
 * 任务配置类
 * 
 * @author HuangLei(wenyuan)
 * @version $Id: TaskConfig.java, v 0.1 2018年5月16日 下午1:50:51 HuangLei(wenyuan) Exp $
 */
@Configuration
@ImportResource({ "classpath:META-INF/applicationContext.xml" })
@ComponentScan(basePackages={"com.ifugle.rap"})
@EnableScheduling
public class TaskConfig {

}
