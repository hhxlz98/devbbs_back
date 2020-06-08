package com.scut.devbbs.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface MailService {

    //添加站内信
    JSONObject sendMail(JSONObject jsonObject);

    //更新站内信状态
    JSONObject updateMailState(JSONObject jsonObject);

    //返回站内信
    JSONObject userMailList(JSONObject jsonObject);

    //系统站内信
    JSONObject sysMailList(JSONObject jsonObject);

    //我的发件
    JSONObject mySendedMailList(JSONObject jsonObject);
    JSONObject updateMailFromDelete(JSONObject jsonObject);

    //系统发送站内信
    void sendSysMails(List<Long> userIds, String content, String title);
    //举报反馈
    void reportFailSysMail(List<Long> userIds, int type, long typeId);
    void reportSuccessSysMail(List<Long> userIds, int type, long typeId);
}
