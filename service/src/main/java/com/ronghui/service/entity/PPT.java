package com.ronghui.service.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PPT")
@Getter
@Setter
@NoArgsConstructor
public class PPT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private long pptid;
    @Column(nullable = false)
    private String cover;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String fileId;
    private long datetime;

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    @JsonIgnore
    private User user;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PPT ppt = (PPT) o;
        return pptid == ppt.pptid &&
        			datetime == ppt.datetime &&
        			Objects.equals(cover, ppt.cover) &&
        			Objects.equals(name, ppt.name) &&
        			Objects.equals(fileId, ppt.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pptid, cover, name, fileId);
    }
    
    @Override
    public String toString() {
    		return "PPT{" + "pptid='" + pptid + "', cover='" + cover + "', name='" + name + "', fileId='" + fileId + "', datetime='" + datetime + "'}";
    }
}
