package com.txrd.auth.param;


import lombok.Data;

@Data
public class CaptchaRequest {
    private String key;   // 验证码唯一标识（前端生成或后端返回）
    private String code;  // 用户输入的验证码
}