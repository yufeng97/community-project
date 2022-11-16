package com.nowcoder.community.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.code.kaptcha.Producer;
import com.nowcoder.community.exception.CaptchaException;
import com.nowcoder.community.exception.CaptchaExpireException;
import com.nowcoder.community.util.Base64;
import com.nowcoder.community.util.CommonResult;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CaptchaService {

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Cache<String, String> caffeineCache;

    public Map<String, String> createToken() {
        // generate captcha
        String text = captchaProducer.createText();
        BufferedImage image = captchaProducer.createImage(text);
        log.info("text = " + text);

        // 验证码的归属
        String captchaOwner = CommunityUtil.generateSimpleUUID();
        // save captcha into redis
        String verifyKey = RedisKeyUtil.getCaptchaKey(captchaOwner);

        try {
            redisTemplate.opsForValue().set(verifyKey, text, 2, TimeUnit.MINUTES);
        } catch (RedisConnectionFailureException e) {
            System.out.println(e.getMessage());
            log.debug("save into local cache key={}, value={}", verifyKey, text);
            caffeineCache.put(verifyKey, text);
        }

        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        Map<String, String> map = new HashMap<>();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("生成验证码失败, {}", e.getMessage());
            map.put("error", e.getMessage());
            return map;
        }

        map.put("img", Base64.encode(os.toByteArray()));
        map.put("uuid", captchaOwner);
        return map;
    }

    public boolean verifyCaptcha(String code, String captchaOwner) {
        log.info(code + " " + captchaOwner);
        String verifyKey = RedisKeyUtil.getCaptchaKey(captchaOwner);
        String captcha;
        try {
            captcha = (String) redisTemplate.opsForValue().get(verifyKey);
            redisTemplate.delete(verifyKey);
        } catch (RedisConnectionFailureException e) {
            captcha = caffeineCache.getIfPresent(verifyKey);
            caffeineCache.invalidate(verifyKey);
        }
        if (captcha == null) {
            throw new CaptchaExpireException();
        }

        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
        return false;
    }
}
