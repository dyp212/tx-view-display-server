package com.txrd.common.config;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterChainDebugger implements CommandLineRunner {

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== 过滤器链信息 ===");
        List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();
        for (int i = 0; i < filterChains.size(); i++) {
            System.out.println("FilterChain " + i + ":");
            for (Filter filter : filterChains.get(i).getFilters()) {
                System.out.println("  " + filter.getClass().getName());
            }
        }
    }
}
