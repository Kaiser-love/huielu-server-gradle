package com.ronghui.service.service;

import com.ronghui.service.entity.PPT;

import java.util.List;

public interface PPTService {
    public List<PPT> getPPTList(long uid);

    public void savePPTInfo(PPT ppt);

    public void updatePPT(PPT ppt);

    public void deletePPT(long id);

    public PPT findPPT(long id);

    public int countPPTs(long uid);
}
