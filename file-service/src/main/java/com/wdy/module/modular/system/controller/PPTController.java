package com.wdy.module.modular.system.controller;

import com.wdy.module.core.common.response.ResponseHelper;
import com.wdy.module.core.common.annotion.BussinessLog;
import com.wdy.module.core.common.constant.ControllerModeConstant;
import com.wdy.module.core.common.request.QueryAllBean;
import com.wdy.module.core.common.response.ResultBean;
import com.wdy.module.core.common.response.SuccessAndErrorList;
import com.wdy.module.core.common.utils.*;
import com.wdy.module.modular.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * @program: PPTController
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 12:49
 */
@RestController
@Api(description = "PPT相关API")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class PPTController {
    @Autowired
    private BaseService baseService;
    @Autowired
    private PptService pptService;
    @Autowired
    private FileService fileService;

    @GetMapping(value = "/ppt/savePPt")
    @ApiOperation("根据多张zimg的图片路径生成PPT")
    public ResponseEntity<ResultBean> completePic2PPT(@RequestParam(value = "pptName") String pptName, @RequestParam(value = "imgPaths") ArrayList<String> imgs) throws Exception {
        Object ppt = pptService.savePPTOrPDF(pptName, imgs, ControllerModeConstant.PPTCONTROLLER_DO_PPT);
        return ResponseHelper.OK(ppt);
    }

    @GetMapping("/pdf/savePDF")
    @ApiOperation("根据多张zimg的图片路径生成PDF")
    public ResponseEntity<ResultBean> completePic2PDF(@RequestParam(value = "pdfName") String pdfName, @RequestParam(value = "imgPaths") ArrayList<String> imgs) throws Exception {
        Object pdf = pptService.savePPTOrPDF(pdfName, imgs, ControllerModeConstant.PPTCONTROLLER_DO_PDF);
        return ResponseHelper.OK(pdf);
    }

    @GetMapping(value = "/pptOrpdf/download")
    @ApiOperation("根据mongodb的id以指定的名字下载ppt或者pdf,0为PPT，1为PDF")
    public void downLoadPPTOrPDF(@RequestParam String id, @RequestParam(required = false) Integer mode, @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response, @RequestParam String name) {
        byte[] file = fileService.findFileById(id);
        try {
            long start = System.currentTimeMillis();
            log.info("----------开始下载文件，文件长度[" + file.length + "]");
            HttpResponseUtil.writeToResponse(request, response, name, mode);
            HttpResponseUtil.exportFile(response, file);
            System.out.println("耗时:[" + (System.currentTimeMillis() - start) + "]ms");
            log.info("----------下载文件完成");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @GetMapping(value = "/ppt/getPPTList")
    @ApiOperation("获得PPT列表")
    public ResponseEntity<ResultBean> getAllPPT(@RequestParam(required = false) String query, @RequestParam(required = false) String queryString, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer count) throws Exception {
        String result = ConditionUtil.judgeArgument(query, queryString, page, count);
//        Ppt ppt = Ppt.builder().build().selectById(1);
//        Ppt ppt1 = pptDao.findById((long) 1).get();
//        TUser tUser = tUserDao.findById((long) 1).get();
        return baseService.getEntityList(QueryAllBean.builder().query(query).queryString(queryString).page(page).pagecount(count).result(result).entityName("Ppt").build());
    }

    @GetMapping(value = "/pdf/getPDFList")
    @ApiOperation("获得PDF列表")
    public ResponseEntity<ResultBean> getAllPDF(@RequestParam(required = false) String query, @RequestParam(required = false) String queryString, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer count) throws Exception {
        String result = ConditionUtil.judgeArgument(query, queryString, page, count);
//        Ppt ppt = Ppt.builder().build().selectById(1);
//        Ppt ppt1 = pptDao.findById((long) 1).get();
//        TUser tUser = tUserDao.findById((long) 1).get();
        return baseService.getEntityList(QueryAllBean.builder().query(query).queryString(queryString).page(page).pagecount(count).result(result).entityName("Pdf").build());
    }


    @ApiOperation(value = "图片批量上传生成PPT或PDF,0为PPT,1为PDF")
    @PostMapping(value = "/pptOrpdf/upload", headers = "content-type=multipart/form-data")
    @BussinessLog("图片批量上传生成PPT或PDF,0为PPT,1为PDF")
    public ResponseEntity<ResultBean> uploadPicture2PPTOrPDF(@RequestBody List<MultipartFile> files, @RequestParam String name, @RequestParam(required = false) Integer mode) throws Exception {
        SuccessAndErrorList resultList = pptService.uploadPicture2PPTOrPDF(files, name, mode);
        return ResponseHelper.OK(resultList);
    }
}