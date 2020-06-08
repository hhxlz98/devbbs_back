package com.scut.devbbs.service;

import com.alibaba.fastjson.JSONObject;

public interface ReportService {

    //接收用户举报
    JSONObject responseUserReport(JSONObject jsonObject);

    //版块举报列表
    JSONObject reportListForPlate(JSONObject jsonObject);

    //后台管理用户举报列表
    JSONObject userReportList(JSONObject jsonObject);

    //处理举报
    //忽略
    JSONObject ignoreReport(JSONObject jsonObject);
    //处理
    JSONObject dealReport(JSONObject jsonObject);
    //封禁
    JSONObject banUser(JSONObject jsonObject);
    //撤销
    JSONObject cancelPunish(JSONObject jsonObject);

    //用户封禁记录
    JSONObject userPunishList(JSONObject jsonObject, boolean isAll);


}
