package com.ronghui.service.entity;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "DIRECTORY")
@Getter
@Setter
@NoArgsConstructor
public class Directory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private long dirid;
    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "uid")
    @JsonIgnore
    private User user;


    @OneToMany(mappedBy = "directory", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonIgnore
    private Set<Picture> pictures = new HashSet<Picture>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Directory directory = (Directory) o;
        return dirid == directory.dirid &&
                Objects.equals(name, directory.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dirid, name);
    }
    
    @Override
    public String toString() {
    		return "Directory{dirid='" + dirid + "', name='" + name + "'}";
    }
}
