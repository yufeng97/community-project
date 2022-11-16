package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.service.TokenService;
import com.nowcoder.community.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserContext userContext;


    @Autowired
    private TokenService tokenService;


    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, Object handler) throws Exception {
        log.debug(request.getRequestURI());
        String token = request.getHeader("Authorization");

        LoginUser loginUser = null;
        try {
            loginUser = tokenService.verifyToken(token);
        } catch (RedisConnectionFailureException e) {
            loginUser = tokenService.verifyTokenByDatabase(token);
        }
        if (loginUser != null) {
            // 在本次请求中持有用户
            userContext.setUser(loginUser);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        userContext.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
