package com.txrd.common.config;

import com.txrd.common.annotation.runner.OperationLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class LogStarterAutoConfiguration {

    @Bean
    public OperationLogAspect operationLogAspect() {
        return new OperationLogAspect();
    }
}
