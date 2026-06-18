package com.txrd.auth.constant;


public class CaptchaConstants {
    // Redis Key 前缀
    public static final String CAPTCHA_KEY_PREFIX = "auth:captcha:";
    // 验证码过期时间（秒）
    public static final long CAPTCHA_EXPIRE_TIME = 300L;
}
