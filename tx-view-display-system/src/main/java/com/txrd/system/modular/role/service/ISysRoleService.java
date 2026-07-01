
package com.txrd.system.modular.role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.role.entity.SysRole;
import com.txrd.system.modular.user.param.GetPageParam;

/**
 * 角色Service接口
 *
 **/
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 分页查询组织列表（平铺模式，支持模糊搜索）
     * @return 分页结果
     */
    IPage<SysRole> selectRolePage(GetPageParam param);

    CommonResult saveRole(SysRole sysRole, String currentAccount);
}
