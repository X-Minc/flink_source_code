package com.ifugle.rap.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ifugle.rap" })
@EnableConfigurationProperties
public class DspApplication {
    public static void main(String[] args) {
        System.setProperty("es.client.username","elastic");
        System.setProperty("es.client.password","8RWTLqfPPrjXnTWoplWylldOS_K9u");
        System.setProperty("es.client.port","9200");
        System.setProperty("es.client.host","es-cn-4591ko4ea0005us3j.elasticsearch.aliyuncs.com");
        SpringApplication.run(DspApplication.class, args);
    }
}
