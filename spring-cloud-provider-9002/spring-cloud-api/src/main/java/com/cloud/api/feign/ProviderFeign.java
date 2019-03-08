package com.cloud.api.feign;

import com.cloud.api.entity.User;
import com.cloud.api.fallback.ProviderFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * name 调用哪个服务的那个接口
 */
@FeignClient(name = "SPRING-CLOUD-PROVIDER", fallback = ProviderFeignFallback.class)
public interface ProviderFeign {

    @GetMapping("/cloud/{num}")
    User findUserAll(@PathVariable Integer num);
}

