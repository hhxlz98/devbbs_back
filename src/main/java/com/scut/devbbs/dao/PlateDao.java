package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlateDao {

    //获取全部板块列表
    List<JSONObject>listPlate(@Param("state")int state);

    List<JSONObject>myListPlate(@Param("plateIds")List<String> plateIds);

    //板块是否存在
    int existPlate(@Param("plateName")String plateName, @Param("plateId")long plateId);

    //用户新关注一个板块
    void plateFollowAdd(@Param("plateId")long plateId);

    //用户取消关注
    void plateFollowReduce(@Param("plateId")long plateId);

    //查询板块信息
    JSONObject queryPlateById(@Param("plateId")long plateId);

    //创建板块
    void addPlate(JSONObject jsonObject);

    //数据更新，post数
    void updatePlatePostNumber(@Param("plateId")long plateId, @Param("num")int num);

    //更新板块信息
     void updatePlateInfo(JSONObject jsonObject);

     //更新版块的状态（删除or恢复）
    void updatePlateState(JSONObject jsonObject);
}
