package com.txrd.common.util;

import com.txrd.common.vo.UserVo;

import java.util.Collections;
import java.util.List;

public class LoginUserUtil {

    private static final ThreadLocal<UserVo> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置当前用户信息
     */
    public static void setUser(UserVo userInfo) {
        USER_THREAD_LOCAL.set(userInfo);
    }

    /**
     * 获取当前用户信息
     */
    public static UserVo getUser() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 清除当前用户信息，防止内存泄漏
     */
    public static void clear() {
        USER_THREAD_LOCAL.remove();
    }

    public static List<String> getLoginUserIndtyDataScope() {
        if(USER_THREAD_LOCAL.get() == null) {
            return Collections.emptyList();
        }
        return getUser().getIndtys();
    }

    public static List<String> getLoginUserCityDataScope() {
        if(USER_THREAD_LOCAL.get() == null) {
            return Collections.emptyList();
        }
        return getUser().getCities();
    }

    public static List<String> getLoginUserCountyDataScope() {
        if(USER_THREAD_LOCAL.get() == null) {
            return Collections.emptyList();
        }
        return getUser().getCounties();
    }


}
