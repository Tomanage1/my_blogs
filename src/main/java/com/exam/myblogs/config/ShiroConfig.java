package com.exam.myblogs.config;

import com.exam.myblogs.shiro.AccountRealm;
import com.exam.myblogs.shiro.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置类
 *
 * 用于集成 Apache Shiro 框架，实现基于 JWT 的无状态认证
 * 主要配置内容包括：
 * - Session 管理（使用 Redis）
 * - SecurityManager 配置
 * - JWT 过滤器注册
 * - URL 过滤规则定义
 */
//Shiro启用注解拦截器
@Configuration
public class ShiroConfig {
    @Autowired
    private JwtFilter jwtFilter;

    /**
     * 配置 Session 管理器
     *
     * 使用 Redis 作为 Session 存储，实现分布式 Session 共享
     *
     * @param redisSessionDAO RedisSessionDAO Bean
     * @return SessionManager 实例
     */
    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    /**
     * 配置 SecurityManager
     *
     * SecurityManager 是 Shiro 的核心安全管理器
     *
     * @param accountRealm 自定义的 Realm，用于处理认证和授权逻辑
     * @param sessionManager Session 管理器
     * @param redisCacheManager Redis 缓存管理器
     * @return SecurityManager 实例
     */
    @Bean
    public DefaultWebSecurityManager securityManager(AccountRealm accountRealm,
                                                     SessionManager sessionManager,
                                                     RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);
        securityManager.setSessionManager(sessionManager); // 设置 Session 管理器
        securityManager.setCacheManager(redisCacheManager);// 设置缓存管理器（使用 Redis）

// 禁用 Shiro 默认的 Session 功能（因为我们使用 JWT 无状态认证）
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false); // 关闭 Session 存储
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * 配置 Shiro 过滤器链定义
     *
     * 定义 URL 路径与过滤器的映射关系
     *
     * @return ShiroFilterChainDefinition 实例
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 所有请求都经过 jwt 过滤器
        filterMap.put("/**","jwt");

        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }

    /**
     * 配置 Shiro Filter Factory Bean
     *
     * 用于创建 Shiro Filter 并注册自定义的 JWT 过滤器
     *
     * @param securityManager Shiro 安全管理器
     * @param shiroFilterChainDefinition 过滤器链定义
     * @return ShiroFilterFactoryBean 实例
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);
        shiroFilter.setFilters(filters);

        // 设置 URL 过滤规则
        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();

        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }
}
