package com.wdy.module.modular.system.service;

import java.util.List;

public interface FileService {
    String completePic2PPT(String pptName, List<String> imgPaths);

    String createPPTWithImgUrl(final String name, final List<String> urlList) throws Exception;

    String createPDFWithImgUrl(final String name, final List<String> urlList);

    byte[] findFileByname(final String name);

    byte[] findFileById(final String id);
}
