package com.ronghui.service.service.impl;

import com.ronghui.service.entity.PPT;
import com.ronghui.service.repository.PPTRepository;
import com.ronghui.service.service.PPTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class PPTServiceImpl implements PPTService {
    private final PPTRepository pptRepository;

    @Autowired
    public PPTServiceImpl(PPTRepository pptRepository) {
        this.pptRepository = pptRepository;
    }

    @Override
    public List<PPT> getPPTList(long uid) {
        List<PPT> pptList = pptRepository.findPPTSByUser_Uid(uid);
        pptList.forEach(ppt -> ppt.setUser(null));
        return pptList;
    }

    @Override
    public void savePPTInfo(PPT ppt) {
        pptRepository.save(ppt);
    }

    @Override
    public void updatePPT(PPT ppt) {
        pptRepository.save(ppt);

    }

    @Override
    public void deletePPT(long id) {
        pptRepository.deleteById(id);
    }

    @Override
    public PPT findPPT(long id) {
        return pptRepository.findPPTByPptid(id);
    }

    @Override
    public int countPPTs(long uid) {
        int count = pptRepository.countPPTSByUser_Uid(uid);
        return count;
    }


}
