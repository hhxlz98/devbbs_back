package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.Constant;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.constants.ResponseEnum;
import com.scut.devbbs.dao.*;
import com.scut.devbbs.service.CommonService;
import com.scut.devbbs.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.scut.devbbs.util.CommonUtil;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Resource
    private PostDao postDao;

    @Resource
    private UserDao userDao;

    @Resource
    private PlateDao plateDao;

    @Resource
    private PostRelateDao postRelateDao;

    @Resource
    private CommentDao commentDao;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ParameterConstant parameterConstant;

    @Override
    public JSONObject addPost(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddPostParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long publishTime = System.currentTimeMillis();
        jsonObject.put("publishTime", publishTime);
        jsonObject.put("replyTime", publishTime);
        jsonObject.put("good", false);
        jsonObject.put("top", false);
        jsonObject.put("replyNumber", 0);
        jsonObject.put("visitNumber", 1);
        //share
        jsonObject.put("likeNumber", 0);

        //ask
        jsonObject.put("status", 0);
        System.out.println(jsonObject);
        plateDao.updatePlatePostNumber(jsonObject.getLong("plateId"), 1);
        postDao.addPost(jsonObject);
        log.info("插入帖子{}", jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_402, jsonObject);
    }

    @Override
    public JSONObject postList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getPostListParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        String tab = jsonObject.getString("tab");
        long plateId = jsonObject.getLong("plateId");
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        JSONObject resultJson = new JSONObject();
        List<JSONObject> postList = new ArrayList<JSONObject>();
        int total = 0;
        if (tab.equals("All")) {
            if (currentPage == 1) {
                postList = postDao.topPostList(plateId);
                if (postList == null) {
                    postList = postDao.allPostList(plateId, currentIndex, pageSize);
                } else {
                    postList.addAll(postDao.allPostList(plateId, currentIndex, pageSize));
                }
                total = postDao.countPost(plateId);
            } else {
                total = postDao.countPost(plateId);
                postList = postDao.allPostList(plateId, currentIndex, pageSize);
            }
        } else if (tab.equals("Good")) {
            total = postDao.countGoodPost(plateId);
            postList = postDao.goodPostList(plateId, currentIndex, pageSize);
        } else {
            total = postDao.countTabPost(plateId, tab);
            postList = postDao.tabPostList(plateId, currentIndex, pageSize, tab);
        }
        for (JSONObject post : postList) {
            long userId = post.getLong("authorId");
            String userName = userDao.queryUserById(userId).getString("userName");
            post.put("authorName", userName);

        }
        resultJson.put("list", postList);
        resultJson.put("total", total);
        return CommonUtil.commonReturn(ResponseEnum.E_403, resultJson);
    }

    @Override
    public JSONObject postContent(JSONObject jsonObject) {
        if (jsonObject.getLong("postId") == null) {
            return CommonUtil.returnParameterErrorInfo(new String[]{"postId"});
        }
        long postId = jsonObject.getLong("postId");
        if (postId > 0){
            JSONObject resultJson = postDao.postContent(postId);
            long authorId = resultJson.getLong("authorId");
            JSONObject author = userDao.queryUserById(authorId);
            resultJson.put("author", author);
            String tab = resultJson.getString("tab");
            if (tab.equals("Ask")) {
                int oldStatus = resultJson.getIntValue("status");
                if (oldStatus == 0) {
                    long currentTime = System.currentTimeMillis();
                    int lastDay = resultJson.getIntValue("lastTime");
                    long publishTime = resultJson.getLong("publishTime");
                    if (publishTime + lastDay * Constant.DAY_TIMESTAMP < currentTime) {
                        postDao.updatePostStatus(postId, 2);
                        resultJson.put("status", 2);
                    } else {
                        long restTime = publishTime + lastDay * Constant.DAY_TIMESTAMP - currentTime;
                        resultJson.put("restTime", restTime);
                    }
                }
            } else if (tab.equals("Share")) {
                long userId = jsonObject.getLong("userId");
                if ( userId > 0) {
                    JSONObject queryJson = new JSONObject();
                    queryJson.put("userId", userId);
                    queryJson.put("postId", postId);
                    JSONObject queryRelate = postRelateDao.existPostRelate(queryJson);
                    if (queryRelate == null) {
                        resultJson.put("isEvaluated", false);
                        resultJson.put("isUp", false);
                    } else {
                        resultJson.put("isEvaluated", true);
                        int type = queryRelate.getIntValue("type");
                        if (type == 0) {
                            resultJson.put("isUp", true);
                        } else {
                            resultJson.put("isUp", false);
                        }
                    }
                } else {
                    resultJson.put("isEvaluated", false);
                    resultJson.put("isUp", false);
                }
            }
            postDao.updatePostVisitNumber(postId);
            return CommonUtil.commonReturn(ResponseEnum.E_406, resultJson);
        } else {
            String info = "postId不合法" + postId;
            return CommonUtil.errorJson(info);
        }
    }

    @Override
    public JSONObject addPostRelate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getPostRelateParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        if (postRelateDao.existPostRelate(jsonObject) == null) {
            postRelateDao.addPostRelate(jsonObject);
            int type = jsonObject.getIntValue("type");
            long postId = jsonObject.getLong("postId");
            if (type == 0) {
                postDao.updatePostLike(postId, 1);
            } else if (type == 1) {
                postDao.updatePostLike(postId, -1);
            }
            return CommonUtil.commonReturn(ResponseEnum.E_408, info);
        } else {
            return CommonUtil.commonReturn(ResponseEnum.E_409, info);
        }

    }

    @Override
    public JSONObject deletePostRelate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getPostRelateParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        System.out.println(jsonObject);
        if (postRelateDao.existPostRelate(jsonObject) != null) {
            int type = jsonObject.getIntValue("type");
            long postId = jsonObject.getLong("postId");
            if (type == 0) {
                postDao.updatePostLike(postId, -1);
            } else if (type == 1) {
                postDao.updatePostLike(postId, 1);
            }
            postRelateDao.deletePostRelate(jsonObject);
            return CommonUtil.commonReturn(ResponseEnum.E_407, info);
        } else {
            return CommonUtil.commonReturn(ResponseEnum.E_410,jsonObject);
        }


    }

    @Override
    public JSONObject decideBestAnswer(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getRewardAssignParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        int type = jsonObject.getIntValue("type");
        if (type == 0) {
            long postId = jsonObject.getLong("postId");
            JSONObject post = postDao.postContent(postId);
            if (post == null) {
                info = "postId不存在：" + postId;
                return CommonUtil.errorJson(info);
            }
            int helpfulNumber = post.getIntValue("helpfulNumber");
            if ( helpfulNumber > 0) {
                List<JSONObject> commentList = commentDao.listComment(postId,0, helpfulNumber);
                for (JSONObject comment: commentList) {
                    commentDao.setHelpfulReply(comment.getLong("commentId"));
                }
            }
            commentDao.setBestReply(jsonObject.getLong("commentId"));
            postDao.updatePostStatus(jsonObject.getLong("postId"), 1);
            return CommonUtil.commonReturn(ResponseEnum.E_606, jsonObject);
        }
        return null;
    }

    @Override
    public JSONObject decideHelpfulAnswer(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getRewardAssignParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        int type = jsonObject.getIntValue("type");
        if (type == 1) {
            long postId = jsonObject.getLong("postId");
            JSONObject post = postDao.postContent(postId);
            if (post == null) {
                info = "postId不存在：" + postId;
                return CommonUtil.errorJson(info);
            }
            int helpfulNumber = post.getIntValue("helpfulNumber");
            if (helpfulNumber > 0) {
                commentDao.setHelpfulReply(jsonObject.getLong("commentId"));
                postDao.updatePostHelpfulNumber(postId, -1);
                CommonUtil.commonReturn(ResponseEnum.E_607, jsonObject);
            } else {
                CommonUtil.commonReturn(ResponseEnum.E_608, jsonObject);
            }
        }
        return null;
    }

    @Override
    public JSONObject userPostList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUserListParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        JSONObject resultJson = new JSONObject();
        long userId = jsonObject.getLong("userId");
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        List<JSONObject> postList = postDao.userPostList(userId, currentIndex, pageSize, 0);
        resultJson.put("list", postList);
        resultJson = commonService.addInfoForPaging(jsonObject, postList.size(), resultJson);
        return CommonUtil.commonReturn(ResponseEnum.E_411, resultJson);
    }

    @Override
    public JSONObject userDeletePostList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUserListParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        JSONObject resultJson = new JSONObject();
        long userId = jsonObject.getLong("userId");
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        List<JSONObject> postList = postDao.userPostList(userId, currentIndex, pageSize, 1);
        resultJson.put("list", postList);
        resultJson = commonService.addInfoForPaging(jsonObject, postList.size(), resultJson);
        return CommonUtil.commonReturn(ResponseEnum.E_411, resultJson);
    }

    @Override
    public JSONObject updatePostState(JSONObject jsonObject) {
        long postId = jsonObject.getLong("postId");
        if (postId > 0) {
            postDao.updatePostState(postId, jsonObject.getIntValue("state"));
            return CommonUtil.commonReturn(ResponseEnum.E_412, jsonObject);
        } else {
            return CommonUtil.errorJson("postId 不合法");
        }
    }

    @Override
    public JSONObject updatePostContent(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUpdatePostContentParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long userId = jsonObject.getLong("userId");
        long postId = jsonObject.getLong("postId");
        if (userId > 0 && postId > 0) {
            JSONObject post = postDao.postContent(postId);
            if (post.getLong("authorId") == userId) {
                postDao.updatePostContent(postId, jsonObject.getString("content"), System.currentTimeMillis());
            } else {
                log.warn("用户{}不是帖子【{}来自用户{}】的作者", userId, postId, post.getLong("authorId"));
                CommonUtil.errorJson("无权限");
            }
        } else {
            log.warn("修改帖子用户Id参数{}，帖子Id参数{}不合法", userId, postId);
            CommonUtil.errorJson("参数异常");
        }
        return CommonUtil.commonReturn(ResponseEnum.E_415, jsonObject);
    }

    @Override
    public JSONObject getSearchPostList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getSearchPostParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        String searchString = jsonObject.getString("searchString");
        String searchType = jsonObject.getString("searchType");
        String sortType = jsonObject.getString("sortType");
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        JSONObject resultJson = new JSONObject();
        List<JSONObject> postList = new ArrayList<JSONObject>();
        if (searchType.equals("1")) {
            if (sortType.equals("发布时间")) {
                postList = postDao.searchPostListSortByPublishOnTitle(searchString, currentIndex, pageSize);
            } else if (sortType.equals("回复时间")) {
                postList = postDao.searchPostListSortByReplyOnTitle(searchString, currentIndex, pageSize);
            }
        } else {
            if (sortType.equals("发布时间")) {
                postList = postDao.searchPostListSortByPublishOnContent(searchString, currentIndex, pageSize);
            } else if (sortType.equals("回复时间")) {
                postList = postDao.searchPostListSortByReplyOnContent(searchString, currentIndex, pageSize);
            }
        }
        resultJson.put("list", postList);
        resultJson = commonService.addInfoForPaging(jsonObject, postList.size(), resultJson);
        return CommonUtil.commonReturn(ResponseEnum.E_413, resultJson);
    }

    @Override
    public JSONObject getPublishInfo(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getPublishInfoParameter();
        String info = "";
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long userId = jsonObject.getLong("userId");
        long plateId = jsonObject.getLong("plateId");
        JSONObject resultJson = new JSONObject();
        if (userId > 0) {
            JSONObject user = userDao.queryUserById(userId);
            JSONObject plate = plateDao.queryPlateById(plateId);
            if (user != null) {
                resultJson.put("points", user.getIntValue("points"));
            } else {
                log.warn("用户Id:{}不存在", userId);
                return CommonUtil.errorJson("用户Id不存在");
            }
            if (plate != null ) {
                resultJson.put("plateName", plate.getString("plateName"));
            } else {
                log.warn("版块Id:{}不存在", plateId);
                return CommonUtil.errorJson("版块Id不存在");
            }
            log.info("用户Id：{}进入版块Id：{}发帖", userId, plateId);
            return CommonUtil.commonReturn(ResponseEnum.E_414, resultJson);
        }
        log.warn("用户Id：{}不合法", userId);
        return CommonUtil.errorJson("用户Id不合法");
    }

    @Override
    public JSONObject getLatestPost() {
        List<JSONObject> postList = postDao.selectLatestPost();
        return CommonUtil.commonReturn(ResponseEnum.E_416, postList);
    }

    @Override
    public JSONObject getHotPost() {
        List<JSONObject> postList = postDao.selectHotPost();
        return CommonUtil.commonReturn(ResponseEnum.E_417, postList);
    }
}
