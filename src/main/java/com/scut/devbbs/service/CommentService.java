package com.scut.devbbs.service;

import com.alibaba.fastjson.JSONObject;

public interface CommentService {

    //添加评论
    JSONObject addComment(JSONObject jsonObject);

    //获得评论列表
    JSONObject commentList(JSONObject jsonObject);

    //增加评论上层信息、以及点赞
    JSONObject addUpInfo(JSONObject jsonObject, long userId);

    //点赞、取消点赞
    JSONObject addCommentRelate(JSONObject jsonObject);
    JSONObject deleteCommentRelate(JSONObject jsonObject);

    //用户评论列表
    JSONObject userCommentList(JSONObject jsonObject);
    JSONObject userDeleteCommentList(JSONObject jsonObject);

    //删除、恢复帖子等
    JSONObject updateCommentState(JSONObject jsonObject);

    //
    JSONObject userMessageList(JSONObject jsonObject);

    JSONObject userNewMessageCount(JSONObject jsonObject);
}
