package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.service.SocialService;
import com.scut.devbbs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/social")
public class SocialController {

    @Autowired
    private SocialService socialService;

    @PostMapping("/addApply")
    public JSONObject addFriendApply(@RequestBody JSONObject jsonObject) {
        return socialService.addFriendApply(jsonObject);
    }

    @PostMapping("/updateApply")
    public JSONObject updateFriendApply(@RequestBody JSONObject jsonObject) {
        return socialService.updateFriendApply(jsonObject);
    }

    @GetMapping("/getMyApply")
    public JSONObject getMyApplyList(HttpServletRequest request) {
        return socialService.friendApplyListForApply(CommonUtil.request2Json(request));
    }

    @GetMapping("/getApplied")
    public JSONObject getAppliedList(HttpServletRequest request) {
        return socialService.friendApplyListForApplied(CommonUtil.request2Json(request));
    }
}
