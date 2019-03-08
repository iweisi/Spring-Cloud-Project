package com.cloud.provider.controller;

import com.cloud.api.entity.User;
import com.cloud.provider.service.ProviderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feign接口对应的Controller
 */
@RestController
public class ProviderController {

    @Autowired
    ProviderService providerService;

    /**
     * 弱抛出异常，将会引发Hystrix机制，fallback方法
     */
    @GetMapping("/cloud/{num}")
    public User cloud(@PathVariable Integer num) {
        System.out.println("9001 控制器");
        if (num > 10) {
            throw new RuntimeException("num 不能大于 10");
        }
        return providerService.findUserAll();
    }
}
