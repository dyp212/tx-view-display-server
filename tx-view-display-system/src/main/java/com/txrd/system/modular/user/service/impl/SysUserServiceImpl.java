
package com.txrd.system.modular.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.txrd.base.exception.CommonException;
import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.org.mapper.SysOrgMapper;
import com.txrd.system.modular.position.entity.SysPosition;
import com.txrd.system.modular.position.mapper.SysPositionMapper;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.user.entity.SysUser;
import com.txrd.system.modular.user.mapper.SysUserMapper;
import com.txrd.system.modular.user.param.GetPageParam;
import com.txrd.system.modular.user.service.ISysUserService;
import com.txrd.common.vo.RoleVo;
import com.txrd.common.vo.UserVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Resource
    private SysOrgMapper sysOrgMapper;
    @Resource
    private SysPositionMapper sysPositionMapper;

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

        IPage<SysUser> result = this.page(page, wrapper);
        if(!CollectionUtils.isEmpty(result.getRecords())){
            List<Long> orgIds = result.getRecords().stream().map(SysUser::getOrgId).filter(Objects::nonNull).toList();
            if(!CollectionUtils.isEmpty(orgIds)){
                List<SysOrg> sysOrgs = sysOrgMapper.selectBatchIds(orgIds);
                if(!CollectionUtils.isEmpty(sysOrgs)){
                    Map<Long, String> orgMap = sysOrgs.stream().collect(Collectors.toMap(SysOrg::getId, SysOrg::getName));
                    result.getRecords().forEach(item -> {
                        item.setOrgName(orgMap.get(item.getOrgId()));
                    });
                }
            }
            //
            List<Long> positionIds = result.getRecords().stream().map(SysUser::getPositionId).filter(Objects::nonNull).toList();
            if(!CollectionUtils.isEmpty(positionIds)){
                List<SysPosition> positions = sysPositionMapper.selectBatchIds(positionIds);
                if(!CollectionUtils.isEmpty(positions)){
                    Map<Long, String> positionMap = positions.stream().collect(Collectors.toMap(SysPosition::getId, SysPosition::getName));
                    result.getRecords().forEach(item -> {
                        item.setPositionName(positionMap.get(item.getPositionId()));
                    });
                }
            }
        }
        return result;
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
        return CommonResult.data(user);
    }

    @Override
    public SysUser getInfoById(Long userId) {
        SysUser user = super.getById(userId);
        if(user != null){
            if(user.getOrgId() != null){
                SysOrg sysOrg = sysOrgMapper.selectById(user.getOrgId());
                user.setOrgName(sysOrg.getName());
            }
            if(user.getPositionId() != null){
                SysPosition sysPosition = sysPositionMapper.selectById(user.getPositionId());
                user.setPositionName(sysPosition.getName());
            }
        }
        return user;
    }

    private void checkUser(SysUser user, String userAccount) {
        if(StringUtils.isBlank(user.getAccount())){
            throw new CommonException(I18nUtil.getMessage("user.account.null"));
        }
        if(user.getId() == null){
            if(user.getPassword() == null){
                user.setPassword("123456");
            }
            user.setUserStatus(1);
            user.setDeleteFlag(0);
            user.setCreateUser(userAccount);
            user.setCreateTime(LocalDateTime.now());
        }
        if(user.getUserStatus() == null){
            user.setUserStatus(1);
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
