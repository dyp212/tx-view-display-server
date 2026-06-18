
package com.txrd.system.modular.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.user.entity.SysUser;
import com.txrd.system.vo.UserVo;

import java.util.List;

/**
 * 用户Service接口
 *
 **/
public interface ISysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     * @param page 分页对象 (当前页, 每页大小)
     * @param user 查询条件 (可选，用于模糊搜索)
     * @return 分页结果
     */
    IPage<SysUser> selectUserPage(IPage<SysUser> page, SysUser user);

    /**
     * 根据账号查询用户
     */
    UserVo getByAccount(String account);

    CommonResult saveUser(SysUser user, String currentAccount);
}
