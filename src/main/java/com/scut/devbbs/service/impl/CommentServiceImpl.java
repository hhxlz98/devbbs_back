package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.constants.ResponseEnum;
import com.scut.devbbs.dao.CommentDao;
import com.scut.devbbs.dao.CommentRelateDao;
import com.scut.devbbs.dao.PostDao;
import com.scut.devbbs.dao.UserDao;
import com.scut.devbbs.service.CommentService;
import com.scut.devbbs.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import com.scut.devbbs.util.CommonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    @Resource
    private CommentRelateDao commentRelateDao;

    @Resource
    private PostDao postDao;

    @Resource
    private UserDao userDao;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ParameterConstant parameterConstant;

    @Override
    public JSONObject addComment(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddCommentParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long postId = jsonObject.getLong("postId");
        JSONObject post = postDao.postContent(postId);
        if (post != null) {
            int floorNumber = post.getIntValue("replyNumber") + 1;
            jsonObject.put("floorNumber", floorNumber);
            jsonObject.put("best", false);
            jsonObject.put("helpful", false);
            jsonObject.put("likeNumber", 0);
            jsonObject.put("publishTime", System.currentTimeMillis());
            jsonObject.put("read", false);
            postDao.updatePostByComment(new Date().getTime(), postId);
            commentDao.addComment(jsonObject);
            return CommonUtil.commonReturn(ResponseEnum.E_602, jsonObject);
        } else {
            info = "帖子Id不存在：" + postId;
            return  CommonUtil.errorJson(info);
        }
    }

    @Override
    public JSONObject commentList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getCommentListParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long postId = jsonObject.getLong("postId");
        long userId = jsonObject.getLong("userId");
        JSONObject post = postDao.postContent(postId);
        if (post != null) {
            String tab = post.getString("tab");
            int currentPage = jsonObject.getIntValue("currentPage");
            int pageSize = jsonObject.getIntValue("pageSize");
            int currentIndex = (currentPage - 1) * pageSize;
            if (tab.equals("Share")) {
                List<JSONObject> commentList = commentDao.listComment(postId, currentIndex, pageSize);
                if (commentList != null) {
                    for (JSONObject comment:commentList) {
                        comment = addUpInfo(comment, userId);
                    }
                }
                for (JSONObject comment : commentList) {
                    long authorId = comment.getLong("authorId");
                    JSONObject author = userDao.queryUserById(authorId);
                    comment.put("author", author);
                }
                JSONObject resultJson = new JSONObject();
                resultJson.put("list", commentList);
                resultJson.put("total", post.getLongValue("replyNumber"));
                return CommonUtil.commonReturn(ResponseEnum.E_603, resultJson);
            } else if (tab.equals("Ask")){
                int restSize = pageSize;
                List<JSONObject> commentList = new ArrayList<JSONObject>();
                if (currentPage == 1 && post.getIntValue("status") == 1) {
                    JSONObject bestComment = commentDao.bestComment(postId);
                    if (bestComment != null) {
                        restSize--;
                        commentList.add(addUpInfo(bestComment, userId));
                    }
                }
                int helpStartIndex = currentIndex;
                if (currentPage > 1 && post.getIntValue("status") == 1) {
                    helpStartIndex--;
                }
                List<JSONObject> helpfulComments = commentDao.listHelpComment(postId, helpStartIndex, pageSize);
                if (helpfulComments != null) {
                    for (JSONObject comment : helpfulComments) {
                        restSize--;
                        comment = addUpInfo(comment, userId);
                    }
                    commentList.addAll(helpfulComments);
                }
                if (restSize > 0) {
                    List<JSONObject> comments = new ArrayList<JSONObject>();
                    if (restSize == pageSize) {
                        int helpfulCount = commentDao.countHelpComment(postId);
                        if (post.getIntValue("status") == 1) {
                            helpfulCount++;
                        }
                        comments = commentDao.listComment(postId, currentIndex - helpfulCount, restSize);
                    } else {
                        comments = commentDao.listComment(postId, 0, restSize);
                    }
                    if (comments != null) {
                        for (JSONObject comment : comments) {
                            comment = addUpInfo(comment, userId);
                        }
                        commentList.addAll(comments);
                    }

                }
                for (JSONObject comment : commentList) {
                    long authorId = comment.getLong("authorId");
                    JSONObject author = userDao.queryUserById(authorId);
                    comment.put("author", author);
                }
                JSONObject resultJson = new JSONObject();
                resultJson.put("list", commentList);
                resultJson.put("total", post.getLongValue("replyNumber"));
                return CommonUtil.commonReturn(ResponseEnum.E_603, resultJson);
            }
        }

        info = "帖子Id不存在：" + postId;
        return  CommonUtil.errorJson(info);
    }

    @Override
    public JSONObject addUpInfo(JSONObject jsonObject, long userId) {
        String info = "";
        long upId = jsonObject.getLong("upId");
        if (upId > 0) {
            JSONObject upComment = commentDao.queryCommentById(upId);
            if (upComment != null) {
                long authorId = upComment.getLong("authorId");
                JSONObject user = userDao.queryUserById(authorId);
                if (user != null) {
                    String upInfo = user.getString("userName") + "(" + upComment.getIntValue("floorNumber") + "L)";
                    jsonObject.put("upInfo", upInfo);
                } else {
                    info = "用户找不到id：" + authorId;
                    return CommonUtil.errorJson(info);
                }
            } else {
                info = "上级评论id不存在：" + upId;
                return CommonUtil.errorJson(info);
            }
        }
        if (jsonObject.getIntValue("likeNumber") > 0 && userId > 0) {
            JSONObject queryRelate = new JSONObject();
            queryRelate.put("userId", userId);
            queryRelate.put("commentId", jsonObject.getLongValue("commentId"));
            queryRelate.put("type", 0);
            if (commentRelateDao.existCommentRelate(queryRelate) != null) {
                jsonObject.put("isFollow", true);
            } else {
                jsonObject.put("isFollow", false);
            }
        } else {
            jsonObject.put("isFollow", false);
        }
        return jsonObject;
    }

    @Override
    public JSONObject addCommentRelate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getCommentRelateParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        JSONObject queryRelate = commentRelateDao.existCommentRelate(jsonObject);
        if (queryRelate == null) {
            commentRelateDao.addCommentRelate(jsonObject);
            if (jsonObject.getIntValue("type") == 0){
                commentDao.likeComment(jsonObject.getLong("commentId"), 1);
            }
            return CommonUtil.commonReturn(ResponseEnum.E_604, info);
        } else {
            info = "已存在：" + jsonObject;
            return CommonUtil.errorJson(info);
        }
    }

    @Override
    public JSONObject deleteCommentRelate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getCommentRelateParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        JSONObject queryRelate = commentRelateDao.existCommentRelate(jsonObject);
        if (queryRelate != null) {
            commentRelateDao.deleteCommentRelate(jsonObject);
            if (jsonObject.getIntValue("type") == 0){
                commentDao.likeComment(jsonObject.getLong("commentId"), -1);
            }
            return CommonUtil.commonReturn(ResponseEnum.E_605, info);
        } else {
            info = "删除失败。不存在：" + jsonObject;
            return CommonUtil.errorJson(info);
        }
    }

    @Override
    public JSONObject userCommentList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUserListParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        JSONObject resultJson = new JSONObject();
        long userId = jsonObject.getLong("userId");
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        List<JSONObject> commentList = commentDao.userCommentList(userId, currentIndex, pageSize, 0);
        resultJson.put("list", commentList);
        resultJson = commonService.addInfoForPaging(jsonObject, commentList.size(), resultJson);
        return CommonUtil.commonReturn(ResponseEnum.E_609, resultJson);
    }

    @Override
    public JSONObject userDeleteCommentList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUserListParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        JSONObject resultJson = new JSONObject();
        long userId = jsonObject.getLong("userId");
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        List<JSONObject> commentList = commentDao.userCommentList(userId, currentIndex, pageSize, 1);
        resultJson.put("list", commentList);
        resultJson = commonService.addInfoForPaging(jsonObject, commentList.size(), resultJson);
        return CommonUtil.commonReturn(ResponseEnum.E_609, resultJson);
    }

    @Override
    public JSONObject updateCommentState(JSONObject jsonObject) {
        long commentId = jsonObject.getLong("commentId");
        int state = jsonObject.getIntValue("state");
        if (commentId > 0) {
            commentDao.updateCommentState(commentId, state);
            return CommonUtil.commonReturn(ResponseEnum.E_612, jsonObject);
        } else {
            return CommonUtil.errorJson("commentId不合法");
        }
    }

    @Override
    public JSONObject userMessageList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUserMessageListParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        JSONObject resultJson = new JSONObject();
        long userId = jsonObject.getLong("userId");
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        List<JSONObject> messageList = commentDao.userMessageList(userId, currentIndex, pageSize);
        resultJson.put("list", messageList);
        int currentMessageCount = commentDao.countMessageList(userId);
        resultJson.put("newCount", currentMessageCount);
        resultJson = commonService.addInfoForPaging(jsonObject, messageList.size(), resultJson);
        System.out.println("请求message");
        System.out.println(jsonObject);
        System.out.println(resultJson);

        return CommonUtil.commonReturn(ResponseEnum.E_610, resultJson);
    }

    @Override
    public JSONObject userNewMessageCount(JSONObject jsonObject) {
        long userId = jsonObject.getIntValue("userId");
        if (userId > 0) {
            int currentMessageCount = commentDao.countMessageList(userId);
            commentDao.updateCommentRead(userId);
            return CommonUtil.commonReturn(ResponseEnum.E_611, currentMessageCount);
        }
        return CommonUtil.errorJson("userId不合法");
    }
}
