package com.txrd.system.modular.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txrd.system.modular.role.entity.SysRolePermission;
import com.txrd.system.modular.role.mapper.SysRolePermissionMapper;
import com.txrd.system.modular.role.param.AssignRolePermissionParam;
import com.txrd.system.modular.role.service.ISysRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {


    @Override
    @Transactional
    public void assignPermissions(AssignRolePermissionParam param) {
        // 1. 删除该角色原有的权限关联
        this.remove(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, param.getRoleId()));

        // 2. 批量插入新的关联
        if (!CollectionUtils.isEmpty(param.getPermissionIds())) {
            List<SysRolePermission> list = param.getPermissionIds().stream().map(pid -> {
                SysRolePermission rp = new SysRolePermission();
                rp.setRoleId(param.getRoleId());
                rp.setPermissionId(pid);
                return rp;
            }).collect(java.util.stream.Collectors.toList());

            this.saveBatch(list);
        }
    }

    @Override
    public List<Long> getPermissionIdsByRoleId(Long roleId) {
        List<SysRolePermission> list = this.list(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId));
        return list.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());
    }
}
