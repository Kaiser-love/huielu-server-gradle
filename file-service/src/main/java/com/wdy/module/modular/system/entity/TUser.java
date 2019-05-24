package com.wdy.module.modular.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author wdy
 * @since 2019-05-18
 */
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "uid")
@TableName("t_user")
@Entity(name = "t_user")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(exclude = {"ppts"})
@EqualsAndHashCode(callSuper = false, exclude = {"ppts"})
public class TUser extends Model<TUser> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;
    @TableField("birthday")
    @Column(name = "birthday")
    private String birthday;
    @TableField("company")
    @Column(name = "company")
    private String company;
    @TableField("create_time")
    @Column(name = "create_time")
    private Timestamp createTime;
    @TableField("email")
    @Column(name = "email")
    private String email;
    @TableField("head")
    @Column(name = "head")
    private String head;
    @TableField("industry")
    @Column(name = "industry")
    private String industry;
    @TableField("level")
    @Column(name = "level")
    private Integer level;
    @TableField("nickname")
    @Column(name = "nickname")
    private String nickname;
    @TableField("openid")
    @Column(name = "openid")
    private String openid;
    @TableField("password")
    @Column(name = "password")
    private String password;
    @TableField("phone")
    @Column(name = "phone")
    private String phone;
    @TableField("qq")
    @Column(name = "qq")
    private String qq;
    @TableField("sex")
    @Column(name = "sex")
    private Integer sex;
    @TableField("title")
    @Column(name = "title")
    private String title;
    @TableField("token")
    @Column(name = "token")
    private String token;
    @TableField("user_name")
    @Column(name = "userName")
    private String userName;
    @TableField("status")
    @Column(name = "status")
    private Byte status;
    @TableField("activate_status")
    @Column(name = "activateStatus")
    private Byte activateStatus;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @TableField(exist = false)
    @JSONField(serialize = false)
    private List<Ppt> ppts;

    @Override
    protected Serializable pkVal() {
        return this.uid;
    }

    public TUser(String userName) {

        this.userName = userName;
    }

}
