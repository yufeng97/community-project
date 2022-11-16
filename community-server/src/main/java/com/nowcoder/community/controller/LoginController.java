package com.nowcoder.community.controller;

import com.nowcoder.community.dto.LoginDto;
import com.nowcoder.community.exception.IllegalArgumentException;
import com.nowcoder.community.service.CaptchaService;
import com.nowcoder.community.service.LoginService;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/login")
    public CommonResult<String> login(@RequestBody LoginDto loginDto) {
        // verifyCaptcha
        String code = loginDto.getCode();
        String uuid = loginDto.getUuid();
        if (StringUtils.isBlank(code) || StringUtils.isBlank(uuid)) {
            throw new IllegalArgumentException();
        }

        captchaService.verifyCaptcha(code, uuid);
        // 生成令牌
        String token = loginService.login(loginDto);
        return CommonResult.success(token);
    }

    @PostMapping("/logout")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        boolean logout = loginService.logout(request);
        return CommonResult.success(logout);
    }

}
