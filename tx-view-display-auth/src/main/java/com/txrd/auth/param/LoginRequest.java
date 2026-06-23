package com.txrd.auth.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "{user.account.null}")
    private String username;

    @NotBlank(message = "{user.password.null}")
    private String password;

    @NotBlank(message = "{user.captcha.key.null}")
    private String captchaKey;

    @NotBlank(message = "{user.captcha.code.null}")
    private String captchaCode;


}
