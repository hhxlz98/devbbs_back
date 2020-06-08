package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.constants.ResponseEnum;
import com.scut.devbbs.dao.CommentDao;
import com.scut.devbbs.dao.MailDao;
import com.scut.devbbs.dao.PostDao;
import com.scut.devbbs.dao.UserDao;
import com.scut.devbbs.service.CommonService;
import com.scut.devbbs.service.MailService;
import com.scut.devbbs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    @Resource
    private MailDao mailDao;

    @Resource
    private PostDao postDao;

    @Resource
    private CommentDao commentDao;

    @Resource
    private UserDao userDao;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ParameterConstant parameterConstant;

    @Override
    public JSONObject sendMail(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddMailParameter();
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        jsonObject.put("sendTime", System.currentTimeMillis());
        jsonObject.put("state", 0);
        jsonObject.put("fromDelete", false);
        mailDao.addMail(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_901, jsonObject);
    }

    @Override
    public JSONObject updateMailState(JSONObject jsonObject) {
        long mailId = jsonObject.getLong("mailId");
        if (mailDao.existMailById(mailId) == 1) {
            mailDao.updateMailState(jsonObject);
            return CommonUtil.commonReturn(ResponseEnum.E_902, jsonObject);
        } else {
            return CommonUtil.errorJson("mailId不存在");
        }
    }

    @Override
    public JSONObject userMailList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getMailListParameter();
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        String type = jsonObject.getString("type");
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        boolean isFrom = jsonObject.getBoolean("isFrom");
        int state = jsonObject.getIntValue("state");
        long userId = jsonObject.getIntValue("userId");
        List<JSONObject> mailList = new ArrayList<JSONObject>();
        if (type.equals("on")) {
            if (isFrom) {
                mailList = mailDao.selectFromMailListOnState(userId, state, currentIndex, pageSize);
            } else {
                mailList = mailDao.selectToMailListOnState(userId, state, currentIndex, pageSize);
            }
        } else if (type.equals("up")) {
            if (isFrom) {
                mailList = mailDao.selectFromMailListUpState(userId, state, currentIndex, pageSize);
            } else {
                mailList = mailDao.selectToMailListUpState(userId, state, currentIndex, pageSize);
            }
        } else if (type.equals("low")) {
            if (isFrom) {
                mailList = mailDao.selectFromMailListLowState(userId, state, currentIndex, pageSize);
            } else {
                mailList = mailDao.selectToMailListLowState(userId, state, currentIndex, pageSize);
            }
        } else {
            return CommonUtil.errorJson("type参数找不到");
        }
        JSONObject resultJson = new JSONObject();
        if (!isFrom) {
            int newCount = mailDao.unreadMailCountForUser(userId);
            resultJson.put("newCount", newCount);
        }
        resultJson.put("list", mailList);
        resultJson = commonService.addInfoForPaging(jsonObject, mailList.size(), resultJson);
        return CommonUtil.commonReturn(ResponseEnum.E_903, resultJson);
    }

    @Override
    public JSONObject sysMailList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getSysMailListParameter();
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        String type = jsonObject.getString("type");
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        int state = jsonObject.getIntValue("state");
        long userId = jsonObject.getIntValue("userId");
        List<JSONObject> mailList = new ArrayList<JSONObject>();
        if (type.equals("on")) {
            mailList = mailDao.selectSystemMailListOnState(userId, state, currentIndex, pageSize);
        } else if (type.equals("up")) {
            mailList = mailDao.selectSystemMailListUpState(userId, state, currentIndex, pageSize);
        } else if (type.equals("low")) {
            mailList = mailDao.selectSystemMailListLowState(userId, state, currentIndex, pageSize);
        } else {
            return CommonUtil.errorJson("type参数找不到");
        }
        JSONObject resultJson = new JSONObject();
        int newCount = mailDao.unreadMailCountForSys(userId);
        resultJson.put("newCount", newCount);
        resultJson.put("list", mailList);
        resultJson = commonService.addInfoForPaging(jsonObject, mailList.size(), resultJson);
        return CommonUtil.commonReturn(ResponseEnum.E_903, resultJson);
    }

    @Override
    public JSONObject mySendedMailList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getSendedMailListParameter();
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        int currentPage = jsonObject.getIntValue("currentPage");
        int pageSize = jsonObject.getIntValue("pageSize");
        int currentIndex = (currentPage - 1) * pageSize;
        long userId = jsonObject.getIntValue("userId");
        boolean isDelete = jsonObject.getBoolean("fromDelete");
        JSONObject resultJson = new JSONObject();
        List<JSONObject> mailList = mailDao.selectMailForFrom(userId, isDelete, currentIndex, pageSize);
        resultJson.put("list", mailList);
        resultJson = commonService.addInfoForPaging(jsonObject, mailList.size(), resultJson);
        return CommonUtil.commonReturn(ResponseEnum.E_903, resultJson);
    }

    @Override
    public JSONObject updateMailFromDelete(JSONObject jsonObject) {
        long mailId = jsonObject.getLong("mailId");
        boolean fromDelete = jsonObject.getBoolean("fromDelete");
        mailDao.updateMailFromDelete(mailId, fromDelete);
        return CommonUtil.commonReturn(ResponseEnum.E_904, jsonObject);
    }

    @Override
    public void sendSysMails(List<Long> userIds, String content, String title) {
        List<JSONObject> mailList = new ArrayList<JSONObject>();
        long sendTime = System.currentTimeMillis();
        for (Long user : userIds) {
            JSONObject mail = new JSONObject();
            mail.put("fromId", 0);
            mail.put("toId", user);
            mail.put("title", title);
            mail.put("content", content);
            mail.put("sendTime", sendTime);
            mail.put("state", 0);
            mail.put("fromDelete", false);
            mailList.add(mail);
        }
        mailDao.addSyeMails(mailList);
    }

    @Override
    public void reportFailSysMail(List<Long> userIds, int type, long typeId) {
        String content = "";
        switch (type) {
            case 0: {
                JSONObject post = postDao.postContentByPostId(typeId);
                content = "您举报用户【" +post.getString("userName") + "】的帖子【" + post.getString("title") + "】未被管理员识别违反平台规定，未能处理，感谢您对社区维护的支持！";
            }
            break;
            case 1: {
                JSONObject comment = commentDao.commentContentById(typeId);
                content = "您举报用户【" + comment.getString("userName") + "】的在帖子【" + comment.getString("postTile") + "】的回复未被管理员识别违反平台规定，未能处理，感谢您对社区维护的支持！";
            }
            break;
            case 2: {
                JSONObject user = userDao.queryUserById(typeId);
                content = "您举报的用户【"  + user.getString("userName") +  "】("+ user.getString("userId") +")未被管理员识别违反平台规定，未能处理，感谢您对社区维护的支持！";
            }
        }
        sendSysMails(userIds, content, "举报反馈");
    }

    @Override
    public void reportSuccessSysMail(List<Long> userIds, int type, long typeId) {
        String content = "";
        switch (type) {
            case 0: {
                JSONObject post = postDao.postContentByPostId(typeId);
                content = "您举报用户【" +post.getString("userName") + "】的帖子【" + post.getString("title") + "】已被管理员识别并处理，感谢您对社区维护的支持！";
            }
            break;
            case 1: {
                JSONObject comment = commentDao.commentContentById(typeId);
                content = "您举报用户【" + comment.getString("userName") + "】的在帖子【" + comment.getString("postTile") + "】的回复已被管理员识别并能处理，感谢您对社区维护的支持！";
            }
            break;
            case 2: {
                JSONObject user = userDao.queryUserById(typeId);
                content = "您举报的用户【"  + user.getString("userName") +  "】("+ user.getString("userId") +")已被管理员识别并能处理，感谢您对社区维护的支持！";
            }
        }
        sendSysMails(userIds, content, "举报反馈");
    }
}
