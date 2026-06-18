
package com.txrd.system.modular.position.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txrd.base.exception.CommonException;
import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.position.entity.SysPosition;
import com.txrd.system.modular.position.mapper.SysPositionMapper;
import com.txrd.system.modular.position.service.ISysPositionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 职位Service接口实现类
 *
 **/
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements ISysPositionService {


    @Override
    public IPage<SysPosition> selectPositionPage(IPage<SysPosition> page, SysPosition position) {
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();

        // 动态查询条件
        if (position != null) {
            // 名称模糊搜索
            wrapper.like(StringUtils.isNotBlank(position.getName()), SysPosition::getName, position.getName());
            //
            wrapper.eq(null != position.getOrgId(), SysPosition::getOrgId, position.getOrgId());
        }

        // 默认过滤已删除数据
        wrapper.eq(SysPosition::getDeleteFlag, "0");

        // 排序：先按排序码，再按创建时间
        wrapper.orderByAsc(SysPosition::getSortCode)
                .orderByDesc(SysPosition::getCreateTime);

        return this.page(page, wrapper);
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
        return CommonResult.ok();
    }

    private void checkPosition(SysPosition position, String userAccount) {
        if(StringUtils.isBlank(position.getName())){
            throw new CommonException(I18nUtil.getMessage("position.name.null"));
        }
        if(position.getId() == null){
            position.setDeleteFlag(0);
            position.setCreateUser(userAccount);
            position.setCreateTime(LocalDateTime.now());
        }

        if(position.getDeleteFlag() == null){
            position.setDeleteFlag(0);
        }
        position.setUpdateUser(userAccount);
        position.setUpdateTime(LocalDateTime.now());
    }
}
