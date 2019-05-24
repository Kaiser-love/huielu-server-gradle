package com.ronghui.service.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CONTENT")
@Data
@NoArgsConstructor
public class Content implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private long cid;
    @Column(nullable = false, unique = true)
    private String name;
    private String data;
    @Enumerated(EnumType.STRING)
    private ContextType type = ContextType.All;

    public static enum ContextType {
        All, Android, WeChat, Web;
    }
}
