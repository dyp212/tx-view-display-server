
package com.txrd;

import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@Slf4j
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        log.info("===模块gateway启动完成===");
    }

    @Operation(summary = "测试")
    @GetMapping("/test")
    public CommonResult<String> test() {
        return CommonResult.data(I18nUtil.getMessage("gateway.test"));
    }
}
