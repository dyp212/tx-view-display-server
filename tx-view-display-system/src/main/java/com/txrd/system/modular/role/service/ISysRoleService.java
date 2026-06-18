
package com.txrd.system.modular.role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.role.entity.SysRole;

/**
 * 角色Service接口
 *
 **/
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 分页查询组织列表（平铺模式，支持模糊搜索）
     * @param page 分页参数
     * @param role 查询条件
     * @return 分页结果
     */
    IPage<SysRole> selectRolePage(IPage<SysRole> page, SysRole role);

    CommonResult saveRole(SysRole sysRole, String currentAccount);
}
