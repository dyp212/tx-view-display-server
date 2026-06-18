package com.txrd.common.annotation.runner;

import com.txrd.base.constant.SystemConstant;
import com.txrd.base.util.RedisUtil;
import com.txrd.common.annotation.RequirePermission;
import com.txrd.common.config.BaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class PermissionRegistrar implements CommandLineRunner {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BaseConfig baseConfig;

    // 正则表达式：匹配 {xxx} 格式的路径变量
    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?\\}");

    @Override
    public void run(String... args) throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

        // 清空旧数据
        redisUtil.deleteHashFieldsByPrefix(SystemConstant.PERMISSION_MAP_KEY, baseConfig.getPathPrefix());

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();

            // 检查方法上是否有 @RequirePermission 注解
            RequirePermission permissionAnnotation = handlerMethod.getMethodAnnotation(RequirePermission.class);

            if (permissionAnnotation != null) {
                // 获取请求路径 patterns
                Set<PathPattern> patterns = entry.getKey().getPathPatternsCondition().getPatterns();//.getPatternsCondition().getPatterns();
                String permissionCode = permissionAnnotation.value();

                for (PathPattern pattern : patterns) {
                    // 【核心逻辑】将 /user/{id} 转换为 /user/*
                    String normalizedPath = normalizePath(pattern.getPatternString());

                    // 存入 Redis: Key=/user/*, Value=user:view
                    redisUtil.hSet(SystemConstant.PERMISSION_MAP_KEY, baseConfig.getPathPrefix() + normalizedPath, permissionCode);
                }
            }
        }
    }

    /**
     * 将路径变量 {xxx} 替换为 *
     * 例如: /sys/user/{id}/detail -> /sys/user/ * /detail
     */
    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }
        return PATH_VARIABLE_PATTERN.matcher(path).replaceAll("*");
    }
}
