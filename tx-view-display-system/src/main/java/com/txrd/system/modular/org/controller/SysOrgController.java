
package com.txrd.system.modular.org.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.txrd.base.result.CommonResult;
import com.txrd.common.annotation.RequirePermission;
import com.txrd.system.modular.org.dto.OrgTreeDTO;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.org.service.ISysOrgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public CommonResult<IPage<SysOrg>> getPage(
            @Schema(description = "分页码") @RequestParam(defaultValue = "1") long current,
            @Schema(description = "每页数据") @RequestParam(defaultValue = "10") long size,
            @Schema(description = "搜索条件，name,parentId")SysOrg queryOrg) {
        IPage<SysOrg> page = new Page<>(current, size);
        return CommonResult.data(sysOrgService.selectOrgPage(page, queryOrg));
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
    public CommonResult save(@RequestHeader("account") String currentAccount, @Valid @RequestBody SysOrg sysOrg) {
        return sysOrgService.saveOrg(sysOrg, currentAccount);
    }

    /**
     * 删除 (逻辑删除)
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "删除 (逻辑删除)")
    @DeleteMapping("/del/{id}")
//    @RequirePermission("org:del")
    public CommonResult remove(@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        SysOrg org = new SysOrg();
        org.setId(id);
        org.setDeleteFlag(1); // 标记删除
        sysOrgService.updateById(org);
        return CommonResult.ok();
    }
}
