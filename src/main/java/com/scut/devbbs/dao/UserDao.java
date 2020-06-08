package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    //根据邮箱返回账户信息
    JSONObject getUser(@Param("userEmail")String userEmail);

    //注册用户
    void addUser(JSONObject jsonObject);

    //查询用户
    JSONObject queryUserById(@Param("userId")long userId);
    JSONObject queryUserByEmail(@Param("email")String email);
    List<JSONObject> queryUserByUserName(@Param("name")String name);

    //查询存在
    JSONObject existUsername(@Param("username")String username);
    JSONObject existEmail(@Param("userEmail")String userEmail);

    //更新用户信息
    void updateUserName(JSONObject jsonObject);
    void updateUserShow(JSONObject jsonObject);
    void updateUserImg(JSONObject jsonObject);
    void updateUserInfo(JSONObject jsonObject);

    void computeUserPoints(@Param("userId")long userId, @Param("num")int num);

    //更新用户的账号状态
    void updateUserStatus(@Param("userId")long userId, @Param("status")int status);
}
