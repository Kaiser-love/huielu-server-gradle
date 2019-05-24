package com.ronghui.service.controller;

import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ronghui.service.entity.Directory;
import com.ronghui.server.files.network.ResCode;
import com.ronghui.server.network.Message;
import com.ronghui.service.entity.Picture;
import com.ronghui.service.entity.User;
import com.ronghui.service.service.DirectoryService;
import com.ronghui.service.service.PictureService;
import com.ronghui.service.service.UserService;
import javax.servlet.http.HttpSession;

import springfox.documentation.annotations.ApiIgnore;

@RestController
public class PictureController {
	private static final Logger log = LoggerFactory.getLogger(PictureController.class);
    private final DirectoryService directoryService;
    private final PictureService pictureService;


    private final UserService userService;

    @Autowired
    public PictureController(DirectoryService directoryService, PictureService pictureService, UserService userService) {
        this.directoryService = directoryService;
        this.pictureService = pictureService;
        this.userService = userService;
    }

    /**
     * 获取图片列表
     * @param dirid
     * @return
     */
    @GetMapping(value = "/getpicturelist")
    @ApiOperation("获取图片列表")
    public Message<List<Picture>> getPictureList(@RequestParam("dirid") long dirid) {
        Message<List<Picture>> msg = new Message<>();
        List<Picture> pictures = pictureService.findByDirId(dirid);
        pictureService.clearLazyField(pictures);
        pictures.forEach(picture -> picture.setPath(picture.getPath()));
        msg.setBody(pictures);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    /**
     * 上传图片
     * @param dirid
     * @param path
     * @param session
     * @return
     */
    @PostMapping(value = "/uploadpicture")
    @ApiOperation("上传图片")
    public Message<String> uploadPicture(@RequestParam("dirid") long dirid, @RequestParam("path") String path) {
    		Message<String> msg = new Message<String>();
    		Directory dir = directoryService.findById(dirid);
    		Picture p = new Picture();
    		p.setDirectory(dir);
    		p.setPath(path);
    		pictureService.save(p);
    		msg.setResCode(ResCode.SUCCESS);
    		return msg;
    }

    /*
     * 上传图片，如果相册不存在则创建相册
     * TODO 应该是按照id传图片
     * TEST
     */
    @PostMapping(value = "/uploadpictureByName")
    @ApiOperation("上传图片，如果相册不存在则创建相册")
    public Message<String> uploadPicture(@RequestParam("path") String path,
                                         @RequestParam("dirName") String dirName,
                                         @ApiIgnore HttpSession session) {
        Message<String> msg = new Message<String>();
        Long uid = (session.getAttribute("user"))!=null?((User) session.getAttribute("user")).getUid(): User.builder().uid(1).build().getUid();
        // TODO: NEED SEARCH USER IN DATABASE?
        User dbUser = userService.findById(uid);
        Directory directory = directoryService.findByUserAndName(uid, dirName);
        if (directory == null) {
            directory = new Directory();
            directory.setName(dirName);
            directory.setUser(dbUser);
            directoryService.save(directory);
        }
        Picture picture = new Picture();
        picture.setPath(path);
        picture.setDirectory(directory);
        pictureService.save(picture);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    /*
     * 更新图片目录
     * TEST
     */
    @PostMapping(value = "/updatepicture")
    @ApiOperation("更新图片目录")
    public Message<String> updatePicture(Picture picture) {
        Message<String> msg = new Message<>();
        pictureService.update(picture);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    /*
     * 删除图片
     * TEST
     */
    @DeleteMapping(value = "/deletepicture")
    @ApiOperation("删除图片")
    public Message<String> deletePicture(@RequestParam("picid") Integer picid) {
        Message<String> msg = new Message<>();
        pictureService.delete(picid);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }
    
    /**
     * 批量删除
     * @param picList
     * @return
     */
    @DeleteMapping(value = "/deletepicturelist")
    @ApiOperation("批量删除")
    public Message<String> deletePictureList(@RequestParam("piclist") ArrayList<Long> picList) {
    		Message<String> msg = new Message<>();
    		if (picList.size() > 0)
    			pictureService.deleteList(picList);
    		msg.setResCode(ResCode.SUCCESS);
    		return msg;
    }
}
