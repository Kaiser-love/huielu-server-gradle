package com.wdy.module.modular.system.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.wdy.module.core.common.utils.ImageHelper;
import com.wdy.module.core.common.utils.ZimgServiceUtil;
import com.wdy.module.core.mongodb.MongoDBService;
import com.wdy.module.modular.system.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.sl.usermodel.PictureData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: FileServiceImpl
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-20 01:44
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private MongoDBService mongoDBService;
    @Autowired
    private ImageHelper imageHelper;
    @Autowired
    private ZimgServiceUtil zimgServiceUtil;

    @Override
    public String completePic2PPT(String pptName, List<String> imgPaths) {
        return null;
    }

    @Override
    public String createPPTWithImgUrl(String name, List<String> urlList) throws Exception {
        final String[] success = new String[1];
        imageHelper.getPictureFromNet(urlList)
                .map(bytes -> {
                    HSLFSlideShow ppt = new HSLFSlideShow();
                    for (byte[] aByte : bytes) {
                        HSLFSlide slide = ppt.createSlide();
                        // This slide has its own background.
                        // Without this line it will use master's background.
                        slide.setFollowMasterBackground(false);
                        HSLFFill fill = slide.getBackground().getFill();
                        HSLFPictureData pd = ppt.addPicture(aByte, PictureData.PictureType.PNG);
                        fill.setFillType(HSLFFill.FILL_PICTURE);
                        fill.setPictureData(pd);
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ppt.write(byteArrayOutputStream);
                    return byteArrayOutputStream.toByteArray();
                })
                .map(bytes -> mongoDBService.save(new ByteArrayInputStream(bytes), name, name))
                .subscribe(aBoolean -> success[0] = aBoolean, throwable -> {
                    success[0] = null;
                });
//        List<String> resultList = new ArrayList<>();
//        for (String zimgId : urlList) {
//            String pptId = mongoDBService.save(new ByteArrayInputStream(zimgServiceUtil.getImgFromZimg(zimgId)), name, name);
//            resultList.add(pptId);
//        }
        return success[0];
    }

    @Override
    public String createPDFWithImgUrl(String name, List<String> urlList) {
        final String[] success = new String[1];
        imageHelper.getPictureFromNet(urlList)
                .map(bytes -> {
                    Document document = new Document();
//Step 2—Get a PdfWriter instance.
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    PdfWriter.getInstance(document, byteArrayOutputStream);
//Step 3—Open the Document.
                    document.open();
                    for (byte[] aByte : bytes) {

                        document.newPage();
                        Image img = Image.getInstance(aByte);
                        float heigth = img.getHeight();
                        float width = img.getWidth();
                        int percent = imageHelper.getPercent2(heigth, width);
                        img.setAlignment(Image.MIDDLE);
                        img.scalePercent(percent + 3);
                        document.add(img);
                    }
                    document.close();
                    return byteArrayOutputStream.toByteArray();
                })
                .map(bytes -> mongoDBService.save(new ByteArrayInputStream(bytes), name, name))
                .subscribe(aBoolean -> success[0] = aBoolean, throwable -> success[0] = null);
        return success[0];
    }

    @Override
    public byte[] findFileByname(String name) {
        return new byte[0];
    }

    @Override
    public byte[] findFileById(String id) {
        byte[] file = mongoDBService.getById(id);
        if (file != null) {
            return file;
        } else {
            return new byte[0];
        }
    }
}