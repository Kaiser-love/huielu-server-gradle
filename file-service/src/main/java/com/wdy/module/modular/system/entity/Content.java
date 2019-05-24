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
@TableName("content")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Content extends Model<Content> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "cid", type = IdType.AUTO)
    private Long cid;
    @TableId(value = "data")
    private String data;
    @TableId(value = "name")
    private String name;
    @TableId(value = "type")
    private String type;

    @Override
    protected Serializable pkVal() {
        return this.cid;
    }
}
