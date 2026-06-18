package com.txrd.system.vo;

import lombok.Data;

@Data
public class PermissionVo {

    private Long id;

    /**
     * 父级权限ID
     */
    private Long parentId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限值 (如: user:add)
     */
    private String value;

    /**
     * 图标
     */
    private String icon;

    /**
     * 类型: 0目录 1菜单 2按钮
     */
    private Integer type;

    /**
     * 前端路由路径
     */
    private String uri;

    /**
     * 排序码
     */
    private Integer sortCode;

    /**
     * 扩展信息
     */
    private String extJson;
}
