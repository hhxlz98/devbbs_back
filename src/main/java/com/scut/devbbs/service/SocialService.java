package com.scut.devbbs.service;

import com.alibaba.fastjson.JSONObject;

public interface SocialService {

    //添加用户关系：好友，拉黑
    JSONObject addUserRelate(JSONObject jsonObject);

    //删除用户关系
    JSONObject deleteUserRelate(JSONObject jsonObject);

    //返回用户关系列表
    JSONObject userRelateList(JSONObject jsonObject);

    //添加好友申请
    JSONObject addFriendApply(JSONObject jsonObject);

    //更新好友申请状态
    JSONObject updateFriendApply(JSONObject jsonObject);

    //返回好友申请发送表
    JSONObject friendApplyListForApply(JSONObject jsonObject);

    //返回好友申请接受表 还返回未处理的好友请求个数
    JSONObject friendApplyListForApplied(JSONObject jsonObject);


}
