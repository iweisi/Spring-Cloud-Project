package com.cloud.config.client3355.com.cloud.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientRest {

    @Value("${spring.application.name}")
    private String applicationName;


    @Value("${server.port}")
    private String port;

    @Value("${env}")
    private String env;

    @RequestMapping("/config")
    public String getConfig() {
        return "applicationName : " + applicationName +
                "port:" + port +
                "env:" + env;
    }
}
