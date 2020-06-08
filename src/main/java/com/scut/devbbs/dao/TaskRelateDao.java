package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskRelateDao {

    void addTaskRelate(JSONObject jsonObject);

    void updateTaskRelate(@Param("taskRelateId")long taskRelateId, @Param("state")int state);

    void finishTask(@Param("taskRelateId")long taskRelateId, @Param("finishTime")long finishTime);

    void deleteTaskRelate(@Param("ids")List<String>ids);

    int queryTaskReward(@Param("taskRelateId")long taskRelateId);

    List<String> selectUserTask(@Param("userId")long userId);
}
