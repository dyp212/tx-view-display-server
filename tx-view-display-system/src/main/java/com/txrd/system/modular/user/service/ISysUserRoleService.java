package com.txrd.system.modular.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.system.modular.user.entity.SysUserRole;

import java.util.List;

public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void assignRolesToUser(Long userId, List<Long> roleIds);

    /**
     * 获取用户的角色ID列表
     */
    List<Long> getRoleIdsByUserId(Long userId);
}
