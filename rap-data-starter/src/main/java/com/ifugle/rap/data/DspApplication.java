package com.ifugle.rap.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ifugle.rap" })
public class DspApplication {
    public static void main(String[] args) {
        System.setProperty("rap.sql.maxRows","100000");
        SpringApplication.run(DspApplication.class, args);
    }
}
