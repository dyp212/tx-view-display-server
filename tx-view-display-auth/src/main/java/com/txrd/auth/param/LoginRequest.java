package com.txrd.auth.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(description = "登录帐号")
    @NotBlank(message = "{user.account.null}")
    private String username;

    @Schema(description = "密码")
    @NotBlank(message = "{user.password.null}")
    private String password;

    @Schema(description = "验证码KEY")
    @NotBlank(message = "{user.captcha.key.null}")
    private String captchaKey;

    @Schema(description = "验证码值")
    @NotBlank(message = "{user.captcha.code.null}")
    private String captchaCode;


}
