package com.scut.devbbs.controller;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.service.TaskService;
import com.scut.devbbs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/list")
    public JSONObject getTaskList(HttpServletRequest request) {
        return taskService.getTaskList(CommonUtil.request2Json(request));
    }

    @GetMapping("/doingList")
    public JSONObject getDongList(HttpServletRequest request) {
        return taskService.doingTaskList(CommonUtil.request2Json(request));
    }

    @GetMapping("/finishList")
    public JSONObject getFinishList(HttpServletRequest request) {
        return taskService.finishTaskList(CommonUtil.request2Json(request));
    }

    @PostMapping("/getTask")
    public JSONObject getTask(@RequestBody JSONObject jsonObject) {
        return taskService.addTaskRelate(jsonObject);
    }

    @PostMapping("/getReward")
    public JSONObject getReward(@RequestBody JSONObject jsonObject) {
        return taskService.rewardTask(jsonObject);
    }

    @PostMapping("/addTask")
    public JSONObject addTask(@RequestBody JSONObject jsonObject) {
        return taskService.addTask(jsonObject);
    }

    @PostMapping("/updateTask")
    public JSONObject updateTask(@RequestBody JSONObject jsonObject) {
        return taskService.updateTask(jsonObject);
    }

    @GetMapping("/allTaskList")
    public JSONObject allTaskList() {
        return taskService.listAllTask();
    }

    @PostMapping("/deleteTask")
    public JSONObject deleteTask(@RequestBody JSONObject jsonObject) {
        return taskService.deleteTask(jsonObject);
    }
}
