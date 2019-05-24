package com.ronghui.service.config;


import com.alibaba.fastjson.JSON;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.ronghui.server.network.Message;
import com.ronghui.server.network.RetrofitFactory;

import java.io.*;
import java.util.*;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import org.omg.IOP.Encoding;
import org.springframework.util.CollectionUtils;
import retrofit2.Call;
import retrofit2.http.*;
import springfox.documentation.spring.web.json.Json;

/**
 * @author yemao
 * @date 2017/4/9
 * @description 写自己的代码, 让别人说去吧!
 */

public class Retrofit {

    private static APIFunction mAPIFunction = RetrofitFactory.build(APIFunction.class);

    public static APIFunction API() {
        return mAPIFunction;
    }

    public interface APIFunction {
        @Streaming
        @GET
        Observable<ResponseBody> donloadPic(@Url String picUrl);

        @GET
        Call<ResponseBody> getImgPoints(@Url String picUrl, @Query(value = "str",encoded = true) String imgUrl);

    }

    //网络请求 demo
    public static void main(String[] args) throws Exception {
//        ArrayList<String> imgList = new ArrayList<>();
//        imgList.add("https://starfire.site/pic/1d32fa3e2ded5bc557aca6cc3d9a6d72?p=0");
//        imgList.add("https://starfire.site/pic/78db0e060fb87cc2f326dbf6194315d5?p=0");
//        imgList.add("https://starfire.site/pic/b5b4c20a10bf3643d8a7bac682d19d62?p=0");
//        imgList.add("https://starfire.site/pic/ace218a59f4983f746ce1d05752922c0?p=0");
//
//        try {
//            String fileName = "tdsdt.pptx";
//            System.out.println(Retrofit.API().testPdf(fileName, imgList).execute().body().toString());
//            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
//            Message<String> messageS = Retrofit.API().testGet(fileName).execute().body();
//            if (messageS.getCode() == 0) {
//                byte[] message = Base64.getDecoder().decode(messageS.getBody());
//                fileOutputStream.writ
//                e(message);
//            } else {
//                System.out.println("啥都没有" + messageS.getMsg());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String apiResult = Retrofit.API().getImgPoints("http://123.206.73.65:8000/pic_url", "https://mybucket1-1257353650.cos.ap-guangzhou.myqcloud.com/dismps_image/dispm_0.bmp").execute().body().string();
//        apiResult = apiResult.replace("][",",");
//        System.out.println(apiResult);
//        String[] split = apiResult.split("][");
//        CollectionUtils.arrayToList(split).stream().forEach(a->System.out.println(a));

        Observable<ResponseBody> observable = Retrofit.API().donloadPic("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556188395479&di=8d96b0be8d8903965a71df7008b61eb0&imgtype=0&src=http%3A%2F%2Fwww.gedc.cn%2Fimages%2Fjcdt%2F2017%2F3%2F5%2FA4912B4775714CE88B9D4B6B55FFA506.jpg");
    }
}