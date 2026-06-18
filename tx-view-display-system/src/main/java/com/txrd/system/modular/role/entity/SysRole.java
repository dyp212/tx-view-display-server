
package com.txrd.system.modular.role.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.txrd.system.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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
