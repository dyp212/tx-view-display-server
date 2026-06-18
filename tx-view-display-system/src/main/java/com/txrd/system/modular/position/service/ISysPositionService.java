
package com.txrd.system.modular.position.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.org.entity.SysOrg;
import com.txrd.system.modular.position.entity.SysPosition;

import java.util.List;

/**
 * 职位Service接口
 *
 **/
public interface ISysPositionService extends IService<SysPosition> {

    /**
     * 分页查询列表（平铺模式，支持模糊搜索）
     * @param page 分页参数
     * @param position 查询条件
     * @return 分页结果
     */
    IPage<SysPosition> selectPositionPage(IPage<SysPosition> page, SysPosition position);

    CommonResult savePostion(SysPosition position, String currentAccount);
}
