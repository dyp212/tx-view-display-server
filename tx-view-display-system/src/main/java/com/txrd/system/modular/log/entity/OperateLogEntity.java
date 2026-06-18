package com.txrd.system.modular.log.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_operate_log")
public class OperateLogEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String traceId;
    private String userId;
    private String username;
    private String module;
    private String type;
    private String method;
    private String url;
    private String ip;
    private String params;
    private String result;
    private Long costTime;
    private Integer status;
    private String errorMsg;
    private LocalDateTime createTime;

}
