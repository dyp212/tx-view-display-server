package com.txrd.system.modular.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.user.entity.SysUserRole;
import com.txrd.system.modular.user.mapper.SysUserRoleMapper;
import com.txrd.system.modular.user.param.AssignUserRoleParam;
import com.txrd.system.modular.user.service.ISysUserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    @Transactional
    public void assignRolesToUser(AssignUserRoleParam param) {
        // 1. 删除该用户原有的所有角色关联
        this.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, param.getUserId()));

        // 2. 批量插入新的角色关联
        if (!CollectionUtils.isEmpty(param.getRoleIds())) {
            List<SysUserRole> userRoles = param.getRoleIds().stream().map(roleId -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(param.getUserId());
                ur.setRoleId(roleId);
                return ur;
            }).collect(Collectors.toList());

            this.saveBatch(userRoles);
        }
    }

    @Override
    public List<SysRole> getUserRoles(Long userId) {
//        List<SysUserRole> list = this.list(new LambdaQueryWrapper<SysUserRole>()
//                .eq(SysUserRole::getUserId, userId));
//        return list.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        return sysUserRoleMapper.getUserRoles(userId);
    }
}
