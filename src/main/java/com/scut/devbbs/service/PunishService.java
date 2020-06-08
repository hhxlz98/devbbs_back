package com.scut.devbbs.service;

public interface PunishService {

    //返回用户在版块下的禁言时间，为0则没有禁言
    long userForbiddenSpeakForPlate(long userId, long plateId);

    //添加用户的禁言
    void addUserForbidden(long dealUserId, long userId, long plateId, int lastDay);

    //添加用户封禁
    void addUserBan(long typeId);
}
