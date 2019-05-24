package com.ronghui.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronghui.service.entity.Directory;
import com.ronghui.service.repository.DirectoryRepository;
import com.ronghui.service.service.DirectoryService;

import java.util.List;

@Service
public class DirectoryServiceImpl implements DirectoryService {
    private final DirectoryRepository directoryRepository;

    @Autowired
    public DirectoryServiceImpl(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    @Override
    public void save(Directory directory) {
        directoryRepository.save(directory);
    }

    @Override
    public void update(Directory directory) {
        directoryRepository.save(directory);
    }

    @Override
    public void delete(long dirid) {
        directoryRepository.deleteById(dirid);
    }

    @Override
    public Directory findById(long dirid) {
        return directoryRepository.findByDirid(dirid);
    }

    @Override
    public List<Directory> findByUser(long uid) {
        return directoryRepository.findDirectoriesByUser_Uid(uid);
    }

    @Override
    public Directory findByUserAndName(long uid, String name) {
        return directoryRepository.findDirectoryByUser_UidAndName(uid, name);
    }
}
