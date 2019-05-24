package com.wdy.module.modular.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;


@TableName("role_permission")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RolePermission extends Model<RolePermission> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableId(value = "permissionId")
    private Long permissionId;
    @TableId(value = "roleId")
    private Long roleId;

    public RolePermission(Long permissionId, Long roleId) {
        this.permissionId = permissionId;
        this.roleId = roleId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
