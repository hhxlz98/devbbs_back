package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface PostDao {

    //增加新帖子
    void addPost(JSONObject jsonObject);

    //帖子列表
    List<JSONObject> allPostList(@Param("plateId")long plateId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    //tab帖子列表
    List<JSONObject> tabPostList(@Param("plateId")long plateId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize,@Param("tab")String tab);

    //置顶帖子理论上肯定不会超过1页
    List<JSONObject> topPostList(@Param("plateId")long plateId);

    //精华帖子
    List<JSONObject> goodPostList(@Param("plateId")long plateId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    //板块的所有帖子数
    int countPost(@Param("plateId")long plateId);
    int countTabPost(@Param("plateId")long plateId, @Param("tab")String tab);
    int countGoodPost(@Param("plateId")long plateId);

    //某一帖子内容
    JSONObject postContent(@Param("postId")long postId);

    //根据postId获取postContent、作者
    JSONObject postContentByPostId(@Param("postId")long postId);

    //更新帖子
    //状态，针对ask
    void updatePostStatus(@Param("postId")long postId, @Param("status")int status);
    //点赞数，针对share
    void updatePostLike(@Param("postId")long posId, @Param("num")int num);
    //通用
    //访问数
    void updatePostVisitNumber(@Param("postId")long postId);
    //回复时间、回复数
    void updatePostByComment(@Param("replyTime")long replyTime, @Param("postId")long postId);
    //针对ask，热心数
    void updatePostHelpfulNumber(@Param("postId")long postId, @Param("num")int num);

    //用户统计
    //
    int countPostForUser(@Param("userId")long userId);
    int countGoodForUser(@Param("userId")long userId);
    int postLikeNumberForUser(@Param("userId")long userId);

    List<JSONObject> userPostList(@Param("userId")long userId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize, @Param("state")int state);

    //更新帖子状态
    void updatePostState(@Param("postId")long postId, @Param("state")int state);
    //更新帖子内容
    void updatePostContent(@Param("postId")long postId, @Param("content")String content, @Param("editTime")long editTime);

    //搜索帖子
    List<JSONObject> searchPostListSortByPublishOnTitle(@Param("searchString")String searchString, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    List<JSONObject> searchPostListSortByReplyOnTitle(@Param("searchString")String searchString, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    List<JSONObject> searchPostListSortByPublishOnContent(@Param("searchString")String searchString, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    List<JSONObject> searchPostListSortByReplyOnContent(@Param("searchString")String searchString, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    //最新帖子
    List<JSONObject> selectLatestPost();
    //最热帖子
    List<JSONObject> selectHotPost();
}
