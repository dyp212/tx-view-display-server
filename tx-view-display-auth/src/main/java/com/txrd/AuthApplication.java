
package com.txrd;

import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import com.txrd.common.annotation.OperationLog;
import com.txrd.system.api.IUserClient;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableDiscoveryClient
@EnableFeignClients
@RestController
@SpringBootApplication(excludeName = {
        "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration",
        "org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration" // 如果存在
})
@Slf4j
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
        log.info("===模块auth启动完成===");
    }

    @Autowired
    private IUserClient iUserClient1;

    @Operation(summary = "获取所有资源")
    @GetMapping("/test")
    @OperationLog(module = "鉴权管理模块", type = "测试", saveResponseData = true)
    public CommonResult<String> test() {
        iUserClient1.test();
        return CommonResult.data(I18nUtil.getMessage("auth.test"));
    }
}
