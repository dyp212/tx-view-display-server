package com.txrd.auth.service;

import com.txrd.auth.dto.CaptchaDto;
import com.txrd.base.result.CommonResult;

public interface ICaptchaService {

    /**
     * 生成验证码
     * @return 包含 key 和 base64 图片的 VO
     */
    CaptchaDto generateCaptcha();

    /**
     * 校验验证码
     * @param key 唯一标识
     * @param code 用户输入
     * @return 是否通过
     */
    CommonResult verifyCaptcha(String key, String code);
}
