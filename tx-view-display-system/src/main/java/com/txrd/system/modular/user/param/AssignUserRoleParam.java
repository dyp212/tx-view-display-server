package com.txrd.system.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户角色分配参数")
public class AssignUserRoleParam {

    @Schema(description = "用户ID ")
    Long userId;

    @Schema(description = "用户授权角色列表 ")
    List<Long> roleIds;

}
