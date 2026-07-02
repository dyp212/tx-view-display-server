
package com.txrd.system.modular.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.txrd.system.core.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "唯一标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String account;

    /**
     * 密码 (建议加密存储)
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String name;

    /**
     * 手机
     */
    @Schema(description = "手机")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 员工编号
     */
    @Schema(description = "员工编号")
    private String empNo;

    /**
     * 员工三方帐号
     */
    @Schema(description = "员工三方帐号")
    private String empAccount;

    /**
     * 机构ID
     */
    @Schema(description = "机构ID")
    private Long orgId;

    /**
     * 职位ID
     */
    @Schema(description = "职位ID")
    private Long positionId;

    /**
     * 省份编码
     */
    @Schema(description = "省份编码")
    private String provCode;

    /**
     * 地市编码
     */
    @Schema(description = "地市编码")
    private String cityCode;

    /**
     * 区县编码
     */
    @Schema(description = "区县编码")
    private String countyCode;

    /**
     * 省份名称
     */
    @Schema(description = "省份名称")
    private String provName;

    /**
     * 地市名称
     */
    @Schema(description = "地市名称")
    private String cityName;

    /**
     * 区县名称
     */
    @Schema(description = "区县名称")
    private String countyName;

    /**
     * 用户状态 (如: 1-正常, 0-禁用)
     */
    @Schema(description = "用户状态 (如: 1-正常, 0-禁用)")
    private Integer userStatus;

    /**
     * 排序码
     */
    @Schema(description = "排序码")
    private Integer sortCode;

    /**
     * 扩展信息 JSON
     */
    @Schema(description = "扩展信息 JSON")
    private String extJson;

    /**
     * 所属组织
     */
    @Schema(description = "所属组织")
    @TableField(exist = false)
    private String orgName;

    /**
     * 所属职位
     */
    @Schema(description = "所属职位")
    @TableField(exist = false)
    private String positionName;
}
