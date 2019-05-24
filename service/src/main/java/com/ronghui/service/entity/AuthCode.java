package com.ronghui.service.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthCode implements Serializable {
	private static final long serialVersionUID = 1L;
	private String phone;
	private String code;
	private long createtime;

	public AuthCode(String p, String c, long t) {
		phone = p;
		code = c;
		createtime = t;
	}
	
}
