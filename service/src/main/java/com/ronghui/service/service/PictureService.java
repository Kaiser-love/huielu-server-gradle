package com.ronghui.service.service;

import com.ronghui.service.entity.Picture;

import java.util.ArrayList;
import java.util.List;

public interface PictureService {
	public abstract void save(Picture picture);
	
	public abstract void update(Picture picture);
	
	public abstract void delete(long picid);
	
	public abstract void deleteList(ArrayList<Long> picList);
	
	public abstract Picture findById(long picid);

	public abstract List<Picture> findByDirId(long dirid);

    public abstract void clearLazyField(List<Picture> pictures);
}
