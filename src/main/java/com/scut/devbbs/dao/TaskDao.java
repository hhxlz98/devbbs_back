package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskDao {

    void updateTaskInfo(JSONObject jsonObject);

    void addTask(JSONObject jsonObject);

    void deleteTask(@Param("taskId")long taskId);

    List<JSONObject> listDoingTask(@Param("userId")long userId);

    List<JSONObject> listTask(@Param("finishTasks")List<String> finishTasks);

    List<JSONObject> allFinishTask(@Param("userId")long userId);

    List<JSONObject> allTaskList();
}
