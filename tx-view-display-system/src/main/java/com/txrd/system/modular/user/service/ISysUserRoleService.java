package com.txrd.system.modular.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.user.entity.SysUserRole;
import com.txrd.system.modular.user.param.AssignUserRoleParam;

import java.util.List;

public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 为用户分配角色
     */
    void assignRolesToUser(AssignUserRoleParam param);

    /**
     * 获取用户的角色列表
     */
    List<SysRole> getUserRoles(Long userId);
}
