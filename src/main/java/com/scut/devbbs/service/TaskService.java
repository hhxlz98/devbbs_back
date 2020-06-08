package com.scut.devbbs.service;

import com.alibaba.fastjson.JSONObject;

public interface TaskService {

    JSONObject getTaskList(JSONObject jsonObject);

    JSONObject addTaskRelate(JSONObject jsonObject);

    JSONObject rewardTask(JSONObject jsonObject);

    JSONObject doingTaskList(JSONObject jsonObject);

    JSONObject finishTaskList(JSONObject jsonObject);

    //管理
    JSONObject addTask(JSONObject jsonObject);
    JSONObject updateTask(JSONObject jsonObject);

    JSONObject listAllTask();
    JSONObject deleteTask(JSONObject jsonObject);
}
