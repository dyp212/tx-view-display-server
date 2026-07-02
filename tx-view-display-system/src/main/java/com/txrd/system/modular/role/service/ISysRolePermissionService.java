package com.txrd.system.modular.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.system.modular.role.entity.SysRolePermission;
import com.txrd.system.modular.role.param.AssignRolePermissionParam;

import java.util.List;

public interface ISysRolePermissionService extends IService<SysRolePermission> {

    /**
     * 为角色分配权限
     */
    void assignPermissions(AssignRolePermissionParam param);

    /**
     * 获取角色的权限ID列表
     */
    List<Long> getPermissionIdsByRoleId(Long roleId);
}
