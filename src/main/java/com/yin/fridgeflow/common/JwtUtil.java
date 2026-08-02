package com.yin.fridgeflow.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类：签发 / 解析 token。
 * <p>HS256 对称签名，secret 与过期时间读自 application.yml 的 {@code jwt.secret} / {@code jwt.expire-hours}。
 * 当前阶段仅用于登录签发 token，未做拦截器校验；后续接入鉴权时调用 {@link #parseUserId(String)} 解析用户 ID。</p>
 *
 * @author yin
 */
@Component
public class JwtUtil {

    /** 签名密钥（读自配置） */
    @Value("${jwt.secret}")
    private String secret;

    /** token 有效期（小时，读自配置） */
    @Value("${jwt.expire-hours}")
    private long expireHours;

    /**
     * 签发 token，subject 为用户 ID。
     *
     * @param userId 用户 ID，作为 token 的 subject
     * @return 签名后的 JWT 字符串
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        // 有效期换算为毫秒
        long expireMs = expireHours * 3600L * 1000L;
        return Jwts.builder()
                .subject(String.valueOf(userId))          // subject = 用户ID
                .issuedAt(now)                            // 签发时间
                .expiration(new Date(now.getTime() + expireMs))  // 过期时间
                .signWith(getKey())                       // HS256 签名
                .compact();
    }

    /**
     * 解析 token，返回用户 ID。
     *
     * @param token JWT 字符串
     * @return subject 中存储的用户 ID
     * @throws io.jsonwebtoken.JwtException token 格式非法、签名不符或已过期时抛出
     */
    public Long parseUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }

    /**
     * 由密钥字符串构造 HS256 SecretKey（要求字节数 ≥32）。
     */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
