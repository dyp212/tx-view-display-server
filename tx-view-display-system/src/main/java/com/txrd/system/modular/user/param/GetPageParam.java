package com.txrd.system.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@Schema(description = "分页查询参数")
public class GetPageParam {

    @Schema(description = "分页码")
    private long current =1;

    @Schema(description = "每页数据")
    private long size = 10;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机")
    private String phone;

    @Schema(description = "用户状态 (如: 0-正常, 1-禁用)")
    private Integer userStatus;

    @Schema(description = "机构ID")
    private Long orgId;

}
