package com.ronghui.service.service;

import com.ronghui.server.network.Message;

import java.util.ArrayList;

public interface FileService {
    public Message<String> completePic2PPT(String pptName, ArrayList<String> imgPaths);

    public String createPPTWithImgUrl(final String name, final ArrayList<String> urlList);

    public String createPDFWithImgUrl(final String name, final ArrayList<String> urlList);

    public byte[] findFileByname(final String name);

    public byte[] findFileById(final String id);
}
