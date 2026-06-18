
package com.txrd.system.modular.permission.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txrd.base.exception.CommonException;
import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.system.modular.org.dto.OrgTreeDTO;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.permission.dto.SysPermissionDto;
import com.txrd.system.modular.permission.entity.SysPermission;
import com.txrd.system.modular.permission.mapper.SysPermissionMapper;
import com.txrd.system.modular.permission.service.ISysPermissionService;
import com.txrd.system.modular.position.entity.SysPosition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限Service接口实现类
 *
 **/
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public IPage<SysPermission> selectPermissionPage(IPage<SysPermission> page, SysPermission permission){
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();

        // 动态查询条件
        if (permission != null) {
            // 名称模糊搜索
            wrapper.like(StringUtils.isNotBlank(permission.getName()), SysPermission::getName, permission.getName());
            // 精确匹配父ID
            wrapper.eq(null != permission.getParentId(), SysPermission::getParentId, permission.getParentId());
        }

        // 默认过滤已删除数据
        wrapper.eq(SysPermission::getDeleteFlag, "0");

        // 排序：先按排序码，再按创建时间
        wrapper.orderByAsc(SysPermission::getSortCode)
                .orderByDesc(SysPermission::getCreateTime);

        return this.page(page, wrapper);
    }

    @Override
    public List<SysPermissionDto> getPermissionTree() {
        // 1. 查询所有未删除的组织，按排序码排序
        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPermission::getDeleteFlag, "0") // 假设0为未删除
                .orderByAsc(SysPermission::getSortCode);
        List<SysPermission> allPermissions = this.list(wrapper);

        if(allPermissions == null || allPermissions.size() == 0) {
            return Collections.emptyList();
        }
        List<SysPermissionDto> dtos = allPermissions.stream().map(item->objectMapper.convertValue(item, SysPermissionDto.class)).collect(Collectors.toList());
        //
        List<SysPermissionDto> rootNodes = dtos.stream()
                .filter(permission -> permission.getParentId() == null || "0".equals(permission.getParentId()))
                .collect(Collectors.toList());

        // 为每个根节点设置子节点
        for (SysPermissionDto root : rootNodes) {
            setChildren(root, dtos);
        }

        return rootNodes;
    }

    @Override
    public CommonResult savePermission(SysPermission permission, String currentAccount) {
        this.checkPermission(permission, currentAccount);
        if(permission.getId() == null){
            LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getName, permission.getName()).eq(SysPermission::getType, permission.getType());
            if(permission.getParentId() == null){
                queryWrapper.isNull(SysPermission::getParentId);
            } else {
                queryWrapper.eq(SysPermission::getParentId, permission.getParentId());
            }
            long count = super.count(queryWrapper);
            if (count > 0) {
                return CommonResult.error(I18nUtil.getMessage("permission.name.exists"));
            }
        }
        super.saveOrUpdate(permission);
        return CommonResult.ok();
    }

    private void setChildren(SysPermissionDto parent, List<SysPermissionDto> allPermissions) {
        List<SysPermissionDto> children = allPermissions.stream()
                .filter(permission -> parent.getId().equals(permission.getParentId()))
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
             parent.setChildren(children);
            for (SysPermissionDto child : children) {
                setChildren(child, allPermissions);
            }
        }
    }

    private void checkPermission(SysPermission permission, String userAccount) {
        if(StringUtils.isBlank(permission.getName())){
            throw new CommonException(I18nUtil.getMessage("permission.name.null"));
        }
        if(permission.getType() == null){
            throw new CommonException(I18nUtil.getMessage("permission.type.null"));
        }
        if(permission.getId() == null){
            permission.setDeleteFlag(0);
            permission.setCreateUser(userAccount);
            permission.setCreateTime(LocalDateTime.now());
        }

        if(permission.getDeleteFlag() == null){
            permission.setDeleteFlag(0);
        }
        permission.setUpdateUser(userAccount);
        permission.setUpdateTime(LocalDateTime.now());
    }
}
