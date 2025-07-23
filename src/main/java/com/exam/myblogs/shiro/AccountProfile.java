package com.exam.myblogs.shiro;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountProfile {
    private Integer id;
    private String username;
    private String nickname;
    private String avatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
