package com.ronghui.service.controller;

import com.ronghui.server.files.network.ResCode;
import com.ronghui.server.network.Message;
import com.ronghui.service.entity.PPT;
import com.ronghui.service.entity.User;
import com.ronghui.service.service.FileService;
import com.ronghui.service.service.PPTService;
import com.ronghui.service.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@Slf4j
public class PPTController {


    private final UserService userService;
    private final FileService fileService;
    private final PPTService pptService;

    @Autowired
    public PPTController(FileService fileService, UserService userService, PPTService pptService) {
        this.fileService = fileService;
        this.userService = userService;
        this.pptService = pptService;
    }

    /*
     * 生成PPT
     * TEST
     */
    @GetMapping(value = "saveppt")
    @ApiOperation("生成PPT")
    public Message<String> completePic2PPT(@RequestParam(value = "pptName") String pptname, @RequestParam(value = "imgPaths") ArrayList<String> imgs, @ApiIgnore HttpSession session) {
        Message<String> msg = new Message<>();
        User user = (session.getAttribute("user"))!=null?((User) session.getAttribute("user")): User.builder().uid(1).build();
        if (user == null) {
            msg.setResCode(ResCode.RELOGIN);
            return msg;
        }
        if (StringUtils.isEmpty(pptname)) {
            log.error("PPT名字为空");
            msg.setResCode(ResCode.INTERNAL_ERROR);
            return msg;
        }
        if (CollectionUtils.isEmpty(imgs)) {
            log.error("生成PPT的图片列表为空");
            msg.setResCode(ResCode.INTERNAL_ERROR);
            return msg;
        }
        if (!pptname.endsWith(".ppt")) {
            pptname += ".ppt";
        }
        String pptID = fileService.createPPTWithImgUrl(pptname, imgs);
        if (!StringUtils.isEmpty(pptID)) {
            PPT ppt = new PPT();
            long uid = user.getUid();
            // TODO: NEED SEARCH USER IN DATABASE?
            User dbUser = userService.findById(uid);
            ppt.setUser(dbUser);
            ppt.setName(pptname);
            ppt.setDatetime(new Date(System.currentTimeMillis()).getTime());
            ppt.setCover(imgs.get(0));
            ppt.setFileId(pptID);
            pptService.savePPTInfo(ppt);
            msg.setResCode(ResCode.SUCCESS);
            msg.setBody(pptID);
        } else {
            log.error("PPT ID为空");
            msg.setResCode(ResCode.INTERNAL_ERROR);
        }
        return msg;
    }


    @GetMapping(value = "getPPTList")
    @ApiOperation("获得PPT列表")
    public Message<List<PPT>> getPPTList(@ApiIgnore HttpSession session) {
        Message<List<PPT>> msg = new Message<>();
        long uid =  (session.getAttribute("user"))!=null?((User) session.getAttribute("user")).getUid(): User.builder().uid(1).build().getUid();
        // TODO: NEED SEARCH USER IN DATABASE?
//        User dbUser = userService.findById(uid);
        List<PPT> pptList = pptService.getPPTList(uid);
        msg.setResCode(ResCode.SUCCESS);
        msg.setBody(pptList);
        return msg;
    }

    /*
     * 生成PDF
     * TEST
     */
    @GetMapping("/savepdf")
    @Transactional
    @ApiOperation("生成PDF")
    public Message<String> completePic2PDF(@RequestParam(value = "pptName") String pptname, @RequestParam(value = "imgPaths") ArrayList<String> imgs) {
        Message<String> msg = new Message<>();

        String pdfid = fileService.createPDFWithImgUrl(pptname, imgs);
        if (!StringUtils.isEmpty(pdfid)) {
            msg.setResCode(ResCode.SUCCESS);
            msg.setBody(pdfid);
        } else {
            log.error("生成PDF出错");
            msg.setResCode(ResCode.INTERNAL_ERROR);
        }
        return msg;
    }


    private void encodeMessage(Message<String> msg, byte[] file) {
        if (file != null) {
            msg.setResCode(ResCode.SUCCESS);
            msg.setBody(Base64.getEncoder().encodeToString(file));
        } else {
            log.error("PPT不存在");
            msg.setResCode(ResCode.INTERNAL_ERROR);
        }
    }


    @GetMapping(value = "/download")
    @ApiOperation("下载文件")
    public void download3(@ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response, @RequestParam String name)
            throws IOException {
        byte[] file = fileService.findFileById(name);
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // headers.setContentDispositionFormData("attachment",
        // cmnTmpFile.getFileName());
        try {
            long start = System.currentTimeMillis();
            log.info("----------开始下载文件，文件长度[" + file.length + "]");
            InputStream fis = new BufferedInputStream(new ByteArrayInputStream(file));
            response.reset();
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + new String(name.getBytes(), "iso-8859-1"));
            response.addHeader("Content-Length", "" + file.length);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            byte[] buffer = new byte[1024 * 1024 * 4];
            int i = -1;
            while ((i = fis.read(buffer)) != -1) {
                toClient.write(buffer, 0, i);
            }
            fis.close();
            toClient.flush();
            toClient.close();

            System.out.println("耗时:[" + (System.currentTimeMillis() - start) + "]ms");
            log.info("----------下载文件完成");
        } catch (IOException ex) {
        }
    }


}

