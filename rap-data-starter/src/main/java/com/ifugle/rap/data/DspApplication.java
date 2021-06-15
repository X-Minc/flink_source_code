package com.ifugle.rap.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ifugle.rap" })
@EnableAdminServer
public class DspApplication {
    public static void main(String[] args) {
        System.setProperty("rap.sql.maxRows","100000");
        System.setProperty("rap.allow.snapshot","true");
        System.setProperty("rap.crypto.key","IzxxW5L7UAdFl1huMCrg2TKs6+B/WeTCFCY+h2M2n5c");
        SpringApplication.run(DspApplication.class, args);
    }
}
