package com.ronghui.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ronghui.service.entity.Picture;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    public Picture findByPicid(long picid);

    public List<Picture> findPicturesByDirectory_Dirid(long dirid);

    @Modifying
    @Query(value = "delete from Picture p where p.picid in (:picList)")
    public void deletePictureList(@Param("picList") ArrayList<Long> picList);
    
}
