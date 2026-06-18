package com.txrd.gateway.controller;

import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    /**
     * 系统服务的降级接口
     * 对应配置: fallbackUri: forward:/fallback/system
     */
    @RequestMapping(value = "/fallback/system", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
    public Mono<CommonResult> systemFallback() {
        // 返回 503 状态码，或者根据你们前端约定返回 200 + 错误码
        return Mono.just(CommonResult.get(503, I18nUtil.getMessage("server.busy"), null));
    }



}
