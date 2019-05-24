package com.ronghui.service.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class DirectoryResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private long		dirid;
	private String 	name;
	private String 	picPath;
	private int	 	picCount;
}
