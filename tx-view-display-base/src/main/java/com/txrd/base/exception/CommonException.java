package com.txrd.base.exception;

import cn.hutool.core.util.StrUtil;
import com.txrd.base.util.I18nUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends RuntimeException {

    private Integer code;

    private String msg;

    public CommonException() {
        super(I18nUtil.getMessage("server.exception"));
        this.code = 500;
        this.msg = I18nUtil.getMessage("server.exception");
    }

    public CommonException(String msg, Object... arguments) {
        super(StrUtil.format(msg, arguments));
        this.code = 500;
        this.msg = StrUtil.format(msg, arguments);
    }

    public CommonException(Integer code, String msg, Object... arguments) {
        super(StrUtil.format(msg, arguments));
        this.code = code;
        this.msg = StrUtil.format(msg, arguments);
    }
}
