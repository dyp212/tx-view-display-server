
package com.txrd.system.modular.org.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.txrd.base.result.CommonResult;
import com.txrd.common.annotation.RequirePermission;
import com.txrd.system.modular.org.dto.OrgTreeDTO;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.org.param.GetPageParam;
import com.txrd.system.modular.org.service.ISysOrgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织控制器
 *
 */
@Tag(name = "组织控制器")
@ApiSupport(order = 1)
@Validated
@RestController
@RequestMapping("/sys/org")
public class SysOrgController {

    @Autowired
    private ISysOrgService sysOrgService;


    /**
     * 分页查询组织列表
     * GET /sys/org/page?current=1&size=10&name=技术部
     */
    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取组织列表")
    @GetMapping("/page")
//    @RequirePermission("org:page")
    public CommonResult<IPage<SysOrg>> getPage(GetPageParam param) {
        return CommonResult.data(sysOrgService.selectOrgPage(param));
    }

    /**
     * 获取组织树
     */
    @ApiOperationSupport(order = 2)
    @Operation(summary = "获取组织树")
    @GetMapping("/tree")
//    @RequirePermission("org:tree")
    public CommonResult<List<OrgTreeDTO>> tree() {
        return CommonResult.data(sysOrgService.getOrgTree());
    }

    /**
     * 根据ID获取详情
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "根据ID获取详情")
    @GetMapping("/info/{id}")
//    @RequirePermission("org:getbyId")
    public CommonResult<SysOrg> getById(@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        return CommonResult.data(sysOrgService.getById(id));
    }

    /**
     * 新增或修改
     */
    @ApiOperationSupport(order = 4)
    @Operation(summary = "新增或修改")
    @PostMapping("/save")
//    @RequirePermission("org:saveOrUpdate")
    public CommonResult save(@RequestHeader(value = "account", required = false) String currentAccount, @Valid @RequestBody SysOrg sysOrg) {
        return sysOrgService.saveOrg(sysOrg, currentAccount);
    }

    /**
     * 删除 (逻辑删除)
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "删除 (逻辑删除)")
    @DeleteMapping("/del/{id}")
//    @RequirePermission("org:del")
    public CommonResult remove(@RequestHeader(value = "account", required = false) String userAccount,@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        return sysOrgService.delete(id, userAccount);
    }
}
