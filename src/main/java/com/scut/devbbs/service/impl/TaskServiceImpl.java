package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.constants.ResponseEnum;
import com.scut.devbbs.dao.TaskDao;
import com.scut.devbbs.dao.TaskRelateDao;
import com.scut.devbbs.dao.UserDao;
import com.scut.devbbs.service.TaskService;
import com.scut.devbbs.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskDao taskDao;

    @Resource
    private TaskRelateDao taskRelateDao;

    @Resource
    private UserDao userDao;

    @Autowired
    private ParameterConstant parameterConstant;

    @Override
    public JSONObject getTaskList(JSONObject jsonObject) {
        long userId = jsonObject.getLong("userId");
        List<JSONObject> finishList = taskDao.allFinishTask(userId);
        List<String> relateLists = new ArrayList<String>();
        for (JSONObject task : finishList) {
            long finishTime = task.getLong("finishTime");
            long circleTime = task.getLong("circleTime");
            if (new Date().getTime() - finishTime > circleTime) {
                relateLists.add(task.getString("taskRelateId"));
            }
        }
        if (relateLists.size() > 0) {
            taskRelateDao.deleteTaskRelate(relateLists);
        }
        List<String> relateTaskId = taskRelateDao.selectUserTask(userId);
        List<JSONObject> taskList = taskDao.listTask(relateTaskId);
        return CommonUtil.commonReturn(ResponseEnum.E_1001, taskList);
    }

    @Override
    public JSONObject addTaskRelate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddTaskRelateParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long addTime = new Date().getTime();
        jsonObject.put("addTime", addTime);
        jsonObject.put("finishTime", 0);
        //目前都是直接可以完成的任务
        jsonObject.put("state", 1);
        taskRelateDao.addTaskRelate(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_1002, jsonObject);
    }

    @Override
    public JSONObject rewardTask(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getFinishTaskParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long taskRelateId = jsonObject.getLong("taskRelateId");
        taskRelateDao.finishTask(taskRelateId, new Date().getTime());
        int reward = taskRelateDao.queryTaskReward(taskRelateId);
        long userId = jsonObject.getLong("userId");
        userDao.computeUserPoints(userId, reward);
        return CommonUtil.commonReturn(ResponseEnum.E_1003, jsonObject);
    }

    @Override
    public JSONObject doingTaskList(JSONObject jsonObject) {
        long userId = jsonObject.getLong("userId");
        List<JSONObject> taskList = taskDao.listDoingTask(userId);
        return CommonUtil.commonReturn(ResponseEnum.E_1004, taskList);
    }

    @Override
    public JSONObject finishTaskList(JSONObject jsonObject) {
        long userId = jsonObject.getLong("userId");
        List<JSONObject> finishList = taskDao.allFinishTask(userId);
        System.out.println(finishList);
        List<String> relateLists = new ArrayList<String>();
        for (JSONObject task : finishList) {
            System.out.println(task);
            long finishTime = task.getLong("finishTime");
            long circleTime = task.getLong("circleTime");
            if (new Date().getTime() - finishTime > circleTime) {
                relateLists.add(task.getString("taskRelateId"));
            }
        }
        if (relateLists.size() > 0) {
            taskRelateDao.deleteTaskRelate(relateLists);
        }
        List<JSONObject> taskList = taskDao.allFinishTask(userId);
        for (JSONObject j : taskList) {
            long finishTime = j.getLong("finishTime");
            long circle = j.getLong("circleTime");
            long restTime = finishTime +  circle -  new Date().getTime() ;
            j.put("restTime", restTime);
        }
        return CommonUtil.commonReturn(ResponseEnum.E_1005, taskList);
    }

    @Override
    public JSONObject addTask(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getAddTaskParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        taskDao.addTask(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_1006, jsonObject);
    }

    @Override
    public JSONObject updateTask(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUpdateTaskParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        taskDao.updateTaskInfo(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_1007, jsonObject);
    }

    @Override
    public JSONObject listAllTask() {
        List<JSONObject> taskList = taskDao.allTaskList();
        return CommonUtil.commonReturn(ResponseEnum.E_1008, taskList);
    }

    @Override
    public JSONObject deleteTask(JSONObject jsonObject) {
        long taskId = jsonObject.getLong("taskId");
        taskDao.deleteTask(taskId);
        return CommonUtil.commonReturn(ResponseEnum.E_1009, jsonObject);
    }


}
