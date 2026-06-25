
package com.txrd.system.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 用户实体
 *
 **/
@Data
public class UserVo {

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
     * 用户状态 (如: 0-正常, 1-禁用)
     */
    @Schema(description = "用户状态 (如: 0-正常, 1-禁用)")
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

    @Schema(description = "删除标志")
    private Integer deleteFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建操作人")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新操作人")
    private String updateUser;

    /**
     * 用户角色
     */
    @Schema(description = "用户角色列表")
    private List<RoleVo> roles;

    /**
     * 用户权限
     */
    @Schema(description = "用户权限列表")
    private List<PermissionVo> permissions;
}
