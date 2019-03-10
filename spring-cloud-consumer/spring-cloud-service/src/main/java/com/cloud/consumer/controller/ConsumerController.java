package com.cloud.consumer.controller;


import com.cloud.api.entity.User;
import com.cloud.api.feign.ProviderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ProviderFeign 提供方的jar包提供的Feign接口，进行调用服务
 */
@RestController
public class ConsumerController {


    @Resource
    ProviderFeign providerFeign;

    @GetMapping("/hello/{num}")
    public User hello(@PathVariable Integer num) {
        System.out.println("consumer 控制器");
        return providerFeign.findUserAll(num);
    }
}
