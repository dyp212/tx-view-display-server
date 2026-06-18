package com.txrd.auth.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.txrd.auth.constant.CaptchaConstants;
import com.txrd.auth.dto.CaptchaDto;
import com.txrd.auth.service.ICaptchaService;
import com.txrd.base.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CaptchaServiceImpl implements ICaptchaService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public CaptchaDto generateCaptcha() {
        // 1. 生成唯一 Key
        String key = UUID.randomUUID().toString().replace("-", "");

        // 2. 生成图形验证码 (宽100, 高40, 4位字符, 干扰线数量)
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 40, 4, 5);

        // 3. 将验证码文本存入 Redis，设置过期时间
        String code = captcha.getCode();
        String redisKey = CaptchaConstants.CAPTCHA_KEY_PREFIX + key;
        redisTemplate.opsForValue().set(redisKey, code, CaptchaConstants.CAPTCHA_EXPIRE_TIME, TimeUnit.SECONDS);

        log.debug("生成验证码: key={}, code={}", key, code);

        // 4. 转换为 Base64 返回前端
        String imageBase64 = captcha.getImageBase64Data();

        return new CaptchaDto(key, imageBase64);
    }

    @Override
    public CommonResult verifyCaptcha(String key, String code) {
        if (key == null || code == null) {
            return CommonResult.error("参数不能为空");
        }

        String redisKey = CaptchaConstants.CAPTCHA_KEY_PREFIX + key;
        String cachedCode = redisTemplate.opsForValue().get(redisKey);

        // 1. 验证码不存在或已过期
        if (cachedCode == null) {
            log.warn("验证码已过期或不存在: key={}", key);
            return CommonResult.error("验证码已过期或不存在");
        }

        // 2. 忽略大小写比对
        if (!cachedCode.equalsIgnoreCase(code)) {
            log.warn("验证码错误: key={}, input={}", key, code);
            return CommonResult.error("验证码错误");
        }

        // 3. 校验成功后，立即删除 Redis 中的验证码（一次性有效）
        redisTemplate.delete(redisKey);

        return CommonResult.ok();
    }
}
