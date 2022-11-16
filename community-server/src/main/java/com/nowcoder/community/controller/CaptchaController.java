package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.constant.Constants;
import com.nowcoder.community.service.CaptchaService;
import com.nowcoder.community.util.Base64;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/captcha")
    public CommonResult<Map<String, String>> getCaptchaImage(HttpServletResponse response) {
        Map<String, String> map = captchaService.createToken();
        if (map.containsKey("error")) {
            return CommonResult.fail(map.get("error"));
        }
        return CommonResult.success(map);
    }
}
