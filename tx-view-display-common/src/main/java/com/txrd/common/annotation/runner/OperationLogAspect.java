package com.txrd.common.annotation.runner;

import cn.hutool.json.JSONUtil;
import com.txrd.base.util.IPUtil;
import com.txrd.common.annotation.OperationLog;
import com.txrd.common.pojo.OperateLogDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 操作日志AOP切面
 */
@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Autowired
    private OperateLogProducer operateLogProducer;

    @Pointcut("@annotation(com.txrd.common.annotation.OperationLog)")
    public void logPointCut() {}

    @Around("logPointCut()")
//    @Around("@annotation(com.txrd.common.annotation.OperationLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();

        // 1. 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog annotation = method.getAnnotation(OperationLog.class);

        // 2. 构建日志对象
        OperateLogDTO operateLogDTO = new OperateLogDTO();
        operateLogDTO.setCreateTime(LocalDateTime.now());
        operateLogDTO.setModule(annotation.module());
        operateLogDTO.setType(annotation.type());
        operateLogDTO.setMethod(signature.toShortString());

        // 3. 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            operateLogDTO.setTraceId(Optional.ofNullable(request.getHeader("traceId")).orElse((String)request.getAttribute("traceId")));
            operateLogDTO.setUserId(request.getHeader("userId"));
            operateLogDTO.setUsername(request.getHeader("account"));
            operateLogDTO.setUrl(request.getRequestURI());
            operateLogDTO.setIp(IPUtil.getIpAddress(request));

            // 4. 获取请求参数
            if (annotation.saveRequestData()) {
                Object[] args = joinPoint.getArgs();
                operateLogDTO.setParams(JSONUtil.toJsonStr((args)));
            }
        }

        Object result = null;
        try {
            // 执行目标方法
            result = joinPoint.proceed();

            // 5. 记录响应结果
            if (annotation.saveResponseData() && result != null) {
                operateLogDTO.setResult(JSONUtil.toJsonStr(result));
            }
            operateLogDTO.setStatus(1);

        } catch (Throwable e) {
            operateLogDTO.setStatus(0);
            operateLogDTO.setErrorMsg(e.getMessage());
            throw e; // 务必抛出异常，否则事务可能不回滚
        } finally {
            long endTime = System.currentTimeMillis();
            operateLogDTO.setCostTime(endTime - beginTime);

            // 6. 异步发送日志
            log.info("日志信息：-> {}", JSONUtil.toJsonStr(operateLogDTO));
            operateLogProducer.sendLog2Rabbit(operateLogDTO);
        }

        return result;
    }

}
