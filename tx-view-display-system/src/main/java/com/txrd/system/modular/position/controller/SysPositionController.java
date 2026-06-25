
package com.txrd.system.modular.position.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.position.entity.SysPosition;
import com.txrd.system.modular.position.service.ISysPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 职位控制器
 *
 */
@Tag(name = "职位控制器")
@ApiSupport(order = 2)
@Validated
@RestController
@RequestMapping("/sys/position")
public class SysPositionController {

    @Autowired
    private ISysPositionService positionService;

    @ApiOperationSupport(order = 1)
    @Operation(summary = "获取职位列表")
    @GetMapping("/page")
    public CommonResult<IPage<SysPosition>> getPage(
            @Schema(description = "分页码") @RequestParam(defaultValue = "1") long current,
            @Schema(description = "每页数据") @RequestParam(defaultValue = "10") long size,
            @Schema(description = "搜索条件，name,orgId")SysPosition queryPosition) {
        IPage<SysPosition> page = new Page<>(current, size);
        return CommonResult.data(positionService.selectPositionPage(page, queryPosition));
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "保存")
    @PostMapping("/save")
    public CommonResult save(@RequestHeader("account") String currentAccount, @RequestBody SysPosition position) {
        return positionService.savePostion(position, currentAccount);
    }

    /**
     * 根据ID获取详情
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "根据ID获取详情")
    @GetMapping("/info/{id}")
    public CommonResult<SysPosition> getById(@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        return CommonResult.data(positionService.getById(id));
    }

    /**
     * 删除 (逻辑删除)
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "删除 (逻辑删除)")
    @DeleteMapping("/del/{id}")
    public CommonResult remove(@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        SysPosition position = new SysPosition();
        position.setId(id);
        position.setDeleteFlag(1); // 标记删除
        positionService.updateById(position);
        return CommonResult.ok();
    }
}
