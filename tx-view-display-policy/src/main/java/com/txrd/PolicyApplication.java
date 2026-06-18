package com.txrd;

import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = { SecurityFilterAutoConfiguration.class, SecurityFilterAutoConfiguration.class })
@EnableFeignClients
@MapperScan("com.txrd.policy.modular.*.mapper")
@EnableDiscoveryClient
@RestController
@Slf4j
public class PolicyApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolicyApplication.class, args);
        log.info("===模块policy启动完成===");
    }

    @Operation(summary = "测试")
    @GetMapping("/test")
    public CommonResult<String> test() {
        return CommonResult.data(I18nUtil.getMessage("policy.test"));
    }
}