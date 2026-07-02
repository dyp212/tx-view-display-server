
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
import com.txrd.system.modular.permission.param.GetPageParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限控制器
 *
 */
@Tag(name = "权限控制器")
@ApiSupport(order = 3)
@Validated
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {

    @Autowired
    private ISysPermissionService permissionService;

    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取权限列表")
    @GetMapping("/page")
    public CommonResult<IPage<SysPermission>> getPage(GetPageParam param) {
        return CommonResult.data(permissionService.selectPermissionPage(param));
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
    public CommonResult save(@RequestHeader(value = "account", required = false)String currentAccount, @RequestBody SysPermission permission) {
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
    public CommonResult remove(@RequestHeader(value = "account", required = false) String userAccount, @Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
         return permissionService.delete(id, userAccount);
    }
}
