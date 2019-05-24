package com.ronghui.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity

@Table(name = "USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long uid;
    private String username;
    private String nickname;
    private String head;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    private int sex;					// 0 male 1 female
    private String birthday;
    @Column(unique = true)
    private String phone;
    private String qq;
    private String openid;			// wechat openid
    @Column(unique = true)
    private String email;
    private String company;
    private String title;
    private String industry;
    private int level;				// default 0 level 0 normal user 1 super user 2 system user
    private String token;
    @Column(nullable = false)
    @JsonIgnore
    private Date createTime = new Date(System.currentTimeMillis());
    @Transient
    private int pptCount;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonIgnore
    private Set<Directory> directorys = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonIgnore
    private Set<PPT> ppts = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid == user.uid &&
                sex == user.sex &&
                level == user.level &&
                Objects.equals(nickname, user.nickname) &&
                Objects.equals(head, user.head) &&
                Objects.equals(password, user.password) &&
                Objects.equals(birthday, user.birthday) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(qq, user.qq) &&
                Objects.equals(openid, user.openid) &&
                Objects.equals(email, user.email) &&
                Objects.equals(company, user.company) &&
                Objects.equals(title, user.title) &&
                Objects.equals(industry, user.industry) &&
                Objects.equals(token, user.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, nickname, head, password, sex, birthday, phone, qq, openid, email, company, title, industry, level, token);
    }
    
    @Override
    public String toString() {
    		return "User{" + "uid='" + uid + "', username='" + username + "', nickname='" + nickname + "', head='" + head + "', sex='" + sex + "', "
    				+ "birthday='" + birthday + "', phone='" + phone + "', qq='" + qq + "', openid='" + openid + "', email='" + email + "', "
    				+ "company='" + company + "', title='" + title + "', industry='" + industry + "', level='" + level + "', token='" + token + "'}";
    }
}
