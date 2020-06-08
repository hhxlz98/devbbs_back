package com.scut.devbbs.service;

import com.alibaba.fastjson.JSONObject;

public interface PostService {

    // 新增帖子
    JSONObject addPost(JSONObject jsonObject);

    //帖子列表
    JSONObject postList(JSONObject jsonObject);

    //帖子内容
    JSONObject postContent(JSONObject jsonObject);

    //帖子状态（ask帖子）

    //点赞
    JSONObject addPostRelate(JSONObject jsonObject);
    JSONObject deletePostRelate(JSONObject jsonObject);

    //分配悬赏
    JSONObject decideBestAnswer(JSONObject jsonObject);
    JSONObject decideHelpfulAnswer(JSONObject jsonObject);

    //用户帖子列表
    JSONObject userPostList(JSONObject jsonObject);
    JSONObject userDeletePostList(JSONObject jsonObject);

    //更新帖子状态
    JSONObject updatePostState(JSONObject jsonObject);
    //更新通知内容
    JSONObject updatePostContent(JSONObject jsonObject);

    //搜索帖子
    JSONObject getSearchPostList(JSONObject jsonObject);

    //发帖数据
    JSONObject getPublishInfo(JSONObject jsonObject);

    //最新帖子
    JSONObject getLatestPost();

    //最热帖子
    JSONObject getHotPost();
}
