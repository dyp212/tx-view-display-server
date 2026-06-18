package com.txrd.system.modular.org.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.txrd.system.modular.org.entity.SysOrg;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrgTreeDTO extends SysOrg {

    private List<OrgTreeDTO> children;
}
