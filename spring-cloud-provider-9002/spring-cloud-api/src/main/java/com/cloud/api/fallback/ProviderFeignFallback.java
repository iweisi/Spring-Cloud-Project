package com.cloud.api.fallback;

import com.cloud.api.entity.User;
import com.cloud.api.feign.ProviderFeign;
import org.springframework.stereotype.Component;

@Component
public class ProviderFeignFallback implements ProviderFeign {
    @Override
    public User findUserAll(Integer num) {
        return new User("双11高峰，查询订单请2点以后再试", "500");
    }
}
