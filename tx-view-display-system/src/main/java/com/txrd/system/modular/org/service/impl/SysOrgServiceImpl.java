
package com.txrd.system.modular.org.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txrd.base.exception.CommonException;
import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.system.modular.org.dto.OrgTreeDTO;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.org.mapper.SysOrgMapper;
import com.txrd.system.modular.org.param.GetPageParam;
import com.txrd.system.modular.org.service.ISysOrgService;
import com.txrd.system.modular.position.entity.SysPosition;
import com.txrd.system.modular.position.mapper.SysPositionMapper;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.role.entity.SysRolePermission;
import com.txrd.system.modular.role.mapper.SysRoleMapper;
import com.txrd.system.modular.user.entity.SysUser;
import com.txrd.system.modular.user.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织Service接口实现类
 *
 **/
@Service
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrg> implements ISysOrgService {

    @Autowired
    private ObjectMapper objectMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysPositionMapper sysPositionMapper;


    @Override
    public IPage<SysOrg> selectOrgPage(GetPageParam param){
        IPage<SysOrg> page = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<SysOrg> wrapper = new LambdaQueryWrapper<>();
        // 名称模糊搜索
        wrapper.like(StringUtils.isNotBlank(param.getName()), SysOrg::getName, param.getName());
        // 精确匹配父ID（可选，用于查看某组织下的子组织分页）
        wrapper.eq(null != param.getParentId(), SysOrg::getParentId, param.getParentId());
        // 默认过滤已删除数据
        wrapper.eq(SysOrg::getDeleteFlag, "0");

        // 排序：先按排序码，再按创建时间
        wrapper.orderByAsc(SysOrg::getSortCode)
                .orderByDesc(SysOrg::getCreateTime);

        return this.page(page, wrapper);
    }

    @Override
    public List<OrgTreeDTO> getOrgTree() {
        // 1. 查询所有未删除的组织，按排序码排序
        LambdaQueryWrapper<SysOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysOrg::getDeleteFlag, "0") // 假设0为未删除
                .orderByAsc(SysOrg::getSortCode);
        List<SysOrg> allOrgs = this.list(wrapper);

        if(allOrgs == null || allOrgs.size() == 0) {
            return Collections.emptyList();
        }
        List<OrgTreeDTO> dtos = allOrgs.stream().map(item->objectMapper.convertValue(item, OrgTreeDTO.class)).collect(Collectors.toList());
        // 2. 构建树形结构 (简单内存构建，数据量大建议递归查询或SQL处理)
        // 找出所有根节点 (parentId 为 null 或 "0")
        List<OrgTreeDTO> rootNodes = dtos.stream()
                .filter(org -> org.getParentId() == null || "0".equals(org.getParentId()))
                .collect(Collectors.toList());

        // 为每个根节点设置子节点
        for (OrgTreeDTO root : rootNodes) {
            setChildren(root, dtos);
        }

        return rootNodes;
    }

    @Override
    @Transactional(rollbackFor = CommonException.class)
    public CommonResult saveOrg(SysOrg sysOrg, String currentAccount) {
        this.checkOrg(sysOrg, currentAccount);
        if(sysOrg.getId() == null){
            LambdaQueryWrapper<SysOrg> queryWrapper = new LambdaQueryWrapper<SysOrg>().eq(SysOrg::getName, sysOrg.getName());
            if(sysOrg.getParentId() == null){
                queryWrapper.isNull(SysOrg::getParentId);
            } else {
                queryWrapper.eq(SysOrg::getParentId, sysOrg.getParentId());
            }
            long count = super.count(queryWrapper);
            if (count > 0) {
                return CommonResult.error(I18nUtil.getMessage("org.name.exists"));
            }
        }
        super.saveOrUpdate(sysOrg);
        return CommonResult.ok();
    }

    @Override
    public CommonResult delete(Long orgId, String userAccount) {
        Long count = sysPositionMapper.selectCount(new LambdaQueryWrapper<SysPosition>().eq(SysPosition::getOrgId, orgId));
        if(count > 0){
            return CommonResult.error(I18nUtil.getMessage("org.position.used"));
        }
        if(sysRoleMapper.selectCount(new LambdaQueryWrapper<SysRole>().eq(SysRole::getOrgId, orgId)) > 0){
            return CommonResult.error(I18nUtil.getMessage("org.role.used"));
        }
        if(sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOrgId, orgId)) > 0){
            return CommonResult.error(I18nUtil.getMessage("org.user.used"));
        }
        SysOrg org = new SysOrg();
        org.setId(orgId);
        org.setUpdateUser(userAccount);
        org.setUpdateTime(LocalDateTime.now());
        super.removeById(org);
        return CommonResult.ok();
    }

    private void setChildren(OrgTreeDTO parent, List<OrgTreeDTO> allOrgs) {
        List<OrgTreeDTO> children = allOrgs.stream()
                .filter(org -> parent.getId().equals(org.getParentId()))
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
             parent.setChildren(children);
            for (OrgTreeDTO child : children) {
                setChildren(child, allOrgs);
            }
        }
    }

    private void checkOrg(SysOrg org, String userAccount) {
        if(StringUtils.isBlank(org.getName())){
            throw new CommonException(I18nUtil.getMessage("org.name.null"));
        }
        if(org.getId() == null){
            org.setDeleteFlag(0);
            org.setIsEnabled(0);
            org.setCreateUser(userAccount);
            org.setCreateTime(LocalDateTime.now());
        }

        if(org.getDeleteFlag() == null){
            org.setDeleteFlag(0);
        }
        if(org.getIsEnabled() == null){
            org.setIsEnabled(0);
        }
        org.setUpdateUser(userAccount);
        org.setUpdateTime(LocalDateTime.now());
    }
}
