package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.constants.ResponseEnum;
import com.scut.devbbs.dao.*;
import com.scut.devbbs.service.MailService;
import com.scut.devbbs.service.PlateService;
import com.scut.devbbs.service.PunishService;
import com.scut.devbbs.service.ReportService;
import com.scut.devbbs.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private PunishRecordDao punishRecordDao;

    @Resource
    private ReportRecordDao reportRecordDao;

    @Resource
    private CommentDao  commentDao;

    @Resource
    private PostDao postDao;

    @Resource
    private UserDao userDao;

    @Autowired
    private PlateService plateService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PunishService punishService;

    @Autowired
    private ParameterConstant parameterConstant;


    @Override
    public JSONObject responseUserReport(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUserReportParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        jsonObject.put("reportTime", new Date().getTime());
        reportRecordDao.addReportRecord(jsonObject);
        return CommonUtil.commonReturn(ResponseEnum.E_1101, jsonObject);
    }

    @Override
    public JSONObject reportListForPlate(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getPlateReportListParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long userId = jsonObject.getLong("userId");
        long plateId = jsonObject.getLong("plateId");
        boolean isDeal = jsonObject.getBoolean("isDeal");
        if (userId > 0) {
            if (!plateService.userManagePlateExist(userId, plateId)) {
                return CommonUtil.commonReturn(ResponseEnum.E_209, jsonObject);
            }
        }
        List<JSONObject> reportList = reportRecordDao.selectReportRecordWithCount(plateId, isDeal);
        if (reportList.size() > 0) {
            for (JSONObject record : reportList) {
                int type = record.getIntValue("type");
                long typeId = record.getLong("typeId");

                if (type == 0) {
                    JSONObject post = postDao.postContentByPostId(typeId);
                    if (post.getIntValue("userStatus") == 0) {
                        long forbiddenTime =punishService.userForbiddenSpeakForPlate(post.getLong("userId"), plateId);
                        if (forbiddenTime > 0) {
                            post.put("userStatus", 2);
                            post.put("forbiddenTime", forbiddenTime);
                        }
                    }
                    record.put("typeContent", post);
                } else if(type == 1) {
                    JSONObject comment = commentDao.commentContentById(typeId);
                    if (comment.getIntValue("userStatus") == 0) {
                        long forbiddenTime =punishService.userForbiddenSpeakForPlate(comment.getLong("userId"), plateId);
                        if (forbiddenTime > 0) {
                            comment.put("userStatus", 2);
                            comment.put("forbiddenTime", forbiddenTime);
                        }
                    }
                    record.put("typeContent", comment);
                }
                List<JSONObject> recordList = reportRecordDao.selectRecord(typeId, type, isDeal);
                if (recordList.size() > 0) {
                    record.put("list", recordList);
                } else {
                    log.warn("举报列表为空， type：{}， typeId：{}，isDeal：{}",type, typeId, isDeal);
                }
            }
        }
        return CommonUtil.commonReturn(ResponseEnum.E_1102, reportList);
    }

    @Override
    public JSONObject userReportList(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getUserReportListParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        boolean isDeal = jsonObject.getBoolean("isDeal");
        List<JSONObject> reportList = reportRecordDao.selectUserReportRecordWithCount(isDeal);
        if (reportList.size() > 0) {
            for (JSONObject record : reportList) {
                long typeId = record.getLong("typeId");
                List<JSONObject> recordList = reportRecordDao.selectRecordForUser(typeId, isDeal);
                if (recordList.size() > 0) {
                    record.put("list", recordList);
                } else {
                    log.warn("举报列表为空， type：{}， typeId：{}，isDeal：{}", 2, typeId, isDeal);
                }
            }
        }
        return CommonUtil.commonReturn(ResponseEnum.E_1104, reportList);
    }

    @Override
    public JSONObject ignoreReport(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getIgnoreReportParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long userId = jsonObject.getLong("userId");
        if (userId > 0) {
            if (!plateService.userManagePlateExist(userId, jsonObject.getLong("plateId"))) {
                return CommonUtil.commonReturn(ResponseEnum.E_209, jsonObject);
            }
        }
        long typeId = jsonObject.getLong("typeId");
        int type = jsonObject.getIntValue("type");
        List<Long> reportUsers = reportRecordDao.selectReportUsers(type, typeId);
        mailService.reportFailSysMail(reportUsers, type, typeId);
        reportRecordDao.updateReportedState(type, typeId);
        return CommonUtil.commonReturn(ResponseEnum.E_1103, jsonObject);
    }

    @Override
    public JSONObject dealReport(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getDealReportParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long userId = jsonObject.getLong("userId");
        long plateId = jsonObject.getLong("plateId");
        if (userId > 0) {
            if (!plateService.userManagePlateExist(userId, plateId)) {
                return CommonUtil.commonReturn(ResponseEnum.E_209, jsonObject);
            }
        }
        long typeId = jsonObject.getLong("typeId");
        int type = jsonObject.getIntValue("type");
        List<Long> reportUsers = reportRecordDao.selectReportUsers(type, typeId);
        mailService.reportSuccessSysMail(reportUsers, type, typeId);
        reportRecordDao.updateReportedState(type, typeId);

        int lastDay = jsonObject.getIntValue("lastDay");
        long reportUserId = reportRecordDao.queryRecordByTypeAndTypeId(type, typeId).getLong("reportUserId");
        if (lastDay > 0) {
            //add punish
            punishService.addUserForbidden(userId, reportUserId, plateId, lastDay);
        }
        if (jsonObject.getBoolean("isDelete")) {
            if (type == 0) {
                postDao.updatePostState(typeId, 2);
            } else {
                commentDao.updateCommentState(typeId, 2);
            }
        }
        if (jsonObject.getBoolean("isUpReport")) {
            JSONObject upReport = new JSONObject();
            upReport.put("reportUserId", userId);
            upReport.put("reportedUserId", reportUserId);
            upReport.put("reportTime", System.currentTimeMillis());
            upReport.put("reportInfo", "来自版主上报");
            upReport.put("plateId", 0);
            upReport.put("type", 2);
            upReport.put("typeId", reportUserId);
            reportRecordDao.addReportRecord(upReport);
        }
        return CommonUtil.commonReturn(ResponseEnum.E_1103, jsonObject);
    }

    @Override
    public JSONObject banUser(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getBanUserParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long typeId = jsonObject.getLong("typeId");
        if (typeId > 0) {
            punishService.addUserBan(typeId);
            List<Long> reportUsers = reportRecordDao.selectReportUsers(2, typeId);
            if (reportUsers.size() > 0) {
                mailService.reportSuccessSysMail(reportUsers, 2, typeId);
                reportRecordDao.updateReportedState(2, typeId);
            }
            userDao.updateUserStatus(typeId, 1);
        } else {
            log.error("封禁用户id{}不合格", typeId);
            return CommonUtil.errorJson("封禁用户id不合格");
        }
        return CommonUtil.commonReturn(ResponseEnum.E_1105, jsonObject);
    }

    @Override
    public JSONObject cancelPunish(JSONObject jsonObject) {
        String[] parameters = parameterConstant.getCancelPunishParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long userId = jsonObject.getLong("userId");
        long punishRecordId = jsonObject.getLong("punishRecordId");
        int type = jsonObject.getIntValue("type");
        switch (type) {
            case 0: {
                break;
            }
            case 1: {
                userDao.updateUserStatus(userId, 0);
                break;
            }
        }
        punishRecordDao.deleteRecord(punishRecordId);
        return CommonUtil.commonReturn(ResponseEnum.E_1107, jsonObject);
    }

    @Override
    public JSONObject userPunishList(JSONObject jsonObject, boolean isAll) {
        String[] parameters = parameterConstant.getUserPunishListParameter();
        if(!CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        long userId = jsonObject.getLong("userId");
        List<JSONObject> resultList = new ArrayList<JSONObject>();
        if (isAll) {
            JSONObject resultJson = userDao.queryUserById(userId);
            List<JSONObject> recordList = punishRecordDao.queryRecordById(userId);
            int num = recordList.size();
            resultJson.put("num", num);
            if (num > 0) {
                for (JSONObject record: recordList) {
                    long startTime = record.getLong("startTime");
                    long lastTime = record.getLong("lastTime");
                    if (startTime + lastTime > System.currentTimeMillis()) {
                        record.put("valid", true);
                    } else {
                        if (record.getIntValue("type") == 1) {
                            record.put("valid", true);
                        } else {
                            record.put("valid", false);
                        }
                    }
                }
            }
            resultJson.put("list", recordList);
            resultList.add(resultJson);
        } else {

        }
        return CommonUtil.commonReturn(ResponseEnum.E_1106, resultList);
    }
}
