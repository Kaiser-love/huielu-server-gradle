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
@Table(name = "PICTURE")
@Getter
@Setter
@NoArgsConstructor
public class Picture implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private long picid;
    @Column(nullable = false)
    private String path;

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER)
    @JoinColumn(name = "dirid")
    @JsonIgnore
    private Directory directory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return picid == picture.picid &&
                Objects.equals(path, picture.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(picid, path);
    }

	@Override
	public String toString() {
		return "Picture{picid='" + picid + "', path='" + path + "'}";
	}
    
}
