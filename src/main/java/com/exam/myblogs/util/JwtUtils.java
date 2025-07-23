package com.exam.myblogs.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 工具类
 *
 * 功能说明：
 * - 生成 JWT Token
 * - 解析 Token 获取用户信息
 * - 验证 Token 是否过期
 *
 * 配置项：
 * - tomanage.jwt.secret：签名密钥
 * - tomanage.jwt.expire：过期时间（秒）
 * - tomanage.jwt.header：Token 在请求头中的字段名（如 Authorization）
 */

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "tomanage.jwt")
public class JwtUtils {

    private String secret;
    private long expire;
    private String header;

    /**
     * 生成 JWT Token
     *
     * @param userId 用户ID（作为 Token 的主体内容）
     * @return 生成的 JWT Token 字符串
     */
    public String generateToken(long userId) {
        Date nowDate = new Date();

        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId + "")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 解析 Token 并获取 Claims（声明信息）
     *
     * @param token JWT Token 字符串
     * @return Claims 对象（包含 Token 中的用户信息），解析失败返回 null
     */
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            log.debug("validate is token error", e);
            return null;
        }
    }

    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
