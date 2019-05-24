package com.wdy.module.modular.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;

@TableName("user_role")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserRole extends Model<RolePermission> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableId(value = "roleId")
    private Long roleId;
    @TableId(value = "userId")
    private Long userId;

    public UserRole(Long roleId, Long userId) {
        this.roleId = roleId;
        this.userId = userId;
    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
