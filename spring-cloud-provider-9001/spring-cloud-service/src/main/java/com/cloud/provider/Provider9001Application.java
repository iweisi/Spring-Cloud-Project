package com.cloud.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


/**
 * 开启Feign，若是多模块需要扫描指定存放Feign接口的的路径
 */
@EnableFeignClients("com.cloud.api.feign")
@SpringCloudApplication
public class Provider9001Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider9001Application.class, args);
    }

}
