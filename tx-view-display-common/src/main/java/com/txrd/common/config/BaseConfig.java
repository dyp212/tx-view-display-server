package com.txrd.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BaseConfig {

    @Value("${path.prefix}")
    private String pathPrefix;
}
