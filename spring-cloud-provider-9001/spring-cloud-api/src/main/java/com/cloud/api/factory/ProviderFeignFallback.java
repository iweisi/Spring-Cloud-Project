package com.cloud.api.factory;

import com.cloud.api.entity.User;
import com.cloud.api.feign.ProviderFeign;
import org.springframework.stereotype.Component;

/**
 * fallback 回退类
 */
@Component
public class ProviderFeignFallback implements ProviderFeign {
    @Override
    public User findUserAll(Integer num) {
        return new User("系统故障", "123456");
    }
}
