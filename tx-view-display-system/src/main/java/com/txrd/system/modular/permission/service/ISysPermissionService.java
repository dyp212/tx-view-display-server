
package com.txrd.system.modular.permission.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.permission.dto.SysPermissionDto;
import com.txrd.system.modular.permission.entity.SysPermission;
import com.txrd.system.modular.permission.param.GetPageParam;

import java.util.List;

/**
 * 权限Service接口
 *
 **/
public interface ISysPermissionService extends IService<SysPermission> {

    /**
     * 分页查询组织列表（平铺模式，支持模糊搜索）
     * @return 分页结果
     */
    IPage<SysPermission> selectPermissionPage(GetPageParam param);

    /**
     * 获取组织树形结构
     * @return 树形列表
     */
    List<SysPermissionDto> getPermissionTree();

    CommonResult savePermission(SysPermission permission, String currentAccount);

    CommonResult delete(Long permissionId, String userAccount);
}
