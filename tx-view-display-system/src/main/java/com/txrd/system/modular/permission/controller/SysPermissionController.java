
package com.txrd.system.modular.permission.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.txrd.base.result.CommonResult;
import com.txrd.common.annotation.OperationLog;
import com.txrd.system.modular.permission.dto.SysPermissionDto;
import com.txrd.system.modular.permission.entity.SysPermission;
import com.txrd.system.modular.permission.service.ISysPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 *
 */
@Tag(name = "权限控制器")
@ApiSupport(order = 4)
@Validated
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {

    @Autowired
    private ISysPermissionService permissionService;

    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取权限列表")
    @GetMapping("/page")
    public CommonResult<IPage<SysPermission>> getPage(
            @Schema(description = "分页码") @RequestParam(defaultValue = "1") long current,
            @Schema(description = "每页数据") @RequestParam(defaultValue = "10") long size,
            @Schema(description = "搜索条件，name,parentId") SysPermission queryPermission) {
        IPage<SysPermission> page = new Page<>(current, size);
        return CommonResult.data(permissionService.selectPermissionPage(page, queryPermission));
    }


    /**
     * 获取权限树
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取权限树")
    @GetMapping("/tree")
    public CommonResult<List<SysPermissionDto>> tree() {
        return CommonResult.data(permissionService.getPermissionTree());
    }

    @ApiOperationSupport(order = 3)
    @Operation(summary = "保存")
    @PostMapping("/save")
    @OperationLog(module = "系统管理模块", type = "保存权限数据", saveResponseData = true)
    public CommonResult save(@RequestHeader("account")String currentAccount, @RequestBody SysPermission permission) {
        Authentication authorities = SecurityContextHolder.getContext().getAuthentication();
         return permissionService.savePermission(permission, currentAccount);
    }

    /**
     * 根据ID获取详情
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "根据ID获取详情")
    @GetMapping("/info/{id}")
    public CommonResult<SysPermission> getById(@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        return CommonResult.data(permissionService.getById(id));
    }


    /**
     * 删除 (逻辑删除)
     */
    @ApiOperationSupport(order = 6)
    @Operation(summary = "删除 (逻辑删除)")
    @DeleteMapping("/del/{id}")
    public CommonResult remove(@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        SysPermission permission = new SysPermission();
        permission.setId(id);
        permission.setDeleteFlag(1); // 标记删除
         permissionService.updateById(permission);
         return CommonResult.ok();
    }
}
