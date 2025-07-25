package com.exam.myblogs.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "用户登录参数")
@Data
public class LoginRequest {

    private String username;
    private String password;

}
