package com.exam.myblogs.controller;


import com.exam.myblogs.dto.request.LoginRequest;
import com.exam.myblogs.dto.request.RegisterRequest;
import com.exam.myblogs.dto.request.UserUpdateRequest;
import com.exam.myblogs.dto.response.Result;
import com.exam.myblogs.dto.response.UserResponse;
import com.exam.myblogs.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(value = "用户Controller", tags = {"用户接口"})
@RestController
@RequestMapping("/myblog/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @ApiOperation(value = "用户登录", tags = {"用户接口"})
    @PostMapping("/login")
    public Result<UserResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        UserResponse user = userService.login(request, response);

        return Result.success(user);
    }

    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册", tags = {"用户接口"})
    @PostMapping("/register")
    public Result<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @ApiOperation(value = "用户更新", tags = {"用户接口"})
    @PutMapping
    public Result<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {
        UserResponse user = userService.updateUser(request);
        return Result.success(user);
    }

    /**
     * 根据ID获取用户信息
     */
    @ApiOperation(value = "用户信息查询", tags = {"用户接口"})
    @GetMapping("/{id}")
    public Result<UserResponse> getUserById(@PathVariable Integer id) {
        UserResponse user = userService.getUserById(id);
        return Result.success(user);
    }
}
