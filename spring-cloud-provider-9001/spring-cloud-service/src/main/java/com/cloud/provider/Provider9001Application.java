package com.cloud.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * 开启Feign，若是多模块需要扫描指定存放Feign接口的的路径
 * @SpringCloudApplication是个组合注解包括的是下面三个,所有我用它代替
 * 1.@SpringBootApplication
 * 2.@EnableDiscoveryClient
 * 3.@EnableCircuitBreaker
 */
@EnableFeignClients("com.cloud.api.feign")
@SpringCloudApplication
public class Provider9001Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider9001Application.class, args);
    }

}
