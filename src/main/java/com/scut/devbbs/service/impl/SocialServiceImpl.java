package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.constants.ResponseEnum;
import com.scut.devbbs.dao.FriendApplyDao;
import com.scut.devbbs.service.CommonService;
import com.scut.devbbs.util.CommonUtil;
import com.scut.devbbs.dao.UserRelateDao;
import com.scut.devbbs.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SocialServiceImpl implements SocialService {

    @Autowired
    private ParameterConstant parameterConstant;

    @Resource
    private FriendApplyDao friendApplyDao;

    @Resource
    private UserRelateDao userRelateDao;

    @Autowired
    private CommonService commonService;

    @Override
    public JSONObject addUserRelate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddUserRelateParameter();
        String info = " ";
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        int type = jsonObject.getIntValue("type");
        long user1Id = jsonObject.getLong("user1Id");
        long user2Id = jsonObject.getLong("user2Id");
        if (userRelateDao.existUserRelate(user1Id, user2Id, type) == null ) {
            long addTime = new Date().getTime();
            jsonObject.put("addTime", addTime);
            if (type == 0) {
                if (userRelateDao.existUserRelate(user2Id, user1Id, type) == null) {
                    JSONObject newJson = new JSONObject();
                    newJson.put("user1Id", user2Id);
                    newJson.put("user2Id", user1Id);
                    newJson.put("type", type);
                    newJson.put("addTime", addTime);
                    userRelateDao.addUserRelate(newJson);
                    userRelateDao.addUserRelate(jsonObject);
                    return CommonUtil.commonReturn(ResponseEnum.E_801, jsonObject);
                } else {
                    return CommonUtil.commonReturn(ResponseEnum.E_802, jsonObject);
                }
            }
            userRelateDao.addUserRelate(jsonObject);
            return CommonUtil.commonReturn(ResponseEnum.E_801, jsonObject);
        } else {
            return CommonUtil.commonReturn(ResponseEnum.E_802, jsonObject);
        }
    }

    @Override
    public JSONObject deleteUserRelate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddUserRelateParameter();
        String info = " ";
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        int type = jsonObject.getIntValue("type");
        long user1Id = jsonObject.getLong("user1Id");
        long user2Id = jsonObject.getLong("user2Id");
        if (type == 0) {
            if (userRelateDao.existUserRelate(user1Id, user2Id, type) != null && userRelateDao.existUserRelate(user2Id, user1Id, type) != null) {
                JSONObject newJson = new JSONObject();
                newJson.put("user1Id", user2Id);
                newJson.put("user2Id", user1Id);
                newJson.put("type", type);
                userRelateDao.deleteUserRelate(jsonObject);
                userRelateDao.deleteUserRelate(newJson);
                return CommonUtil.commonReturn(ResponseEnum.E_803, jsonObject);
            } else {
                return CommonUtil.commonReturn(ResponseEnum.E_804, jsonObject);
            }
        } else if (type == 1) {
            if (userRelateDao.existUserRelate(user1Id, user2Id, type) != null) {
                userRelateDao.deleteUserRelate(jsonObject);
                return CommonUtil.commonReturn(ResponseEnum.E_803, jsonObject);
            } else {
                return CommonUtil.commonReturn(ResponseEnum.E_804, jsonObject);
            }
        }
        return null;
    }

    @Override
    public JSONObject userRelateList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUserRelateListParameter();
        String info = " ";
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        List<JSONObject> userRelateList = userRelateDao.userListForType(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_805, userRelateList);
    }

    @Override
    public JSONObject addFriendApply(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddFriendApplyParameter();
        String info = " ";
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        if (friendApplyDao.existFriendApply(jsonObject) == 0) {
            jsonObject.put("state", 0);
            jsonObject.put("applyTime", new Date().getTime());
            friendApplyDao.addFriendApply(jsonObject);
            return CommonUtil.commonReturn(ResponseEnum.E_806, jsonObject);
        } else {
            return CommonUtil.commonReturn(ResponseEnum.E_807, jsonObject);
        }
    }

    @Override
    public JSONObject updateFriendApply(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUpdateFriendApplyParameter();
        String info = " ";
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        JSONObject apply = friendApplyDao.queryFriendApply(jsonObject.getLong("friendApplyId"));
        if (apply == null) {
            return CommonUtil.errorJson("申请不存在");
        }
        friendApplyDao.updateFriendApplyState(jsonObject);
        //同意申请后，需要加入好友关系
        if (jsonObject.getIntValue("state") == 1) {
            JSONObject addRelateJson = new JSONObject();
            addRelateJson.put("type", 0);
            addRelateJson.put("user1Id", apply.getLongValue("fromId"));
            addRelateJson.put("user2Id", apply.getLongValue("toId"));
            addUserRelate(addRelateJson);
        }
        return CommonUtil.commonReturn(ResponseEnum.E_808, jsonObject);
    }

    @Override
    public JSONObject friendApplyListForApply(JSONObject jsonObject) {
        long userId = jsonObject.getLong("userId");
        if (userId > 0) {
            JSONObject resultJson = new JSONObject();
            int currentPage = jsonObject.getIntValue("currentPage");
            int pageSize = jsonObject.getIntValue("pageSize");
            int currentIndex = (currentPage - 1) * pageSize;
            List<JSONObject> applyList = friendApplyDao.fromFriendApplyList(userId, currentIndex, pageSize);
            resultJson.put("list", applyList);
            resultJson = commonService.addInfoForPaging(jsonObject, applyList.size(), resultJson);
            return CommonUtil.commonReturn(ResponseEnum.E_809, resultJson);
        } else {
            return CommonUtil.errorJson("userId不合法");
        }
    }

    @Override
    public JSONObject friendApplyListForApplied(JSONObject jsonObject) {
        long userId = jsonObject.getLong("userId");
        if (userId > 0) {
            JSONObject resultJson = new JSONObject();
            int currentPage = jsonObject.getIntValue("currentPage");
            int pageSize = jsonObject.getIntValue("pageSize");
            int currentIndex = (currentPage - 1) * pageSize;
            List<JSONObject> applyList = friendApplyDao.toFriendApplyList(userId, currentIndex, pageSize);
            int newCount = friendApplyDao.friendApplyCountForUnread(userId);
            resultJson.put("newCount", newCount);
            resultJson.put("list", applyList);
            resultJson = commonService.addInfoForPaging(jsonObject, applyList.size(), resultJson);
            return CommonUtil.commonReturn(ResponseEnum.E_809, resultJson);
        } else {
            return CommonUtil.errorJson("userId不合法");
        }
    }
}
