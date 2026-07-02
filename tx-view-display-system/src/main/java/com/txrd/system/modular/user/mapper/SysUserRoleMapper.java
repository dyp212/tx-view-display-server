package com.txrd.system.modular.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.user.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*/
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<SysRole> getUserRoles(@Param("userId") Long userId);
}




