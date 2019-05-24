package com.ronghui.service.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.ronghui.service.entity.Directory;
import com.ronghui.service.entity.DirectoryResult;
import com.ronghui.server.files.network.ResCode;
import com.ronghui.server.network.Message;

import com.ronghui.service.entity.Picture;
import com.ronghui.service.entity.User;
import com.ronghui.service.service.DirectoryService;
import com.ronghui.service.service.PictureService;
import com.ronghui.service.service.UserService;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Slf4j
public class DirectoryController {

    private final UserService userService;
    private final DirectoryService directoryService;
    private final PictureService pictureService;

//    @Value("${zimg.uri-tmp}")
//    String zimgPath;

    @Autowired
    public DirectoryController(UserService userService, DirectoryService directoryService, PictureService pictureService) {
        this.userService = userService;
        this.directoryService = directoryService;
        this.pictureService = pictureService;
    }

    /*
     * 进入目录首页
     * TEST
     */
//    @RequestMapping(value = "/getdirectory", method = {RequestMethod.GET})
//    public String getDirectoryIndex() {
//        return "directory";
//    }

    /*
     * 获取用户目录列表
     * TEST
     */
    @GetMapping("/getdirectorylist")
    @ApiOperation("获取用户目录列表")
    public Message<List<DirectoryResult>> getDirectoryList(@RequestParam Long userId,@ApiIgnore HttpSession session) {
        Message<List<DirectoryResult>> msg = new Message<>();
        // TODO: NEED SEARCH USER IN DATABASE?
        List<Directory> directories = directoryService.findByUser(userId);
        ArrayList<DirectoryResult> directoryResults = new ArrayList<>();
        for (Directory dir : directories) {
            DirectoryResult dr = new DirectoryResult();
            List<Picture> pictures = pictureService.findByDirId(dir.getDirid());
            dr.setDirid(dir.getDirid());
            dr.setName(dir.getName());
            dr.setPicPath(null);
            dr.setPicCount(pictures.size());
            if (dr.getPicCount() != 0)
                dr.setPicPath(pictures.iterator().next().getPath());
            directoryResults.add(dr);
        }
        msg.setBody(directoryResults);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    /*
     * 创建目录
     * TEST
     */
    @PutMapping("/createdirectory")
    @ApiOperation("创建目录")
    public Message<String> createDirectory(@RequestParam Long userId,@RequestBody Directory directory, @ApiIgnore HttpSession session) {
        Message<String> msg = new Message<>();
        if (directory.getName() == null || StringUtils.isEmpty(directory.getName())) {
            log.debug("相册名称不能为空");
            msg.setResCode(ResCode.INTERNAL_ERROR);
            return msg;
        }
        // TODO: NEED SEARCH USER IN DATABASE?
        User dbUser = userService.findById(userId);
        directory.setUser(dbUser);
        directoryService.save(directory);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    /*
     * 通过相册名称查找id
     * TEST
     */
    @GetMapping("/getDirectoryByName")
    @ApiOperation("通过相册名称查找id")
    public Message<Directory> getDirectoryByName(@RequestParam Long userId,@RequestParam("name") String dirName, @ApiIgnore HttpSession session) {
        Message<Directory> msg = new Message<>();
        Directory directory = directoryService.findByUserAndName(userId, dirName);
        msg.setBody(directory);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    /*
     * 更新目录
     * TEST
     */
    @PostMapping("/updatedirectory")
    @ApiOperation("更新目录")
    public Message<String> updateDirectory(@RequestBody Directory directory) {
        Message<String> msg = new Message<>();
        directoryService.update(directory);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }

    /*
     * 删除目录
     * TEST
     */
    @DeleteMapping("/deletedirectory")
    @ApiOperation("删除目录")
    public Message<String> deleteDirectory(@RequestParam("dirid") long dirid) {
        Message<String> msg = new Message<String>();
        directoryService.delete(dirid);
        msg.setResCode(ResCode.SUCCESS);
        return msg;
    }
}
