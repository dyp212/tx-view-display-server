package com.txrd.base.util;

import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class I18nHelper {

    @Resource
    private MessageSource messageSource;

    public String getMessage(String code){
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object[] args) {
       return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
