package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.Constant;
import com.scut.devbbs.dao.PunishRecordDao;
import com.scut.devbbs.service.PunishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class PunishServiceImpl implements PunishService {

    @Resource
    private PunishRecordDao punishRecordDao;

    @Override
    public long userForbiddenSpeakForPlate(long userId, long plateId) {
        long punishTime = punishRecordDao.userPunishForPlate(userId, plateId);
        if (punishTime != 0 || punishTime > System.currentTimeMillis()) {
            return punishTime - System.currentTimeMillis();
        }
        return 0;
    }

    @Override
    public void addUserForbidden(long dealUserId, long userId, long plateId, int lastDay) {
        long startTime = punishRecordDao.userPunishForPlate(userId, plateId);
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        long lastTime = lastDay * Constant.DAY_TIMESTAMP;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startTime", startTime);
        jsonObject.put("dealUserId", dealUserId);
        jsonObject.put("lastTime", lastTime);
        jsonObject.put("type", 0);
        jsonObject.put("plateId", plateId);
        jsonObject.put("userId", userId);
        punishRecordDao.addPunishRecord(jsonObject);
    }

    @Override
    public void addUserBan(long typeId) {
        long startTime = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startTime", startTime);
        jsonObject.put("dealUserId", 0);
        jsonObject.put("lastTime", 0);
        jsonObject.put("type", 1);
        jsonObject.put("plateId", 0);
        jsonObject.put("userId", typeId);
        punishRecordDao.addPunishRecord(jsonObject);
    }
}
