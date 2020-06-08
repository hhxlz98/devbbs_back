package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MailDao {

    //添加站内信
    void addMail(JSONObject jsonObject);
    void addSyeMails(@Param("mails")List<JSONObject> mails);

    //更改站内信状态
    void updateMailState(JSONObject jsonObject);

    //判断是否存在
    int existMailById(@Param("mailId")long mailId);

    //返回未读取的用户站内信数
    int unreadMailCountForUser(@Param("userId")long userId);

    int unreadMailCountForSys(@Param("userId")long userId);

    //系统站内信
    List<JSONObject> selectSystemMailListOnState(@Param("userId")long userId, @Param("state")int state, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    List<JSONObject> selectSystemMailListLowState(@Param("userId")long userId, @Param("state")int state, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    List<JSONObject> selectSystemMailListUpState(@Param("userId")long userId, @Param("state")int state, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    //站内信列表
    List<JSONObject> selectFromMailListOnState(@Param("userId")long userId, @Param("state")int state, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    List<JSONObject> selectFromMailListLowState(@Param("userId")long userId, @Param("state")int state, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    List<JSONObject> selectFromMailListUpState(@Param("userId")long userId, @Param("state")int state, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    List<JSONObject> selectToMailListOnState(@Param("userId")long userId, @Param("state")int state, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    List<JSONObject> selectToMailListLowState(@Param("userId")long userId, @Param("state")int state, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    List<JSONObject> selectToMailListUpState(@Param("userId")long userId, @Param("state")int state, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    //发件信列表
    List<JSONObject> selectMailForFrom(@Param("userId")long userId, @Param("fromDelete")boolean fromDelete, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);
    void updateMailFromDelete(@Param("mailId")long mailId, @Param("fromDelete")boolean fromDelete);
}
