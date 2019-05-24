package com.wdy.module.modular.system.jpa.dao;

import com.wdy.module.modular.system.entity.Ppt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @program: PPTDao
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-20 03:54
 */
public interface PPTDao extends JpaRepository<Ppt, Long> {
}