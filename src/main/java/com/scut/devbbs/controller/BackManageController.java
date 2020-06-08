package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.dao.NewsDao;
import com.scut.devbbs.service.CommonService;
import com.scut.devbbs.service.PlateService;
import com.scut.devbbs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/backManage")
public class BackManageController {

    @Autowired
    private PlateService plateService;

    @Autowired
    private CommonService commonService;

    @GetMapping("/plateList")
    public JSONObject getPlateList(HttpServletRequest request) {
        return plateService.allPlateList(CommonUtil.request2Json(request));
    }

    @PostMapping("/updatePlateInfo")
    public JSONObject updatePlateInfo(@RequestBody JSONObject jsonObject) {
        return plateService.updatePlateInfo(jsonObject);
    }

    @PostMapping("/changePlateState")
    public JSONObject deletePlate(@RequestBody JSONObject jsonObject) {
        return plateService.updatePlateState(jsonObject);
    }

    @PostMapping("/plateNameExist")
    public boolean plateNameExist(@RequestBody JSONObject jsonObject) {
        return plateService.existPlateName(jsonObject);
    }

    @PostMapping("/addPlate")
    public JSONObject addPlate(@RequestBody JSONObject requestJson) {
        return plateService.addPlate(requestJson);
    }

    @GetMapping("/plateManageList")
    public JSONObject plateManageList() {
        return plateService.plateAddManageList();
    }

    @GetMapping("/newsList")
    public JSONObject allNewsList() {
        return commonService.allNewsList();
    }

    @GetMapping("/homeNewsList")
    public JSONObject homeNewsList() {
        return commonService.homeNewsList();
    }

    @PostMapping("/addNews")
    public JSONObject addNews(@RequestBody JSONObject requestJson) {
        return commonService.addNews(requestJson);
    }

    @PostMapping("/updateNews")
    public JSONObject updateNews(@RequestBody JSONObject requestJson) {
        return commonService.updateNews(requestJson);
    }

    @PostMapping("/updateNewsValid")
    public JSONObject updateNewsValid(@RequestBody JSONObject requestJson) {
        return commonService.updateNewsValid(requestJson);
    }

    @PostMapping("/deleteNews")
    public JSONObject deleteNews(@RequestBody JSONObject requestJson) {
        return commonService.deleteNews(requestJson);
    }

}
