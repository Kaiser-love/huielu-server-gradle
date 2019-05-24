package com.wdy.module.core.common.utils;

import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.wdy.module.config.retrofit.Retrofit.API;

/**
 * @program: ImageHelper
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-20 01:46
 */
@Slf4j
@Component
public class ImageHelper {
    @Value("${zimg.uri-temp}")
    private String zimgUriTmp;

    public Single<List<byte[]>> getPictureFromNet(final List<String> urlList) {
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

    public int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);

        return p;
    }
}