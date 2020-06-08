package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRelateDao {

    void addUserRelate(JSONObject jsonObject);

    JSONObject existUserRelate(@Param("user1Id")long user1Id, @Param("user2Id")long user2Id, @Param("type")int type);

    void deleteUserRelate(JSONObject jsonObject);

    List<JSONObject> userListForType(JSONObject jsonObject);

}
