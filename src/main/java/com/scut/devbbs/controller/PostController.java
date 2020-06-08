package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.scut.devbbs.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/new")
    public JSONObject newPosting(@RequestBody JSONObject requestJson) {
        return postService.addPost(requestJson);
    }

    @GetMapping("/list")
    public JSONObject postList(HttpServletRequest request) {
        return postService.postList(CommonUtil.request2Json(request));
    }

    @GetMapping("/post")
    public JSONObject postContent(HttpServletRequest request) {
        return postService.postContent(CommonUtil.request2Json(request));
    }

    @PostMapping("/addRelate")
    public JSONObject postRelateAdd(@RequestBody JSONObject requestJson) {
        return postService.addPostRelate(requestJson);
    }

    @PostMapping("/deleteRelate")
    public JSONObject postRelateDelete(@RequestBody JSONObject requestJson) {
        return postService.deletePostRelate(requestJson);
    }

    @PostMapping("/rewardAssign")
    public JSONObject postRewardAssign(@RequestBody JSONObject requestJson) {
        int type = requestJson.getIntValue("type");
        if (type == 0) {
            return postService.decideBestAnswer(requestJson);
        } else {
            return postService.decideHelpfulAnswer(requestJson);
        }
    }

    @PostMapping("/updateState")
    public JSONObject updatePostState(@RequestBody JSONObject jsonObject) {
        return postService.updatePostState(jsonObject);
    }

    @PostMapping("/updateContent")
    public JSONObject updatePostContent(@RequestBody JSONObject jsonObject) {
        return postService.updatePostContent(jsonObject);
    }

    @GetMapping("/userDeletePostList")
    public JSONObject userDeletePostList(HttpServletRequest request) {
        return postService.userDeletePostList(CommonUtil.request2Json(request));
    }

    @GetMapping("/searchPost")
    public JSONObject seachPost(HttpServletRequest request) {
        return postService.getSearchPostList(CommonUtil.request2Json(request));
    }

    @GetMapping("/publishInfo")
    public JSONObject publishInfo(HttpServletRequest request) {
        return postService.getPublishInfo(CommonUtil.request2Json(request));
    }

    @GetMapping("/hotPost")
    public JSONObject homeHotPost(HttpServletRequest request) {
        return postService.getHotPost();
    }

    @GetMapping("/latestPost")
    public JSONObject homeLatestPost(HttpServletRequest request) {
        return postService.getLatestPost();
    }
}
