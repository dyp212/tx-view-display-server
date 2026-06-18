package com.txrd.gateway.service;

import com.txrd.base.util.RedisUtil;
import com.txrd.base.constant.SystemConstant;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PermissionCacheService {

    @Autowired
    private RedisUtil redisUtil;

    // 本地缓存: Key=URL Pattern (如 /user/*), Value=Permission Code
    private final Map<String, String> localPermissionMap = new ConcurrentHashMap<>();

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 服务启动时加载权限规则到本地内存
     */
    @PostConstruct
    public void loadPermissions() {
        refreshCache();
    }

    /**
     * 刷新缓存（可暴露接口供手动触发，或监听 MQ 消息自动刷新）
     */
    public void refreshCache() {
        Map<Object, Object> entries = redisUtil.hGetAll(SystemConstant.PERMISSION_MAP_KEY);
        localPermissionMap.clear();
        if (entries != null) {
            entries.forEach((k, v) -> {
                if (k instanceof String && v instanceof String) {
                    localPermissionMap.put((String) k, (String) v);
                }
            });
        }
        log.info("网关权限缓存已更新，共 " + localPermissionMap.size() + " 条规则");
    }

    /**
     * 根据请求路径获取所需权限
     * @param requestPath 例如: /sys/user/1001
     * @return 权限码，如果没有配置则返回 null
     */
    public String getRequiredPermission(String requestPath) {
        // 1. 先尝试精确匹配 (性能最优)
        String exactPermission = localPermissionMap.get(requestPath);
        if (exactPermission != null) {
            return exactPermission;
        }

        // 2. 如果没有精确匹配，遍历本地缓存进行 Ant 风格匹配
        // 注意：如果规则非常多（上千条），这里可以考虑优化数据结构，但通常几百条规则内存遍历非常快
        for (Map.Entry<String, String> entry : localPermissionMap.entrySet()) {
            String pattern = entry.getKey();
            // 如果 pattern 包含 *，则进行匹配
            if (pattern.contains("*") || pattern.contains("?")) {
                if (pathMatcher.match(pattern, requestPath)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }
}
