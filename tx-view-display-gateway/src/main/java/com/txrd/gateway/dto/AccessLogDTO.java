package com.txrd.gateway.dto;

import lombok.Data;

@Data
public class AccessLogDTO {

    private String traceId;       // 链路ID
    private String requestTime;   // 请求时间
    private String method;        // HTTP方法
    private String url;           // 请求URL
    private String ip;            // 客户端IP
    private String userId;        // 用户ID (从Header获取)
    private String requestBody;   // 请求参数
    private String responseBody;  // 响应结果
    private Integer status;       // 状态码
    private Long costTime;        // 耗时(ms)
    private String errorMsg;      // 异常信息
}
