package com.ronghui.service.service;

import com.ronghui.service.entity.Content;

import java.util.List;

public interface ContentService {
	public Content findByName(String name);
	
	public void save(Content content);

	public void delete(long cid);

	public List<Content> findByType(String type);
}
