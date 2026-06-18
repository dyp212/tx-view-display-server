
package com.txrd.auth.controller;

import com.txrd.auth.dto.CaptchaDto;
import com.txrd.auth.dto.LoginDto;
import com.txrd.auth.param.LoginRequest;
import com.txrd.auth.service.IAuthService;
import com.txrd.auth.service.ICaptchaService;
import com.txrd.base.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
public class AuthController {

    @Autowired
    private IAuthService authService;
    @Autowired
    private ICaptchaService captchaService;

    /**
     * 获取验证码
     */
    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public CommonResult<CaptchaDto> getCaptcha() {
        CaptchaDto captchaDto = captchaService.generateCaptcha();
        return CommonResult.data(captchaDto);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public CommonResult<LoginDto> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
