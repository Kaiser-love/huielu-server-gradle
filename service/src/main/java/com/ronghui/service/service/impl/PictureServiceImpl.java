package com.ronghui.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronghui.service.entity.Picture;
import com.ronghui.service.repository.PictureRepository;
import com.ronghui.service.service.PictureService;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

@Service
public class PictureServiceImpl implements PictureService {
	private final PictureRepository pictureRepository;

	@Autowired
	public PictureServiceImpl(PictureRepository pictureRepository) {
		this.pictureRepository = pictureRepository;
	}

	@Override
	public void save(Picture picture) {
		pictureRepository.save(picture);
	}
	
	@Override
	public void update(Picture picture) {
		pictureRepository.save(picture);
	}
	
	@Override
	public void delete(long picid) {
		pictureRepository.deleteById(picid);
	}
	
	@Override
	@Transactional
	public void deleteList(ArrayList<Long> picList) {
		pictureRepository.deletePictureList(picList);
	}
	
	@Override
	public Picture findById(long picid) {
		return pictureRepository.findByPicid(picid);
	}

	@Override
	public List<Picture> findByDirId(long dirid) {
		return pictureRepository.findPicturesByDirectory_Dirid(dirid);
	}

	@Override
	public void clearLazyField(List<Picture> pictures) {
		pictures.forEach(picture -> {
			picture.setDirectory(null);
		});
	}

}
