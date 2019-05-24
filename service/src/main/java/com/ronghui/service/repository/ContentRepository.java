package com.ronghui.service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ronghui.service.entity.Content;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    public Content findByCid(int cid);

    public Content findByName(String name);


    public List<Content> findContentsByTypeOrType(Content.ContextType type1, Content.ContextType type2);
}
