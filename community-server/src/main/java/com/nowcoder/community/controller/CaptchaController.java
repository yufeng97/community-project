package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.constant.Constants;
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
    private Producer captchaProducer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/captcha")
    public CommonResult<Map<String, Object>> getCaptchaImage(HttpServletResponse response) {

        // generate captcha
        String text = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(text);
        log.info("text = " + text);

        // 验证码的归属
        String captchaOwner = CommunityUtil.generateSimpleUUID();
        String verifyKey = RedisKeyUtil.getCaptchaKey(captchaOwner);
        redisTemplate.opsForValue().set(verifyKey, text, 2, TimeUnit.MINUTES);

        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("生成验证码失败", e.getMessage());
            return CommonResult.fail(e.getMessage());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("img", Base64.encode(os.toByteArray()));
        map.put("uuid", captchaOwner);
//        map.put("text", text);
        return CommonResult.success(map);
    }
}
