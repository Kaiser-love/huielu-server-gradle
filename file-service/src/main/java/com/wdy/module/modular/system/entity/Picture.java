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
@TableName("picture")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Picture extends Model<Picture> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "picid", type = IdType.AUTO)
    private Long picid;
    @TableId(value = "path")
    private String path;
    @TableId(value = "dirid")
    private Long dirid;
    @Override
    protected Serializable pkVal() {
        return this.picid;
    }
}
