package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.dao.PlateDao;
import com.scut.devbbs.service.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.scut.devbbs.util.CommonUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/plate")
public class PlateController {

    @Autowired
    private PlateService plateService;

    @GetMapping("/allList")
    public JSONObject allListPlate(HttpServletRequest request) {
        return plateService.listPlate(CommonUtil.request2Json(request));
    }

    @GetMapping("/myList")
    public JSONObject myListPlate(HttpServletRequest request) {
        return plateService.myListPlate(CommonUtil.request2Json(request));
    }

    @GetMapping("/info")
    public JSONObject getPlateInfo(HttpServletRequest request) {
        return plateService.plateInfo(CommonUtil.request2Json(request));
    }

    @PostMapping("/addRelatePlate")
    public JSONObject addRelatePlate(@RequestBody JSONObject requestJson) {
        return plateService.addPlateRelate(requestJson);
    }

    @PostMapping("/deleteRelatePlate")
    public JSONObject deleteRelatePlate(@RequestBody JSONObject requestJson) {
        return plateService.deletePlateRelate(requestJson);
    }






}
