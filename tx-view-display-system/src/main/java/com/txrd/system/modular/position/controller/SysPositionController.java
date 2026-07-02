
package com.txrd.system.modular.position.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.position.entity.SysPosition;
import com.txrd.system.modular.position.param.GetPageParam;
import com.txrd.system.modular.position.service.ISysPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    public CommonResult<IPage<SysPosition>> getPage(GetPageParam param){
        return CommonResult.data(positionService.selectPositionPage(param));
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "保存")
    @PostMapping("/save")
    public CommonResult save(@RequestHeader(value = "account", required = false) String currentAccount, @RequestBody SysPosition position) {
        return positionService.savePostion(position, currentAccount);
    }

    /**
     * 根据ID获取详情
     */
    @ApiOperationSupport(order = 3)
    @Operation(summary = "根据ID获取详情")
    @GetMapping("/info/{id}")
    public CommonResult<SysPosition> getById(@Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        return CommonResult.data(positionService.getInfoById(id));
    }

    /**
     * 删除 (逻辑删除)
     */
    @ApiOperationSupport(order = 5)
    @Operation(summary = "删除 (逻辑删除)")
    @DeleteMapping("/del/{id}")
    public CommonResult remove(@RequestHeader(value = "account", required = false) String userAccount, @Schema(description = "要操作数据ID")@PathVariable("id") Long id) {
        return positionService.delete(id, userAccount);
    }
}
