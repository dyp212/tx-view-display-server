
package com.txrd.system.modular.position.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.base.result.CommonResult;
import com.txrd.system.modular.position.entity.SysPosition;
import com.txrd.system.modular.position.param.GetPageParam;

import java.util.List;

/**
 * 职位Service接口
 *
 **/
public interface ISysPositionService extends IService<SysPosition> {

    /**
     * 分页查询列表（平铺模式，支持模糊搜索）
     * @return 分页结果
     */
    IPage<SysPosition> selectPositionPage(GetPageParam param);

    CommonResult savePostion(SysPosition position, String currentAccount);
}
