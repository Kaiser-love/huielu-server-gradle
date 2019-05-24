package com.wdy.module.modular.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author wdy
 * @since 2019-05-18
 */
@TableName("permission")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Permission extends Model<Permission> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableId(value = "name")
    private String name;
    @TableId(value = "url")
    private String url;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
