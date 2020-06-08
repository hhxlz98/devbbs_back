package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.service.ReportService;
import com.scut.devbbs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/add")
    public JSONObject addReport(@RequestBody JSONObject jsonObject) {
        return reportService.responseUserReport(jsonObject);
    }

    @GetMapping("/reportList")
    public JSONObject reportListForPlate(HttpServletRequest request) {
        return reportService.reportListForPlate(CommonUtil.request2Json(request));
    }

    @GetMapping("/userReportList")
    public JSONObject userReportList(HttpServletRequest request) {
        return reportService.userReportList(CommonUtil.request2Json(request));
    }

    @PostMapping("/ignoreReport")
    public JSONObject ignoreReport(@RequestBody JSONObject jsonObject) {
        return reportService.ignoreReport(jsonObject);
    }

    @PostMapping("/dealReport")
    public JSONObject dealReport(@RequestBody JSONObject jsonObject) {
        return reportService.dealReport(jsonObject);
    }

    @PostMapping("/banUser")
    public JSONObject banUser(@RequestBody JSONObject jsonObject) {
        return reportService.banUser(jsonObject);
    }

    @GetMapping("/userPunishList")
    public JSONObject userPunishList(HttpServletRequest request) {
        return reportService.userPunishList(CommonUtil.request2Json(request), true);
    }

    @PostMapping("/cancelPunish")
    public JSONObject cancelPunish(@RequestBody JSONObject jsonObject) {
        return reportService.cancelPunish(jsonObject);
    }

}
