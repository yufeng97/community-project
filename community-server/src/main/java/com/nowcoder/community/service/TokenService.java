package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.RedisKeyUtil;
import com.nowcoder.community.util.StringUtils;
import com.nowcoder.community.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TokenService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final long EXPIRED_MINUTES = 30L;
    private static final long LONGER_EXPIRED_MINUTES = 24 * 60L;
    private static final long REFRESH_MILLISECONDS = TimeUnit.MINUTES.toMillis(30L);

    public String createToken(LoginUser loginUser) {
        String token = RedisKeyUtil.getLoginTokenKey(UUID.randomUUID().toString());
        // 将token存进redis里
        refreshToken(token, loginUser, EXPIRED_MINUTES);
        return token;
    }

    public LoginUser verifyToken(String token) {
        if (StringUtils.isNotBlank(token)) {
            LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(token);
            if (loginUser != null) {
                Long expireTime = loginUser.getExpireTime();
                if (expireTime - System.currentTimeMillis() <= REFRESH_MILLISECONDS) {
                    refreshToken(token, loginUser, LONGER_EXPIRED_MINUTES);
                }
                return loginUser;
            }
        }
        return null;
    }

    /**
     * 更新过期时间
     * @param token
     * @param loginUser
     * @param expiredMinutes
     */
    public void refreshToken(String token, LoginUser loginUser, long expiredMinutes) {
        log.debug(loginUser.toString() + " " + expiredMinutes + " minutes");
        loginUser.setExpireTime(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expiredMinutes));
        redisTemplate.opsForValue().set(token, loginUser, Duration.ofMinutes(expiredMinutes));
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotBlank(token)) {
            redisTemplate.delete(token);
        }
    }

    public LoginUser createLoginUser(User user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        loginUser.setUserId(user.getId());
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(System.currentTimeMillis() + REFRESH_MILLISECONDS);
        return loginUser;
    }
}
