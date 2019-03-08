package com.cloud.api.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: 薛日
 * @Date: 2019/3/4 16:42
 */
@Data
@AllArgsConstructor
public class User {
    private String username;
    private String password;
}
