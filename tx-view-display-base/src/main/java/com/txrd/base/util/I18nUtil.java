package com.txrd.base.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        I18nUtil.applicationContext = applicationContext;
    }


    public static MessageSource getMessageSource(){
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext has not been initialized");
        }
        return applicationContext.getBean(MessageSource.class);
    }

    /**
     * 根据 Key 获取国际化消息
     *
     * @param code 消息键 (对应 properties 文件中的 key)
     * @return 国际化后的消息字符串
     */
    public static String getMessage(String code) {
        return getMessage(code, (Object) null);
    }

    /**
     * 根据 Key 和参数获取国际化消息
     *
     * @param code 消息键
     * @param args 占位符参数 (对应 properties 中的 {0}, {1}...)
     * @return 国际化后的消息字符串
     */
    public static String getMessage(String code, Object... args) {
        return getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * 根据 Key、参数和指定语言环境获取国际化消息
     *
     * @param code   消息键
     * @param args   占位符参数
     * @param locale 指定语言环境 (如果不传，默认使用当前请求线程的 Locale)
     * @return 国际化后的消息字符串
     */
    public static String getMessage(String code, Object[] args, Locale locale) {
        try {
            // 如果 locale 为 null，则使用当前线程绑定的 Locale
            Locale targetLocale = (locale != null) ? locale : LocaleContextHolder.getLocale();
            return getMessageSource().getMessage(code, args, targetLocale);
        } catch (Exception e) {
            // 如果找不到对应的 key，返回 key 本身或者默认值，避免程序崩溃
            return code;
        }
    }

    /**
     * 获取当前请求的语言环境
     */
    public static Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }
}
