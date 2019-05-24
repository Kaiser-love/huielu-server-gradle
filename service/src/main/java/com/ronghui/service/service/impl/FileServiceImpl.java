package com.ronghui.service.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.ronghui.service.service.FileService;
import com.ronghui.server.network.Message;
import com.ronghui.service.service.MongoDBService;

import org.apache.poi.hslf.usermodel.HSLFFill;
import org.apache.poi.hslf.usermodel.HSLFPictureData;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.PictureData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;

import static com.ronghui.service.config.Retrofit.API;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    public FileServiceImpl(MongoDBService mongoDBService) {
        this.mongoDBService = mongoDBService;
    }

    @Override
    public Message<String> completePic2PPT(String pptName, ArrayList<String> imgPaths) {

        return null;
    }

    @Value("${zimg.uri-tmp}")
    private String zimgUriTmp ;
    private final MongoDBService mongoDBService;



    public String createPPTWithImgUrl(final String name, final ArrayList<String> urlList) {
        final String[] success = new String[1];
        getPictureFromNet(urlList)
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
                    System.out.println(throwable);
                    success[0] = null;
                });
        return success[0];
    }

    @Override
    public String createPDFWithImgUrl(String name, ArrayList<String> urlList) {
        final String[] success = new String[1];
        getPictureFromNet(urlList)
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
                        int percent = getPercent2(heigth, width);
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
        byte[] file = mongoDBService.getByFileName(name);
        if (file != null) {
            return file;
        } else {
            return null;
        }

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


    private int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);

        return p;
    }


    private Single<List<byte[]>> getPictureFromNet(final List<String> urlList) {
        List<Observable<ResponseBody>> observableList = new ArrayList<>();
        for (String url : urlList) {
            log.debug("图片地址：{}", url);
            observableList.add(API().donloadPic(String.format(zimgUriTmp, url)));
        }
        Observable<ResponseBody> observable = Observable.concat(observableList);
        return observable.collect(ArrayList::new, (bytes, responseBody) -> bytes.add(read(responseBody.byteStream())));
    }

    private byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
