package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlateRelateDao {

    //获取特定用户的特定关系的板块
    List<JSONObject> relatePlate(@Param("userId")long userId, @Param("type") int type);

    //增加一个联系
    void addPlateRelate(JSONObject jsonObject);

    //删除一个联系
    void deletePlateRelate(JSONObject jsonObject);

    //判断联系是否存在
    JSONObject existPlateRelate(JSONObject jsonObject);

    List<JSONObject> listPlateRelateUser(@Param("plateId")long plateId, @Param("type")int type);

}
