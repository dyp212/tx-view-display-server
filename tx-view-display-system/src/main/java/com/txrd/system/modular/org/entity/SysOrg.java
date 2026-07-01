
package com.txrd.system.modular.org.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.txrd.system.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 系统组织表
 * </p>
 *
 * @author generator
 * @since 2026-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_org")
public class SysOrg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "唯一标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父组织ID
     */
    @Schema(description = "父组织ID")
    private Long parentId;

    /**
     * 组织名称
     */
    @Schema(description = "组织名称")
    private String name;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String remark;

    /**
     * 排序码
     */
    @Schema(description = "排序码")
    private Integer sortCode;

    /**
     * 扩展信息(JSON)
     */
    @Schema(description = "扩展信息(JSON)")
    private String extJson;

    /**
     * 是否可用
     */
    @Schema(description = "是否可用")
    private Integer isEnabled;

}
