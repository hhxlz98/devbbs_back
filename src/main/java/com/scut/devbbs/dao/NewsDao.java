package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsDao {

    //添加首页广告
    void addNews(JSONObject jsObject);

    //首页广告列表
    List<JSONObject> selectHomeNewsList();

    //全部广告列表
    List<JSONObject> selectNewsList();

    //更新广告
    void updateNews(JSONObject jsObject);

    //更新启用
    void updateNewsValid(@Param("newsId")long newsId, @Param("valid")boolean valid);

    //删除
    void deleteNews(@Param("newsId")long newsId);
}
