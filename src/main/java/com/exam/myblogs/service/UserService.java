package com.exam.myblogs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.myblogs.dto.request.LoginRequest;
import com.exam.myblogs.dto.request.RegisterRequest;
import com.exam.myblogs.dto.request.UserUpdateRequest;
import com.exam.myblogs.dto.response.UserResponse;
import com.exam.myblogs.entity.User;

import javax.servlet.http.HttpServletResponse;

//BaseService 是 IService 的默认实现类，基于 BaseMapper 实现数据操作
public interface UserService extends IService<User> {
    /**
     * 用户登录
     */
    UserResponse login(LoginRequest request, HttpServletResponse response);

    /**
     * 用户注册
     */
    UserResponse register(RegisterRequest request);

    /**
     * 更新用户信息
     */
    UserResponse updateUser(Integer userId, UserUpdateRequest request);

    /**
     * 根据ID获取用户信息
     */
    UserResponse getUserById(Integer id);
}
