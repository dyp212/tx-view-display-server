
package com.txrd.system.modular.position.service.impl;


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
import com.txrd.system.modular.position.mapper.SysPositionMapper;
import com.txrd.system.modular.position.service.ISysPositionService;
import com.txrd.system.modular.position.param.GetPageParam;
import com.txrd.system.modular.user.entity.SysUser;
import com.txrd.system.modular.user.mapper.SysUserMapper;
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
 * 职位Service接口实现类
 *
 **/
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements ISysPositionService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysOrgMapper sysOrgMapper;

    @Override
    public IPage<SysPosition> selectPositionPage(GetPageParam param) {
        IPage<SysPosition> page = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
// 名称模糊搜索
        wrapper.like(StringUtils.isNotBlank(param.getName()), SysPosition::getName, param.getName());
        //
        wrapper.eq(null != param.getOrgId(), SysPosition::getOrgId, param.getOrgId());
        // 默认过滤已删除数据
        wrapper.eq(SysPosition::getDeleteFlag, "0");

        // 排序：先按排序码，再按创建时间
        wrapper.orderByAsc(SysPosition::getSortCode)
                .orderByDesc(SysPosition::getCreateTime);
        IPage<SysPosition> result = this.page(page, wrapper);
        if(!CollectionUtils.isEmpty(result.getRecords())){
            List<Long> orgIds = result.getRecords().stream().map(SysPosition::getOrgId).filter(Objects::nonNull).toList();
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
    public CommonResult savePostion(SysPosition position, String currentAccount) {
        this.checkPosition(position, currentAccount);
        if(position.getId() == null){
            LambdaQueryWrapper<SysPosition> queryWrapper = new LambdaQueryWrapper<SysPosition>().eq(SysPosition::getName, position.getName());
            long count = super.count(queryWrapper);
            if (count > 0) {
                return CommonResult.error(I18nUtil.getMessage("position.name.exists"));
            }
        }
        super.saveOrUpdate(position);
        return CommonResult.data(position);
    }

    @Override
    public CommonResult delete(Long positionId, String userAccount) {
        Long count = sysUserMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPositionId, positionId));
        if(count > 0){
            return CommonResult.error(I18nUtil.getMessage("position.user.used"));
        }
        SysPosition position = new SysPosition();
        position.setId(positionId);
        position.setDeleteFlag(1); // 标记删除
        position.setUpdateUser(userAccount);
        position.setUpdateTime(LocalDateTime.now());
        super.removeById(position);
        return CommonResult.ok();
    }

    @Override
    public SysPosition getInfoById(Long positionId) {
        SysPosition position = super.getById(positionId);
        if(position != null && position.getOrgId() != null){
            SysOrg sysOrg = sysOrgMapper.selectById(position.getOrgId());
            position.setOrgName(sysOrg.getName());
        }
        return position;
    }

    private void checkPosition(SysPosition position, String userAccount) {
        if(StringUtils.isBlank(position.getName())){
            throw new CommonException(I18nUtil.getMessage("position.name.null"));
        }
        if(position.getId() == null){
            position.setDeleteFlag(0);
            position.setIsEnabled(0);
            position.setCreateUser(userAccount);
            position.setCreateTime(LocalDateTime.now());
        }

        if(position.getDeleteFlag() == null){
            position.setDeleteFlag(0);
        }
        if(position.getIsEnabled() == null){
            position.setIsEnabled(0);
        }
        position.setUpdateUser(userAccount);
        position.setUpdateTime(LocalDateTime.now());
    }
}
