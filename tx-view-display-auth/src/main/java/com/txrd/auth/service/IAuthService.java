package com.txrd.auth.service;

import com.txrd.auth.dto.LoginDto;
import com.txrd.auth.param.LoginRequest;
import com.txrd.base.result.CommonResult;

public interface IAuthService {
    /**
     * 用户登录
     */
    CommonResult<LoginDto> login(LoginRequest request);
}
