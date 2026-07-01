
package com.txrd.system.modular.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txrd.base.exception.CommonException;
import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.position.entity.SysPosition;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.role.mapper.SysRoleMapper;
import com.txrd.system.modular.role.service.ISysRoleService;
import com.txrd.system.modular.user.param.GetPageParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 角色Service接口实现类
 *
 **/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {


    @Override
    public IPage<SysRole> selectRolePage(GetPageParam param) {
        IPage<SysRole> page = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        // 名称模糊搜索
        wrapper.like(StringUtils.isNotBlank(param.getName()), SysRole::getName, param.getName());
        //
        wrapper.eq(null != param.getOrgId(), SysRole::getOrgId, param.getOrgId());
        // 默认过滤已删除数据
        wrapper.eq(SysRole::getDeleteFlag, "0");

        // 排序：先按排序码，再按创建时间
        wrapper.orderByAsc(SysRole::getSortCode)
                .orderByDesc(SysRole::getCreateTime);

        return this.page(page, wrapper);
    }

    @Override
    public CommonResult saveRole(SysRole sysRole, String currentAccount) {
        this.checkRole(sysRole, currentAccount);
        if(sysRole.getId() == null){
            LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>().eq(SysRole::getName, sysRole.getName());
            long count = super.count(queryWrapper);
            if (count > 0) {
                return CommonResult.error(I18nUtil.getMessage("role.name.exists"));
            }
        }
        super.saveOrUpdate(sysRole);
        return CommonResult.ok();
    }

    private void checkRole(SysRole role, String userAccount) {
        if(StringUtils.isBlank(role.getName())){
            throw new CommonException(I18nUtil.getMessage("role.name.null"));
        }
        if(role.getId() == null){
            role.setDeleteFlag(0);
            role.setCreateUser(userAccount);
            role.setCreateTime(LocalDateTime.now());
        }

        if(role.getDeleteFlag() == null){
            role.setDeleteFlag(0);
        }
        role.setUpdateUser(userAccount);
        role.setUpdateTime(LocalDateTime.now());
    }
}
