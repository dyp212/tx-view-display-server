package com.txrd.system.modular.role.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.role.service.ISysRolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色权限控制器")
@ApiSupport(order = 5)
@RestController
@RequestMapping("/sys/role/permission")
public class SysRolePermissionController {

    @Autowired
    private ISysRolePermissionService rolePermissionService;

    /**
     * 获取角色的权限ID列表
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取角色的权限ID列表")
    @GetMapping("/list/{roleId}")
    public CommonResult<List<Long>> getPermissionIds(@Schema(description = "操作角色Id ") @PathVariable("roleId") Long roleId) {
        return CommonResult.data(rolePermissionService.getPermissionIdsByRoleId(roleId));
    }

    /**
     * 分配权限
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分配权限")
    @PostMapping("/assign")
    public CommonResult assign(@Schema(description = "操作角色Id ") @RequestParam Long roleId, @Schema(description = "要授权的权限ID列表 ") @RequestBody List<Long> permissionIds) {
        rolePermissionService.assignPermissions(roleId, permissionIds);
        return CommonResult.ok();
    }

}
