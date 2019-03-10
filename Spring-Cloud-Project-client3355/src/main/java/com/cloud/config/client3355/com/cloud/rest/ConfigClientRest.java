package com.cloud.config.client3355.com.cloud.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// 实时刷新
@RefreshScope
public class ConfigClientRest {

    @Value("${spring.application.name}")
    private String applicationName;


    @Value("${server.port}")
    private String port;

    @Value("${env}")
    private String env;

    @RequestMapping("/config")
    public void getConfig() {
        System.out.println("applicationName : " + applicationName);
        System.out.println("port : " + port);
        System.out.println("env : " + env);
    }
}
