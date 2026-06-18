package com.txrd.system.api;

import com.txrd.base.result.CommonResult;
import com.txrd.system.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tx-view-display-system", path = "")
public interface IUserClient {

    @GetMapping("/test")
    CommonResult<String> test();

    @GetMapping("/sys/user/info/{account}")
    UserVo getInfo(@PathVariable("account") String account);
}
