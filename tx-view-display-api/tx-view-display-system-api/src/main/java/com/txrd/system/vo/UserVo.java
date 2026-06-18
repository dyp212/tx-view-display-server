
package com.txrd.system.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private Integer deleteFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String updateUser;

    /**
     * 用户角色
     */
    private List<RoleVo> roles;

    /**
     * 用户权限
     */
    private List<PermissionVo> permissions;
}
