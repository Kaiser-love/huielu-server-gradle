package com.ronghui.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.ronghui.service.entity.Directory;

import java.util.List;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {
    public Directory findByDirid(long dirid);

    public List<Directory> findDirectoriesByUser_Uid(long uid);

    public Directory findDirectoryByUser_UidAndName(long uid, String name);
}
