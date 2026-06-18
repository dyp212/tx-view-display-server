
package com.txrd.system.vo;

import lombok.Data;

/**
 * <p>
 * 系统组织表
 * </p>
 *
 */
@Data
public class OrgVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 父组织ID
     */
    private Long parentId;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 排序码
     */
    private Integer sortCode;

    /**
     * 扩展信息(JSON)
     */
    private String extJson;
}
