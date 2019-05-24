package com.ronghui.service.service;

import com.ronghui.service.entity.Directory;

import java.util.List;

public interface DirectoryService {
	public abstract void save(Directory directory);
	
	public abstract void update(Directory directory);
	
	public abstract void delete(long dirid);
	
	public abstract Directory findById(long dirid);

	public abstract List<Directory> findByUser(long uid);

	public abstract Directory findByUserAndName(long uid,String name);

}
