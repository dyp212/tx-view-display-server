
package com.txrd;

import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.common.annotation.OperationLog;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@EnableDiscoveryClient
@MapperScan("com.txrd.system.modular.*.mapper")
@EnableFeignClients
@RestController
@SpringBootApplication(excludeName = {
        "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration",
        "org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration" // 如果存在
})
@Slf4j
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
        log.info("===模块system启动完成===");
    }

    @Operation(summary = "测试")
    @GetMapping("/test")
    @PreAuthorize("hasRole('SYSTEM') or hasAuthority('test')")
    @OperationLog(module = "系统管理模块", type = "测试系统服务", saveResponseData = true)
    public CommonResult<String> test(@RequestHeader(value = "account", required = false)String currentAccount, @RequestHeader(value = "userId", required = false)String userId) {
        return CommonResult.data(I18nUtil.getMessage("system.test"));
    }
}
