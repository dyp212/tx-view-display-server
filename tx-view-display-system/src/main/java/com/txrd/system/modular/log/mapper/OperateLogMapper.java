package com.txrd.system.modular.log.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.txrd.system.modular.log.entity.OperateLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogMapper extends BaseMapper<OperateLogEntity> {
    // 若有复杂查询可在此扩展
}
