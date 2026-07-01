
package com.txrd.system.modular.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.txrd.system.modular.user.entity.SysUser;
import com.txrd.common.vo.PermissionVo;
import com.txrd.common.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 *
 **/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {


    List<RoleVo> selectUserRoles(@Param("userId") Long userId);

    List<PermissionVo> selectPermissions(@Param("userId") Long userId);
}
