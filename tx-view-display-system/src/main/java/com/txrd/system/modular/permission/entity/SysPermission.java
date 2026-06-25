package com.txrd.system.modular.permission.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.txrd.system.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_permission")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID (自增)
     */
    @Schema(description = "唯一标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父级权限ID
     */
    @Schema(description = "父级权限ID")
    private Long parentId;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    private String name;

    /**
     * 权限值 (如: user:add)
     */
    @Schema(description = "权限值 (如: user:add)")
    private String value;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 类型: 0目录 1菜单 2按钮
     */
    @Schema(description = "类型: 0目录 1菜单 2按钮")
    private Integer type;

    /**
     * 前端路由路径
     */
    @Schema(description = "前端路由路径")
    private String uri;

    /**
     * 排序码
     */
    @Schema(description = "排序码")
    private Integer sortCode;

    /**
     * 扩展信息
     */
    @Schema(description = "扩展信息")
    private String extJson;
}