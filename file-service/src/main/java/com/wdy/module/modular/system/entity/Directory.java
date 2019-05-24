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
@TableName("directory")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Directory extends Model<Directory> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "dirid", type = IdType.AUTO)
    private Long dirid;
    @TableId(value = "name")
    private String name;
    @TableId(value = "uid")
    private Long uid;
    @Override
    protected Serializable pkVal() {
        return this.dirid;
    }
}
