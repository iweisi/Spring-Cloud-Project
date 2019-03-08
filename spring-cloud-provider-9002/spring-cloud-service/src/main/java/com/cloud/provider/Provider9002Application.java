package com.cloud.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * 开启Feign，若是多模块需要扫描指定存放Feign接口的的路径
 */
@EnableFeignClients(basePackages = "com.cloud.api.feign")
@SpringCloudApplication
public class Provider9002Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider9002Application.class, args);
    }

}
