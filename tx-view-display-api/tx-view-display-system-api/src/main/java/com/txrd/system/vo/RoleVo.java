package com.txrd.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RoleVo {

    @Schema(description = "唯一标识")
    private Long id;

    /**
     * 所属组织ID
     */
    @Schema(description = "所属组织ID")
    private String orgId;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String name;

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
