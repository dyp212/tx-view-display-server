package com.txrd.auth.service.impl;

import com.txrd.auth.config.JwtTokenGenerator;
import com.txrd.auth.dto.LoginDto;
import com.txrd.auth.param.LoginRequest;
import com.txrd.auth.service.IAuthService;
import com.txrd.auth.service.ICaptchaService;
import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.system.api.IUserClient;
import com.txrd.common.vo.PermissionVo;
import com.txrd.common.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private IUserClient userClient;
    @Autowired
    private ICaptchaService captchaService;
    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Override
    public CommonResult<LoginDto> login(LoginRequest request) {
        CommonResult rst = captchaService.verifyCaptcha(request.getCaptchaKey(), request.getCaptchaCode());
        if(rst.getCode() != CommonResult.CODE_SUCCESS){
            return rst;
        }
        // 1. 查询用户
        UserVo user = userClient.getInfo(request.getUsername());

        // 2. 验证用户是否存在
        if (user == null) {
            return CommonResult.error(I18nUtil.getMessage("user.login.fail"));
        }

        // 3. 验证账号状态
        if (user.getUserStatus() == 1) {
            return CommonResult.error(I18nUtil.getMessage("user.account.disabled"));
        }

        // 4. 验证密码 (BCrypt匹配)
        // 注意：如果是旧系统明文密码，需先判断格式。这里假设数据库存的是 BCrypt 哈希值
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return CommonResult.error(I18nUtil.getMessage("user.login.fail"));
        }
        List<String> permissions = user.getPermissions().stream().map(PermissionVo::getValue).collect(Collectors.toList());
        /**
        // 5. 构建 JWT Claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getAccount());
        claims.put("role", user.getRoles().stream().map(RoleVo::getName).collect(Collectors.toList())); // 实际应从 sys_user_role 关联查询
        claims.put("permissions", permissions); // 实际应从角色关联查询权限

        // 6. 生成 Token
        String token = JwtUtil.generateToken(user.getId()+"", claims);
         **/
        String token = jwtTokenGenerator.generateToken(user);
        if(StringUtils.isBlank(token)){
            return CommonResult.error(I18nUtil.getMessage("user.token.fail"));
        }

        // 7. 返回结果
        return CommonResult.data(LoginDto.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getAccount())
                .permissions(permissions)
                .roleVos(user.getRoles())
                .permissionVos(user.getPermissions())
                .build());
    }
}
