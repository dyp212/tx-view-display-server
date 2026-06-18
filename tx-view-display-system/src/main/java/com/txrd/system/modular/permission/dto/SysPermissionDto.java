package com.txrd.system.modular.permission.dto;

import com.txrd.system.modular.org.dto.OrgTreeDTO;
import com.txrd.system.modular.permission.entity.SysPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysPermissionDto extends SysPermission {

    private List<SysPermissionDto> children;
}
