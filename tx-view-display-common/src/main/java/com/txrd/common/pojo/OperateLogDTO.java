package com.txrd.common.pojo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OperateLogDTO implements Serializable {

    private String traceId;      // 链路ID
    private String userId;       // 操作人ID
    private String username;     // 操作人姓名
    private String module;       // 模块
    private String type;         // 类型
    private String method;       // 方法名
    private String url;          // 请求URL
    private String ip;           // IP
    private String params;       // 请求参数(JSON)
    private String result;       // 返回结果(JSON)
    private Long costTime;       // 耗时(ms)
    private Integer status;      // 状态: 1成功, 0失败
    private String errorMsg;     // 异常信息
    private LocalDateTime createTime;
}
