
package com.txrd.system.modular.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.txrd.base.result.CommonResult;
import com.txrd.common.annotation.RequirePermission;
import com.txrd.system.modular.user.entity.SysUser;
import com.txrd.system.modular.user.param.GetPageParam;
import com.txrd.system.modular.user.service.ISysUserService;
import com.txrd.common.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 用户控制器
 *
 **/
@Tag(name = "用户控制器")
@ApiSupport(order = 6)
@Validated
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    /**
     * 分页查询用户列表
     * GET /sys/user/page?current=1&size=10&name=张
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取用户分页列表")
    @GetMapping("/page")
    public CommonResult<IPage<SysUser>> getPage(GetPageParam param) {
        return CommonResult.data(userService.selectUserPage(param));
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('user:save')")
    public CommonResult save(@RequestHeader(value = "account", required = false) String userAccount, @RequestBody SysUser user) {
        Authentication authorities = SecurityContextHolder.getContext().getAuthentication();
        return userService.saveUser(user, userAccount);
    }

    /**
     * 根据ID获取详情
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "根据ID获取详情")
    @GetMapping("/{id}")
    public CommonResult<SysUser> getById(@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        return CommonResult.data(userService.getById(id));
    }

    /**
     * 删除 (逻辑删除)
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "删除 (逻辑删除)")
    @DeleteMapping("/del/{id}")
    @RequirePermission("user:del")
    public CommonResult remove(@RequestHeader(value = "account", required = false) String userAccount, @Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setDeleteFlag(1); // 标记删除
        user.setUpdateUser(userAccount);
        user.setUpdateTime(LocalDateTime.now());
        userService.removeById(user);
        return CommonResult.ok();
    }

    @ApiOperationSupport(order = 6)
    @Operation(summary = "通过帐号获取用户")
    @GetMapping("/info/{account}")
    public UserVo getInfo(@Schema(description = "用户帐号")@PathVariable("account") String account) {
        return userService.getByAccount(account);
    }


}
