package com.txrd.auth.dto;

import com.txrd.common.vo.PermissionVo;
import com.txrd.common.vo.RoleVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginDto {

    @Schema(description = "登录TOKEN，登录成功后返回值放到请示头：Bearer ***")
    private String token;
    @Schema(description = "帐号ID")
    private Long userId;
    @Schema(description = "帐号")
    private String username;
    @Schema(description = "权限值列表")
    private List<String> permissions;
    @Schema(description = "角色对象列表")
    private List<RoleVo> roleVos;
    @Schema(description = "权限对象列表")
    private List<PermissionVo> permissionVos;
}
