package com.ronghui.service.repository;

import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ronghui.service.entity.PPT;
import com.ronghui.service.entity.User;

import java.util.List;

@Repository
public interface PPTRepository extends JpaRepository<PPT, Long> {
    public List<PPT> findPPTSByUser_Uid(long uid);

    public PPT findPPTByPptid(long pptid);

    public int countPPTSByUser_Uid(long uid);

}
