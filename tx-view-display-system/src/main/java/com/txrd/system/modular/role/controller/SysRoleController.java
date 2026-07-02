
package com.txrd.system.modular.role.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.role.service.ISysRoleService;
import com.txrd.system.modular.user.param.GetPageParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 角色控制器
 *
 */
@Tag(name = "角色控制器")
@ApiSupport(order = 4)
@Validated
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {
    @Autowired
    private ISysRoleService roleService;

    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取角色列表")
    @GetMapping("/page")
    public CommonResult<IPage<SysRole>> getPage(GetPageParam param) {
        return CommonResult.data(roleService.selectRolePage(param));
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "保存")
    @PostMapping("/save")
    public CommonResult save(@RequestHeader(value = "account", required = false) String currentAccount, @RequestBody SysRole sysRole) {
        return roleService.saveRole(sysRole, currentAccount);
    }

    /**
     * 根据ID获取详情
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "根据ID获取详情")
    @GetMapping("/info/{id}")
    public CommonResult<SysRole> getById(@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        return CommonResult.data(roleService.getInfoById(id));
    }

    /**
     * 删除 (逻辑删除)
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "删除 (逻辑删除)")
    @DeleteMapping("/del/{id}")
    public CommonResult remove(@RequestHeader(value = "account", required = false) String userAccount,@Schema(description = "要操作数据ID") @PathVariable("id") Long id) {
        return roleService.delete(id, userAccount);
    }
}
