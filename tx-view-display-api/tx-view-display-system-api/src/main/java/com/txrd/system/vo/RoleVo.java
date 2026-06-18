package com.txrd.system.vo;

import lombok.Data;

@Data
public class RoleVo {

    private Long id;

    /**
     * 所属组织ID
     */
    private String orgId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 排序码
     */
    private Integer sortCode;

    /**
     * 扩展信息
     */
    private String extJson;
}
