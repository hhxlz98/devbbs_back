package com.scut.devbbs.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface PlateService {

    //获取全部板块列表，需判断是否userId来确认是否关注
    JSONObject listPlate(JSONObject jsonObject);
    //获取特定关联的板块列表，同样需要增加是否关注的信息
    JSONObject myListPlate(JSONObject jsonObject);
    //添加板块，会判断板块名字是否重复
    JSONObject addPlate(JSONObject jsonObject);
    //增加用户与板块的联系：关注、管理（添加版主权限），同时会调用板块方法更新板块数据（关注数）
    JSONObject addPlateRelate(JSONObject jsonObject);

    //板块关注数加1或者减1
    void plateFollowAdd(JSONObject jsonObject);
    void plateFollowReduce(JSONObject jsonObject);

    //删除用户与板块的联系：取消关注、撤销版主权限
    JSONObject deletePlateRelate(JSONObject jsonObject);

    //提取功能：给板块信息list增加isFollow内容，前端板块组件需要这一数据
    List<JSONObject> addFollowInfo(List<JSONObject> jsonObjects, long userId);

    //获取板块信息
    JSONObject plateInfo(JSONObject jsonObject);

    //判断联系是否存在
    boolean plateRelateExist(JSONObject jsonObject);

    //判断用户是否有版块的管理权限
    boolean userManagePlateExist(long userId, long plateId);

    //判断板块是否存在
    boolean plateExist(JSONObject jsonObject);

    //后台管理获取板块列表
    JSONObject allPlateList(JSONObject jsonObject);
    boolean existPlateName(JSONObject jsonObject);
    JSONObject updatePlateInfo(JSONObject jsonObject);
    JSONObject updatePlateState(JSONObject jsonObject);
    JSONObject plateAddManageList();


}
