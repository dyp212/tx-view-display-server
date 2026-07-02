package com.txrd.system.modular.user.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.user.param.AssignUserRoleParam;
import com.txrd.system.modular.user.service.ISysUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户角色控制器")
@ApiSupport(order = 7)
@RestController
@RequestMapping("/sys/user/role")
public class SysUserRoleController {

    @Autowired
    private ISysUserRoleService userRoleService;

    /**
     * 获取角色的权限ID列表
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取用户的角色列表")
    @GetMapping("/list/{userId}")
    public CommonResult<List<SysRole>> getUserRoles(@Schema(description = "用户ID") @PathVariable("userId") Long userId) {
        return CommonResult.data(userRoleService.getUserRoles(userId));
    }

    /**
     * 分配权限
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "分配角色")
    @PostMapping("/assign")
    public CommonResult assign(@RequestBody AssignUserRoleParam param) {
        userRoleService.assignRolesToUser(param);
        return CommonResult.ok();
    }

}
