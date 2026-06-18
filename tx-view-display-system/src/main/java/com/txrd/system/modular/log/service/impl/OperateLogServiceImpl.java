package com.txrd.system.modular.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txrd.common.pojo.OperateLogDTO;
import com.txrd.system.modular.log.entity.OperateLogEntity;
import com.txrd.system.modular.log.mapper.OperateLogMapper;
import com.txrd.system.modular.log.service.IOperateLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLogEntity> implements IOperateLogService {

   @Transactional(propagation = Propagation.REQUIRES_NEW)
   @Override
   public void saveLog(OperateLogDTO logDTO) {
        try {
            // 1. 数据预处理
            preprocessLog(logDTO);

            // 2. 转换 DTO -> Entity
            OperateLogEntity entity = new OperateLogEntity();
            BeanUtils.copyProperties(logDTO, entity);
            entity.setCreateTime(LocalDateTime.now());

            // 3. 插入数据库
            super.save(entity);
        } catch (Exception e) {
            log.error("保存操作日志失败", e);
        }
    }

    /**
     * 数据预处理：脱敏、截断
     */
    private void preprocessLog(OperateLogDTO dto) {
        // 1. 参数脱敏 (例如隐藏 password 字段)
        if (dto.getParams() != null && dto.getParams().contains("password")) {
            dto.setParams(dto.getParams().replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"&zwnj;******&zwnj;\""));
        }

        // 2. 结果截断 (假设数据库字段限制为 2000 字符)
        if (dto.getResult() != null && dto.getResult().length() > 2000) {
            dto.setResult(dto.getResult().substring(0, 2000) + "...[truncated]");
        }

        // 3. 参数截断
        if (dto.getParams() != null && dto.getParams().length() > 2000) {
            dto.setParams(dto.getParams().substring(0, 2000) + "...[truncated]");
        }
    }
}
