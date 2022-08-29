package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.component.JwtToken;
import com.nowcoder.community.component.UserContext;
import com.nowcoder.community.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private UserContext userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug(request.getRequestURI());
        // 从token中获取凭证
        LoginUser loginUser = jwtToken.getLoginUser(request);
        if (loginUser != null) {
            jwtToken.verifyToken(loginUser);
            // 在本次请求中持有用户
            userHolder.setUser(loginUser);
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
