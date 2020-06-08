package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.annotation.AuthToken;
import com.scut.devbbs.service.CommentService;
import com.scut.devbbs.service.PostService;
import com.scut.devbbs.service.SocialService;
import com.scut.devbbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.scut.devbbs.util.CommonUtil;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SocialService socialService;

    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject requestJson) {
        return  userService.userLogin(requestJson);
    }

    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject requestJson) {
        return userService.registerUser(requestJson);
    }

    @GetMapping("/info")
    public JSONObject userInfo(HttpServletRequest request) {
        return userService.userInfo(CommonUtil.request2Json(request));
    }

    @PostMapping("/alterUserName")
    public JSONObject alterUserName(@RequestBody JSONObject requestJson) {
        return userService.updateUserName(requestJson);
    }

    @PostMapping("/existUserName")
    public boolean existUserName(@RequestBody JSONObject requestJson) {
        return userService.usernameRepeat(requestJson.getString("userName"));
    }

    @PostMapping("/alterUserShow")
    public JSONObject alterUserShow(@RequestBody JSONObject requestJson) {
        return userService.updateUserShow(requestJson);
    }

    @PostMapping("/updateUserInfo")
    public JSONObject updateUserInfo(@RequestBody JSONObject requestJson) {
        return userService.updateUserInfo(requestJson);
    }

    @GetMapping("/userPostList")
    public JSONObject getUserPostList(HttpServletRequest request) {
        return postService.userPostList(CommonUtil.request2Json(request));
    }

    @GetMapping("/userCommentList")
    public JSONObject getUserCommentList(HttpServletRequest request) {
        return commentService.userCommentList(CommonUtil.request2Json(request));
    }

    @GetMapping("/userList")
    public JSONObject getUserList(HttpServletRequest request) {
        return socialService.userRelateList(CommonUtil.request2Json(request));
    }

    @PostMapping("/addUserRelate")
    public JSONObject addUserRelate(@RequestBody JSONObject jsonObject) {
        return socialService.addUserRelate(jsonObject);
    }

    @PostMapping("/deleteUserRelate")
    public JSONObject deleteUserRelate(@RequestBody JSONObject jsonObject) {
        return  socialService.deleteUserRelate(jsonObject);
    }

    @GetMapping("/queryUser")
    public JSONObject queryUser(HttpServletRequest request) {
        return userService.queryUser(CommonUtil.request2Json(request));
    }

    @GetMapping("/test")
    @AuthToken
    public JSONObject testToken(HttpServletRequest request) {
        return CommonUtil.request2Json(request);
    }


}
