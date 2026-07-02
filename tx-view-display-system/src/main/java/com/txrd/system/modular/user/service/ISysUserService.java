
package com.txrd.system.modular.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.user.entity.SysUser;
import com.txrd.system.modular.user.param.GetPageParam;
import com.txrd.common.vo.UserVo;

/**
 * 用户Service接口
 *
 **/
public interface ISysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     * @return 分页结果
     */
    IPage<SysUser> selectUserPage(GetPageParam param);

    /**
     * 根据账号查询用户
     */
    UserVo getByAccount(String account);

    CommonResult saveUser(SysUser user, String currentAccount);

    SysUser getInfoById(Long userId);
}
