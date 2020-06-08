package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentDao {

    //按照回复时间，列出普通回复
    List<JSONObject>listComment(@Param("postId")long postId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    //列出有助回复
    List<JSONObject>listHelpComment(@Param("postId")long postId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    int countHelpComment(@Param("postId")long postId);
    //列出最佳回复
    JSONObject bestComment(@Param("postId")long postId);

    //通过id查询回复
    JSONObject queryCommentById(@Param("commentId")long commentId);

    //给回复点赞、取消点赞
    void likeComment(@Param("commentId")long commentId, @Param("count")int count);

    //回复内容
    JSONObject commentContentById(@Param("commentId")long commentId);

    //添加回复
    void addComment(JSONObject jsonObject);

    //设最佳和热心回复
    void setHelpfulReply(@Param("commentId")long commentId);
    void setBestReply(@Param("commentId")long commentId);

    //用户统计
    int countCommentForUser(@Param("userId")long userId);
    int countBestForUser(@Param("userId")long userId);
    int commentLikeNumberForUser(@Param("userId")long userId);

    //用户评论列表, 状态区分删除等信息
    List<JSONObject> userCommentList(@Param("userId")long userId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize, @Param("state")int state);

    //更新评论状态
    void updateCommentState(@Param("commentId")long commentId, @Param("state")int state);

    //用户消息
    List<JSONObject> userMessageList(@Param("userId")long userId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    int countMessageList(@Param("userId")long userId);
    void updateCommentRead(@Param("userId")long userId);
}
