package com.txrd.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.txrd.common.util.LoginUserUtil;
import com.txrd.common.vo.UserVo;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1) // 确保在高优先级执行
public class UserInfoFilter implements Filter {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            // 1. 从 Header 中获取用户信息字符串
            String userDetails = httpRequest.getHeader("userDetails");

            if (StringUtils.isNotBlank(userDetails)) {
                // 2. 读取 UserInfo 对象
                UserVo userInfo = objectMapper.readValue(userDetails, UserVo.class);

                // 3. 存入 ThreadLocal
                LoginUserUtil.setUser(userInfo);
            }

            // 4. 继续过滤链
            chain.doFilter(request, response);

        } finally {
            // 5. 无论成功与否，请求结束后必须清除 ThreadLocal，防止线程复用导致数据污染
            LoginUserUtil.clear();
        }
    }
}
