package com.scut.devbbs.service;

import com.alibaba.fastjson.JSONObject;

public interface UserService {

    //用户登录
    JSONObject userLogin(JSONObject jsonObject);

    //用户注册
    JSONObject registerUser(JSONObject jsonObject);

    //用户存在
    boolean emailRepeat(String email);
    boolean usernameRepeat(String usename);

    //修改用户的信息
    JSONObject updateUserShow(JSONObject jsonObject);
    JSONObject updateUserImg(JSONObject jsonObject);
    JSONObject updateUserName(JSONObject jsonObject);
    JSONObject updateUserInfo(JSONObject jsonObject);

    //用户信息
    JSONObject userInfo(JSONObject jsonObject);

    //查询用户
    JSONObject queryUser(JSONObject jsonObject);
}
