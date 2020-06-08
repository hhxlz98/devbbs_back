package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PunishRecordDao {

    //增加封禁记录
    void addPunishRecord(JSONObject jsonObject);

    //通过id查询用户封禁记录
    List<JSONObject> queryRecordById(@Param("userId")long userId);

    //删除封禁记录（撤销）
    void deleteRecord(@Param("punishRecordId")long punishRecordId);

    //查询用户对某一版块的禁言时间（ms）
    long userPunishForPlate(@Param("userId")long userId, @Param("plateId")long plateId);
}
