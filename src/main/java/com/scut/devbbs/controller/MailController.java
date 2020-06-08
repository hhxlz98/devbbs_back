package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.service.CommentService;
import com.scut.devbbs.service.MailService;
import com.scut.devbbs.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.scut.devbbs.util.CommonUtil;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MailService mailService;

    @GetMapping("/newMessageCount")
    public JSONObject newMessageCount(HttpServletRequest request) {
        return commentService.userNewMessageCount(CommonUtil.request2Json(request));
    }

    @GetMapping("/messageList")
    public JSONObject messageList(HttpServletRequest request) {
        return commentService.userMessageList(CommonUtil.request2Json(request));
    }

    @PostMapping("/sendMail")
    public JSONObject sendMail(@RequestBody JSONObject jsonObject) {
        return mailService.sendMail(jsonObject);
    }

    @PostMapping("/updateMail")
    public JSONObject updateMail(@RequestBody JSONObject jsonObject) {
        return mailService.updateMailState(jsonObject);
    }

    @GetMapping("/getUserMailList")
    public JSONObject getUserMailList(HttpServletRequest request) {
        return mailService.userMailList(CommonUtil.request2Json(request));
    }

    @GetMapping("/getSysMailList")
    public JSONObject getSysMailList(HttpServletRequest request) {
        return mailService.sysMailList(CommonUtil.request2Json(request));
    }

    @GetMapping("/sendedMailList")
    public JSONObject getSendedMailList(HttpServletRequest request) {
        return mailService.mySendedMailList(CommonUtil.request2Json(request));
    }

    @PostMapping("/updateMailFromDelete")
    public JSONObject updateMailFromDelete(@RequestBody JSONObject jsonObject) {
        return mailService.updateMailFromDelete(jsonObject);
    }

}
