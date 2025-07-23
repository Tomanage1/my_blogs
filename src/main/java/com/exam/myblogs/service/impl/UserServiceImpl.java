package com.exam.myblogs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.myblogs.dto.request.LoginRequest;
import com.exam.myblogs.dto.request.RegisterRequest;
import com.exam.myblogs.dto.request.UserUpdateRequest;
import com.exam.myblogs.dto.response.UserResponse;
import com.exam.myblogs.entity.User;
import com.exam.myblogs.mapper.UserMapper;
import com.exam.myblogs.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //登录
    @Override
    public UserResponse login(LoginRequest request) {
        // 查询用户
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, request.getUsername());

        // 密码加密处理
        String password = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        lambdaQueryWrapper.eq(User::getPassword, password);

        User user = baseMapper.selectOne(lambdaQueryWrapper);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        return convertToResponse(user);
    }

    //注册
    @Override
    public UserResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, request.getUsername());
        if (baseMapper.selectCount(lambdaQueryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        // 密码加密存储
        user.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        // 设置默认昵称和头像
        user.setNickname("新用户" + System.currentTimeMillis() % 10000);
        user.setAvatar("https://tse1-mm.cn.bing.net/th/id/OIP-C.mH9YLFEL5YdVxJM82mjVJQHaEo?w=292&h=182&c=7&r=0&o=7&dpr=1.4&pid=1.7&rm=3");

        baseMapper.insert(user);
        return convertToResponse(user);
    }

    //更新用户信息
    @Override
    public UserResponse updateUser(Integer userId, UserUpdateRequest request) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息
        user.setNickname(request.getNickname());
        user.setAvatar(request.getAvatar());

        baseMapper.updateById(user);
        return convertToResponse(user);
    }

    @Override
    public UserResponse getUserById(Integer id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            return null;
        }
        return convertToResponse(user);
    }

    /**
     * 转换实体类到响应DTO
     */
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
}
