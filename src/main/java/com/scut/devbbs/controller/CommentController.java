package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.scut.devbbs.util.CommonUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public JSONObject commentList(HttpServletRequest request) {
        return commentService.commentList(CommonUtil.request2Json(request));
    }

    @PostMapping("/addComment")
    public JSONObject addComment(@RequestBody JSONObject jsonObject) {
        return commentService.addComment(jsonObject);
    }

    @PostMapping("/likeComment")
    public JSONObject likeComment(@RequestBody JSONObject jsonObject) {
        return commentService.addCommentRelate(jsonObject);
    }

    @PostMapping("/unlikeComment")
    public JSONObject unlikeComment(@RequestBody JSONObject jsonObject) {
        return commentService.deleteCommentRelate(jsonObject);
    }

    @GetMapping("/deleteCommentList")
    public JSONObject deleteCommentList(HttpServletRequest request) {
        return commentService.userDeleteCommentList(CommonUtil.request2Json(request));
    }

    @PostMapping("/updateCommentState")
    public JSONObject updateCommentState(@RequestBody JSONObject jsonObject) {
        return commentService.updateCommentState(jsonObject);
    }


}
