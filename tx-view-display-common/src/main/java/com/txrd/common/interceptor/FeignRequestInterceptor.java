package com.txrd.common.interceptor;

import com.txrd.common.util.LoginUserUtil;
import com.txrd.common.vo.UserVo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 1. 从当前请求的 Header 中获取用户信息（网关透传的）
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        template.header("X-Invoke-Type", "INTERNAL");
        template.header("X-Feign-Source", "FEIGN");
        UserVo userVo = LoginUserUtil.getUser();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            //
            String traceId = Optional.ofNullable(request.getHeader("traceId")).orElse((String)request.getAttribute("traceId"));
            // 透传用户相关 Header
            String userId = request.getHeader("userId");
            String account = request.getHeader("account");
            String userDetails = request.getHeader("userDetails");
            if(StringUtils.isBlank(userId)){
                userId = userVo != null?userVo.getId().toString():"SYSTEM";
            }
            template.header("userId", userId);
           //
            if(StringUtils.isBlank(account)){
                account = userVo != null?userVo.getAccount():"FEIGN";
            }
            template.header("account", account);
            //
            if (traceId != null) {
                template.header("traceId", traceId);
            }
            if(StringUtils.isNotBlank(userDetails)) {
                template.header("userDetails", userDetails);
            }
        }
    }
}
