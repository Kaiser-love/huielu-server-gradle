package com.wdy.module.modular.system.controller;

import com.wdy.module.core.common.response.ResponseHelper;
import com.wdy.module.core.common.response.ResultBean;
import com.wdy.module.core.common.utils.ZimgServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @program: ZimgController
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 23:27
 */
@RestController
@Api(description = "Zimg上传API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ZimgController {
    @Autowired
    private ZimgServiceUtil zimgServiceUtil;

    @ApiOperation(value = "图片上传zimg服务器")
    @PostMapping("/zimg/upload")
    public ResponseEntity<ResultBean> uploadZimg(@RequestBody MultipartFile file) {
        return ResponseHelper.OK(zimgServiceUtil.sendPost(file));
    }

    @ApiOperation(value = "图片批量上传zimg服务器")
    @PostMapping(value = "/zimgs/upload", headers = "content-type=multipart/form-data")
    public ResponseEntity<ResultBean> uploadZimgs(@RequestBody List<MultipartFile> files) {
        ArrayList<String> resultList = new ArrayList<>();
        for (MultipartFile file : files) {
            resultList.add(zimgServiceUtil.sendPost(file));
        }
        return ResponseHelper.OK(resultList);
    }

    @ApiOperation(value = "从zimg服务器下载图片")
    @GetMapping("/zimg/download")
    public void downloadZimg(@RequestParam String zimgId, @ApiIgnore HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] data = zimgServiceUtil.getImgFromZimg(zimgId);
        response.setContentType("image/jpeg");//设置输出流内容格式为图片格式
        response.setCharacterEncoding("utf-8");//response的响应的编码方式为utf-8
        OutputStream outputStream = response.getOutputStream();//输出流
        InputStream in = new ByteArrayInputStream(data);//字节输入流
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf, 0, 1024)) != -1) {
            outputStream.write(buf, 0, len);
        }
        in.close();
        outputStream.close();
    }
}