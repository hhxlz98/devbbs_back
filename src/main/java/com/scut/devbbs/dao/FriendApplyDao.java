package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendApplyDao {

    void addFriendApply(JSONObject jsonObject);

    void updateFriendApplyState(JSONObject jsonObject);

    int existFriendApply(JSONObject jsonObject);

    List<JSONObject> toFriendApplyList(@Param("userId")long userId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    List<JSONObject> fromFriendApplyList(@Param("userId")long userId, @Param("currentIndex")int currentIndex, @Param("pageSize")int pageSize);

    int friendApplyCountForUnread(@Param("userId")long userId);

    JSONObject queryFriendApply(@Param("friendApplyId")long friendApplyId);


}
