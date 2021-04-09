package com.ifugle.rap.data.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ifugle
 * @version $
 * @since 4月 08, 2021 21:39
 */
@Configuration
@MapperScan(basePackages = {"com.ifugle.rap.**.mapper","com.ifugle.rap.**.dsb"}) // 扫描mapper
public class MybatisConfig {
}
