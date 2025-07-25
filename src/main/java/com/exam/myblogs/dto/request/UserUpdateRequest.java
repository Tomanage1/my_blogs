package com.exam.myblogs.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "用户更新参数")
@Data
public class UserUpdateRequest {
    private Integer id;
    private String nickname;
    private String avatar;
}