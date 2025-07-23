package com.exam.myblogs.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Integer id;
    private String nickname;
    private String avatar;
}