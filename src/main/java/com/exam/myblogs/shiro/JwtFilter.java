package com.exam.myblogs.shiro;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.exam.myblogs.dto.response.Result;
import com.exam.myblogs.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 认证过滤器
 *
 * 用于 Shiro 框架中处理 JWT Token 的认证流程
 * - 提取请求头中的 Token
 * - 校验 Token 是否有效
 * - 处理登录流程和异常返回
 */
@Component
public class JwtFilter extends AuthenticatingFilter {
    @Autowired
    JwtUtils jwtUtils;

    /**
     * 创建认证 Token 对象
     *
     * @param servletRequest  HTTP 请求对象
     * @param servletResponse HTTP 响应对象
     * @return AuthenticationToken 认证 Token，如果 Token 不存在则返回 null
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 从请求头中获取 JWT Token（通常为 Authorization 字段）
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)) {
            return null;
        }
        // 创建自定义的 JwtToken 对象用于后续认证
        return new JwtToken(jwt);
    }

    /**
     * 访问被拒绝时的处理逻辑
     *
     * 用于判断是否需要进行登录认证
     *
     * @param servletRequest  HTTP 请求对象
     * @param servletResponse HTTP 响应对象
     * @return boolean 是否允许访问，true 表示允许，false 表示拒绝
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception{
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取头部Token
        String jwt = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        //放行登录和注册接口
        if (uri.equals("/myblog/user/login") || uri.equals("/myblog/user/register")) {
            return true;
        }

        //校验jwt
        Claims claims = jwtUtils.getClaimByToken(jwt);
        //校验是否为空和时间是否过期
        if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSONUtil.toJsonStr(Result.error(401, "token已失效,请重新登录", null)));
            return false;
        }
        // 执行登录认证流程
        return executeLogin(servletRequest, servletResponse);

    }
    /**
     * 登录失败时的异常处理
     *
     * 用于统一返回 JSON 格式的错误信息给前端
     *
     * @param token     认证 Token
     * @param e         认证异常
     * @param request   HTTP 请求对象
     * @param response  HTTP 响应对象
     * @return boolean 是否继续执行后续过滤器（通常返回 false）
     */
    //捕捉错误重写方法返回Result
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 获取异常原因，优先取 cause
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        // 构建错误响应对象
        Result result = Result.error(throwable.getMessage());
        //返回json
        String json = JSONUtil.toJsonStr(result);
        try {
            // 向客户端返回错误信息
            //打印JSON
            httpServletResponse.getWriter().print(json);
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }
}
