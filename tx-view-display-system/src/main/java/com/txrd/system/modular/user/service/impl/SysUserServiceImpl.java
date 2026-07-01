
package com.txrd.system.modular.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txrd.base.exception.CommonException;
import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.system.modular.user.entity.SysUser;
import com.txrd.system.modular.user.mapper.SysUserMapper;
import com.txrd.system.modular.user.param.GetPageParam;
import com.txrd.system.modular.user.service.ISysUserService;
import com.txrd.common.vo.RoleVo;
import com.txrd.common.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户Service接口实现类
 *
 **/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {


    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public IPage<SysUser> selectUserPage(GetPageParam param){
        IPage<SysUser> page = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        // 姓名模糊查询
        wrapper.like(StringUtils.isNotBlank(param.getName()), SysUser::getName, param.getName());
        // 账号模糊查询
        wrapper.like(StringUtils.isNotBlank(param.getAccount()), SysUser::getAccount, param.getAccount());
        // 手机号模糊查询
        wrapper.like(StringUtils.isNotBlank(param.getPhone()), SysUser::getPhone, param.getPhone());
        // 状态精确查询
        wrapper.eq(param.getUserStatus() != null, SysUser::getUserStatus, param.getUserStatus());
        // 机构ID精确查询
        wrapper.eq(null != param.getOrgId(), SysUser::getOrgId, param.getOrgId());

        // 默认只查询未删除的数据
        wrapper.eq(SysUser::getDeleteFlag, "0");

        // 按创建时间倒序排列
        wrapper.orderByDesc(SysUser::getCreateTime);

        // 执行分页查询
        return this.page(page, wrapper);
    }

    @Override
    public UserVo getByAccount(String account) {
        SysUser sysUser = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getAccount, account)
                .eq(SysUser::getDeleteFlag, "0"));
        if(sysUser == null){
            return null;
        }
        UserVo userVo = objectMapper.convertValue(sysUser, UserVo.class);
        List<RoleVo> roles = userMapper.selectUserRoles(sysUser.getId());
        userVo.setRoles(roles);
        userVo.setPermissions(userMapper.selectPermissions(sysUser.getId()));
        //@TODO
        List<String> indtys = null;
        userVo.setIndtys(indtys);
        List<String> cities = null;
        userVo.setCities(cities);
        List<String> counties = null;
        userVo.setCounties(counties);
        return userVo;
    }

    @Override
    @Transactional(rollbackFor = CommonException.class)
    public CommonResult<Object> saveUser(SysUser user, String currentAccount) {
        if(user.getId() == null) {
            long count = super.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, user.getAccount()));
            if (count > 0) {
                return CommonResult.error(I18nUtil.getMessage("user.account.exists"));
            }
        }
        this.checkUser(user, currentAccount);
        super.saveOrUpdate(user);
        return CommonResult.ok();
    }

    private void checkUser(SysUser user, String userAccount) {
        if(StringUtils.isBlank(user.getAccount())){
            throw new CommonException(I18nUtil.getMessage("user.account.null"));
        }
        if(user.getId() == null){
            if(user.getPassword() == null){
                user.setPassword("123456");
            }
            user.setUserStatus(0);
            user.setDeleteFlag(0);
            user.setCreateUser(userAccount);
            user.setCreateTime(LocalDateTime.now());
        }
        if(user.getUserStatus() == null){
            user.setUserStatus(0);
        }
        if(user.getDeleteFlag() == null){
            user.setDeleteFlag(0);
        }
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        user.setUpdateUser(userAccount);
        user.setUpdateTime(LocalDateTime.now());
    }

}
