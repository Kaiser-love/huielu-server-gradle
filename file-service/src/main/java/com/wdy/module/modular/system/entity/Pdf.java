package com.wdy.module.modular.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 *
 * </p>
 *
 * @author wdy
 * @since 2019-05-18
 */
@TableName("pdf")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "pdf")
@ToString(exclude = {"user"})
@EqualsAndHashCode(callSuper = false, exclude = {"user"})
public class Pdf extends Model<Pdf> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "pdfid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "pdfid", type = IdType.AUTO)
    private Long pdfid;
    @Column(name = "cover")
    @TableField("cover")
    private String cover;
    @Column(name = "datetime")
    @TableField("datetime")
    private Timestamp datetime;
    @Column(name = "file_id")
    @TableField("file_id")
    private String fileId;
    @Column(name = "name")
    @TableField("name")
    private String name;
    @TableField("uid")
    @Transient
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
    private Long uid;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    @TableField(exist = false)
    private TUser user;

    @Override
    protected Serializable pkVal() {
        return this.pdfid;
    }

}
