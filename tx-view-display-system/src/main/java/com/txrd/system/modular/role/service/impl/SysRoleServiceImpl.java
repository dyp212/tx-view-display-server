
package com.txrd.system.modular.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txrd.base.exception.CommonException;
import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.org.mapper.SysOrgMapper;
import com.txrd.system.modular.position.entity.SysPosition;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.role.mapper.SysRoleMapper;
import com.txrd.system.modular.role.service.ISysRoleService;
import com.txrd.system.modular.user.entity.SysUserRole;
import com.txrd.system.modular.user.mapper.SysUserRoleMapper;
import com.txrd.system.modular.user.param.GetPageParam;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色Service接口实现类
 *
 **/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysOrgMapper sysOrgMapper;

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

        IPage<SysRole> result = this.page(page, wrapper);
        if(!CollectionUtils.isEmpty(result.getRecords())){
            List<Long> orgIds = result.getRecords().stream().map(SysRole::getOrgId).filter(Objects::nonNull).toList();
            if(!CollectionUtils.isEmpty(orgIds)){
                List<SysOrg> sysOrgs = sysOrgMapper.selectBatchIds(orgIds);
                if(!CollectionUtils.isEmpty(sysOrgs)){
                    Map<Long, String> orgMap = sysOrgs.stream().collect(Collectors.toMap(SysOrg::getId, SysOrg::getName));
                    result.getRecords().forEach(item -> {
                        item.setOrgName(orgMap.get(item.getOrgId()));
                    });
                }
            }
        }
        return result;
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
            if (super.count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, sysRole.getCode())) > 0) {
                return CommonResult.error(I18nUtil.getMessage("role.code.exists"));
            }
        }
        super.saveOrUpdate(sysRole);
        return CommonResult.data(sysRole);
    }

    @Override
    public CommonResult delete(Long roleId, String userAccount) {
        Long count = sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId));
        if(count > 0){
            return CommonResult.error(I18nUtil.getMessage("role.user.used"));
        }
        SysRole role = new SysRole();
        role.setId(roleId);
        role.setDeleteFlag(1); // 标记删除
        role.setUpdateUser(userAccount);
        role.setUpdateTime(LocalDateTime.now());
        super.removeById(role);
        return CommonResult.ok();
    }

    @Override
    public SysRole getInfoById(Long roleId) {
        SysRole role = super.getById(roleId);
        if(role != null && role.getOrgId() != null){
            SysOrg sysOrg = sysOrgMapper.selectById(role.getOrgId());
            role.setOrgName(sysOrg.getName());
        }
        return role;
    }

    private void checkRole(SysRole role, String userAccount) {
        if(StringUtils.isBlank(role.getName())){
            throw new CommonException(I18nUtil.getMessage("role.name.null"));
        }
        if(StringUtils.isBlank(role.getCode())){
            throw new CommonException(I18nUtil.getMessage("role.code.null"));
        }
        if(role.getId() == null){
            role.setDeleteFlag(0);
            role.setIsEnabled(0);
            role.setCreateUser(userAccount);
            role.setCreateTime(LocalDateTime.now());
        }

        if(role.getDeleteFlag() == null){
            role.setDeleteFlag(0);
        }
        if(role.getIsEnabled() == null){
            role.setIsEnabled(0);
        }
        role.setUpdateUser(userAccount);
        role.setUpdateTime(LocalDateTime.now());
    }
}
