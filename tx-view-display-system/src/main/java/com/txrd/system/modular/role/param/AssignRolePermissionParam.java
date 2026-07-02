package com.txrd.system.modular.role.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Data
@Schema(description = "角色权限分配参数")
public class AssignRolePermissionParam {

    @Schema(description = "操作角色Id ")
    Long roleId;

    @Schema(description = "要授权的权限ID列表 ")
    List<Long> permissionIds;

}
