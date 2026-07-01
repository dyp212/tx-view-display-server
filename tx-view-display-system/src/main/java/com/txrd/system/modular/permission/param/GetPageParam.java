package com.txrd.system.modular.permission.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分页查询参数")
public class GetPageParam {

    @Schema(description = "分页码")
    private long current =1;

    @Schema(description = "每页数据")
    private long size = 10;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "父级权限ID")
    private Long parentId;

}
