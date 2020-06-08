package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportRecordDao {

    //添加举报信息
    void addReportRecord(JSONObject jsonObject);

    //更新举报信息处理状态
    void updateRecordState(@Param("records")List<String> records);
    void updateReportedState(@Param("type")int type, @Param("typeId")long typeId);

    //按照用户列出举报信息条数
    List<JSONObject> selectReportedUser();

    List<JSONObject> selectUndealRecord(@Param("userId")long userId);

    List<JSONObject> selectRecordByUserId(@Param("userId")long userId);

    //按照被举报列出信息条数
    List<JSONObject> selectReportInfoForPost(@Param("plateId")long plateId);

    //查询
    JSONObject queryRecordByTypeAndTypeId(@Param("type")int type, @Param("typeId")long typeId);



    //列出版块的举报信息列表
    List<JSONObject> selectReportRecordWithCount(@Param("plateId")long plateId, @Param("isDeal")boolean isDeal);
    //根据typeId、type、isDeal列出某一举报的举报列表
    List<JSONObject> selectRecord(@Param("typeId")long typeId, @Param("type")int type, @Param("isDeal")boolean isDeal);

    //列出用户举报信息列表
    List<JSONObject> selectUserReportRecordWithCount(@Param("isDeal")boolean isDeal);
    //列出某一用户的举报信息
    List<JSONObject> selectRecordForUser(@Param("typeId")long typeId, @Param("isDeal")boolean isDeal);


    //获取被举报对象的所有举报用户Id
    List<Long> selectReportUsers(@Param("type")int type, @Param("typeId")long typeId);

}
