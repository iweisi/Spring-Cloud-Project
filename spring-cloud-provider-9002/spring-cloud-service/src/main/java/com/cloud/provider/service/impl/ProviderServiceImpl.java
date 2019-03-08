package com.cloud.provider.service.impl;

import com.cloud.api.entity.User;
import com.cloud.provider.service.ProviderService;
import org.springframework.stereotype.Service;

/**
 * @Author: 薛日
 * @Date: 2019/3/4 16:55
 */
@Service
public class ProviderServiceImpl implements ProviderService {
    @Override
    public User findUserAll() {
        return new User("查询订单服务", "单号15316161");
    }
}
