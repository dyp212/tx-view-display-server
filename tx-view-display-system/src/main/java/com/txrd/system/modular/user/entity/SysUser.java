
package com.txrd.system.modular.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.txrd.system.core.entity.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体
 *
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity { // 继承之前定义的 BaseEntity

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码 (建议加密存储)
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 员工编号
     */
    private String empNo;

    /**
     * 员工三方帐号
     */
    private String empAccount;

    /**
     * 机构ID
     */
    private Long orgId;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 省份编码
     */
    private String provCode;

    /**
     * 地市编码
     */
    private String cityCode;

    /**
     * 区县编码
     */
    private String countyCode;

    /**
     * 省份名称
     */
    private String provName;

    /**
     * 地市名称
     */
    private String cityName;

    /**
     * 区县名称
     */
    private String countyName;

    /**
     * 用户状态 (如: 0-正常, 1-禁用)
     */
    private Integer userStatus;

    /**
     * 排序码
     */
    private Integer sortCode;

    /**
     * 扩展信息 JSON
     */
    private String extJson;
}
