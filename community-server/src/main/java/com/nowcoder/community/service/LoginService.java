package com.nowcoder.community.service;

import com.nowcoder.community.component.JwtToken;
import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.constant.Constants;
import com.nowcoder.community.dto.LoginDto;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.exception.IllegalArgumentException;
import com.nowcoder.community.exception.*;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.RedisKeyUtil;
import com.nowcoder.community.util.StringUtils;
import com.nowcoder.community.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private UserMapper userMapper;

    private static final int TICKET_VALID = 1;
    private static final int TICKET_INVALID = 0;

    private static final long EXPIRED_MINUTES = 30L;
    private static final long EXPIRED_SECONDS = TimeUnit.MINUTES.toMillis(EXPIRED_MINUTES);

    public String login(LoginDto loginDto) {
        log.info(String.valueOf(loginDto));
        validateCaptcha(loginDto.getCode(), loginDto.getUuid());

        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new IllegalArgumentException();
        }
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new InvalidCredentialsException();
        }
        // 验证状态
        if (user.getStatus() == 0) {
            //  "该账号未激活"
            throw new UserInvalidStateException();
        }

        // 验证密码
        String hash = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(hash)) {
            throw new InvalidCredentialsException();
        }

        // 生成登陆凭证
//        String token = jwtToken.createToken(user);
        LoginUser loginUser = tokenService.createLoginUser(user);
        String token = tokenService.createToken(loginUser);
        recordLoginTicket(token, user.getId());

        return token;
    }

    public boolean logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(token)) {
            tokenService.delLoginUser(token);
            return true;
        }
        return false;
    }

    private void validateCaptcha(String code, String captchaOwner) {
        log.info(code + " " + captchaOwner);
        String verifyKey = RedisKeyUtil.getCaptchaKey(captchaOwner);
        String captcha = (String) redisTemplate.opsForValue().get(verifyKey);
        redisTemplate.delete(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }

        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }

    private void recordLoginTicket(String token, Integer userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        loginTicket.setTicket(token);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + EXPIRED_SECONDS));
        loginTicket.setStatus(TICKET_VALID);
        String ticketKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(ticketKey, loginTicket);
    }
}
