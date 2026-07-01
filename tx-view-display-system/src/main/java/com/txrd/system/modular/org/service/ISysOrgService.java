package com.txrd.system.modular.org.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.org.dto.OrgTreeDTO;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.org.param.GetPageParam;

import java.util.List;

public interface ISysOrgService extends IService<SysOrg> {

    /**
     * 分页查询组织列表（平铺模式，支持模糊搜索）
     * @return 分页结果
     */
    IPage<SysOrg> selectOrgPage(GetPageParam param);

    /**
     * 获取组织树形结构
     * @return 树形列表
     */
    List<OrgTreeDTO> getOrgTree();

    /**
     *
     * @param sysOrg
     * @return
     */
    CommonResult saveOrg(SysOrg sysOrg, String currentAccount);
}
