package com.txrd.base.exception;

import com.txrd.base.result.CommonResult;
import com.txrd.base.util.I18nUtil;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    /**
     * 处理 @Validated (类级别) 或 GET 请求参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<Map<String, String>> handleBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return CommonResult.get(400, I18nUtil.getMessage("request.args.error"), errors);
    }

    /**
     * 1. 处理 @Valid 校验失败异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // 返回统一的错误结果
        return CommonResult.get(400, I18nUtil.getMessage("request.args.error"), errors);
    }

    /**
     * 2. 处理 Body 缺失或 JSON 格式错误 (关键！)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResult<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        // 判断具体原因
        if (ex.getMessage() != null && ex.getMessage().contains("Required request body is missing")) {
            return CommonResult.get(400, I18nUtil.getMessage("request.body.null"), null);
        }
        return CommonResult.get(400, I18nUtil.getMessage("request.args.error"), null);
    }

    @ExceptionHandler(FeignException.class)
    public CommonResult<?> handleFeignException(FeignException ex) {
        return CommonResult.get(500, I18nUtil.getMessage("server.feign.exception"), null);
    }

    @ExceptionHandler(Exception.class)
    public CommonResult<?> handleException(Exception ex) {
        if(ex.getMessage().contains("An Authentication object was not found in the SecurityContext") ){
            return CommonResult.get(403, I18nUtil.getMessage("server.authentication.fail"), null);
        }
        return CommonResult.get(500, I18nUtil.getMessage("server.exception"), null);
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public CommonResult<?> handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
        return CommonResult.get(403, I18nUtil.getMessage("server.authentication.fail"), null);
    }

    /**
     * 3. 处理其他所有异常
     */
    @ExceptionHandler(CommonException.class)
    public CommonResult<?> handleCommonException(CommonException e) {
        return CommonResult.get(500,  e.getMessage(), null);
    }
}
