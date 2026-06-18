package com.txrd.system.modular.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txrd.common.pojo.OperateLogDTO;
import com.txrd.system.modular.log.entity.OperateLogEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface IOperateLogService extends IService<OperateLogEntity> {


    void saveLog(OperateLogDTO logDTO);
}
