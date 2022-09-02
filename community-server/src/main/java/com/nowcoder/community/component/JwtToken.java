package com.nowcoder.community.component;

import com.nowcoder.community.constant.Constants;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.exception.InvalidTokenException;
import com.nowcoder.community.exception.TokenExpireException;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.StringUtils;
import com.nowcoder.community.vo.LoginUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtToken {

    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    private static final long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;
    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;
    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;
    // 更长的令牌有效期
    @Value("${token.longerExpireTime}")
    private int longerExpireTime;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建令牌
     *
     * @param user 用户信息
     * @return 令牌
     */
    public String createToken(User user) {
        String token = CommunityUtil.generateUUID();
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        loginUser.setToken(token);
        loginUser.setUserId(user.getId());

        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime * MILLIS_MINUTE))
                .compact();
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser, boolean isNew) {
        loginUser.setLoginTime(System.currentTimeMillis());
        long expireTimeMillis = isNew? loginUser.getLoginTime() + expireTime * MILLIS_MINUTE : loginUser.getLoginTime() + longerExpireTime * MILLIS_MINUTE;
        loginUser.setExpireTime(expireTimeMillis);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisTemplate.opsForValue().set(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    public void refreshToken(LoginUser loginUser) {
        refreshToken(loginUser, true);
    }

    /**
     * 从request获取用户身份信息
     *
     * @param request Request
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
                String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                String userKey = getTokenKey(uuid);
                return (LoginUser) redisTemplate.opsForValue().get(userKey);
            } catch (ExpiredJwtException e) {
                log.error("JWT过期：", e);
                throw new TokenExpireException();
            } catch (UnsupportedJwtException e) {
                log.error("不支持的JWT：", e);
                throw new InvalidTokenException();
            } catch (MalformedJwtException e) {
                log.error("JWT格式错误：", e);
                throw new InvalidTokenException();
            } catch (SignatureException e) {
                log.error("签名异常：", e);
                throw new InvalidTokenException();
            } catch (IllegalArgumentException e) {
                log.error("非法请求：", e);
                throw new InvalidTokenException();
            } catch (Exception e) {
                log.error("解析异常：", e);
                throw new RuntimeException("解析异常");
            }
        }
        return null;
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisTemplate.delete(userKey);
        }
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Decoders.BASE64.decode(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser, false);
        }
    }

    private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }


}
