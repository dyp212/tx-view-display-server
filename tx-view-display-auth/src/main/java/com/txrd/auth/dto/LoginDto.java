package com.txrd.auth.dto;

import com.txrd.system.vo.PermissionVo;
import com.txrd.system.vo.RoleVo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginDto {

    private String token;
    private Long userId;
    private String username;
    private List<String> permissions;
    private List<RoleVo> roleVos;
    private List<PermissionVo> permissionVos;
}
