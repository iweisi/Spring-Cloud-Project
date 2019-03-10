package com.cloud.config.client3355;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 该服务是cloud client
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Client3355Application {

    public static void main(String[] args) {
        SpringApplication.run(Client3355Application.class, args);
    }

}
