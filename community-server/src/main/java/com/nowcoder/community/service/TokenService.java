package com.nowcoder.community.service;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.util.RedisKeyUtil;
import com.nowcoder.community.util.StringUtils;
import com.nowcoder.community.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TokenService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserMapper userMapper;

    private static final long EXPIRED_MINUTES = 30L;
    private static final long LONGER_EXPIRED_MINUTES = 24 * 60L;
    private static final long REFRESH_MILLISECONDS = TimeUnit.MINUTES.toMillis(30L);

    public String createToken(LoginUser loginUser) {
        String token = RedisKeyUtil.getLoginTokenKey(UUID.randomUUID().toString());
        // 将token存进redis里

        try {
            refreshToken(token, loginUser, EXPIRED_MINUTES);
        } catch (RedisConnectionFailureException e) {
            log.error("fail to save the token into redis");
            log.error(e.getMessage());
        }
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

    public LoginUser verifyTokenByDatabase(String token) {
        if (StringUtils.isNotBlank(token)) {

            String ticket = RedisKeyUtil.getTicketFromTokenKey(token).replaceAll("-", "");
            LoginTicket loginTicket = loginTicketMapper.selectByTicket(ticket);

            if (loginTicket != null && new Date().compareTo(loginTicket.getExpired()) < 0) {
                // 更新过期时间
                loginTicket.setExpired(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(LONGER_EXPIRED_MINUTES)));
                loginTicketMapper.updateTicket(loginTicket);
                User user = userMapper.selectById(loginTicket.getUserId());
                return createLoginUser(user);
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
            try {
                redisTemplate.delete(token);
            } catch (RedisConnectionFailureException e) {
                log.error("redis connection failed, {}", e.getMessage());
            }

        }
    }

    public LoginUser createLoginUser(User user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        loginUser.setUserId(user.getId());
        long currentTime = System.currentTimeMillis();
        loginUser.setLoginTime(currentTime);
        loginUser.setExpireTime(currentTime + REFRESH_MILLISECONDS);
        return loginUser;
    }
}
