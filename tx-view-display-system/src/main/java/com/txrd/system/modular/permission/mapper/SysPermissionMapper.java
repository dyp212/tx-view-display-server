
package com.txrd.system.modular.permission.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.txrd.system.modular.permission.entity.SysPermission;
import com.txrd.system.modular.role.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限Mapper接口
 *
 **/
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {


}
