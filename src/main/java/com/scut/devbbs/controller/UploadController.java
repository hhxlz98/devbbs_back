package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ResponseEnum;
import com.scut.devbbs.service.CommonService;
import com.scut.devbbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.scut.devbbs.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public JSONObject infoMessage(){
        JSONObject resultJson = new JSONObject();
        UUID randomUUID = UUID.randomUUID();
        System.out.println(randomUUID);
        resultJson.put("data", randomUUID);
        return resultJson;
    }

    @PostMapping("/userImg")
    public JSONObject uploadUserImg(@RequestParam("file") MultipartFile file, HttpServletRequest request)throws Exception {
        String path = "D:\\devbbsProjectFile\\userImg\\";
        JSONObject req = CommonUtil.request2Json(request);
        long userId = req.getLong("userId");
        String originalName = file.getOriginalFilename();
        String extensionName = originalName
                .substring(originalName.lastIndexOf(".") + 1);
        String fileName = userId + "-" + new Date().getTime() + '.' + extensionName;
        ResponseEnum responseEnum = commonService.uploadImg(file, path, fileName);
//        commonService.imageToSquare(path + fileName, extensionName);
        if (responseEnum == ResponseEnum.E_506) {
            String imgUrl = "/userImg/" + fileName;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("imgUrl", imgUrl);
            userService.updateUserImg(jsonObject);
            return CommonUtil.commonReturn(responseEnum, request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + imgUrl);
        } else {
            return CommonUtil.commonReturn(responseEnum, "出现错误");
        }
    }

    @PostMapping("/plateImg")
    public JSONObject uploadPlateImage(@RequestParam("file") MultipartFile file, HttpServletRequest request)throws Exception {
        String path = "D:\\devbbsProjectFile\\plateImg\\";
        JSONObject req = CommonUtil.request2Json(request);
        String plateName = req.getString("plateName");
        String originalName = file.getOriginalFilename();
        String extensionName = originalName
                .substring(originalName.lastIndexOf(".") + 1);
        String fileName = plateName + "-" + new Date().getTime() + '.' + extensionName;
        ResponseEnum responseEnum = commonService.uploadImg(file, path, fileName);
        if (responseEnum == ResponseEnum.E_506) {
            String imgUrl = "/plateImg/" + fileName;
            return CommonUtil.commonReturn(responseEnum, imgUrl);
        } else {
            return CommonUtil.commonReturn(responseEnum, "出现错误");
        }
    }

    @PostMapping("/pic")
    public JSONObject uploadImageFromArticle(@RequestParam("file") MultipartFile file, HttpServletRequest request)throws Exception {
        String path = "D:\\devbbsProjectFile\\pic\\";
        String originalName = file.getOriginalFilename();
        String extensionName = originalName
                .substring(originalName.lastIndexOf(".") + 1);
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + extensionName;
        ResponseEnum responseEnum = commonService.uploadImg(file, path, fileName);
        if (responseEnum == ResponseEnum.E_506) {
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/pic/" + fileName;
            return CommonUtil.imgUploadSuccess(url);
        } else {
            return CommonUtil.commonReturn(responseEnum, "出现错误");
        }
    }

    @PostMapping("/newsImg")
    public JSONObject uploadImageForNews(@RequestParam("file") MultipartFile file, HttpServletRequest request)throws Exception {
        String path = "D:\\devbbsProjectFile\\newsImg\\";
        String originalName = file.getOriginalFilename();
        String originalFileName = originalName
                .substring(0, originalName.lastIndexOf("."));
        String extensionName = originalName
                .substring(originalName.lastIndexOf(".") + 1);
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        String fileName = originalFileName + ft.format(dNow) + "." + extensionName;
        ResponseEnum responseEnum = commonService.uploadImg(file, path, fileName);
        if (responseEnum == ResponseEnum.E_506) {
            String imgUrl = "/newsImg/" + fileName;
            return CommonUtil.commonReturn(responseEnum, imgUrl);
        } else {
            return CommonUtil.commonReturn(responseEnum,"出现错误");
        }
    }
}
