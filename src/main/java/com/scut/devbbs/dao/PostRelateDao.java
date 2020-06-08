package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

public interface PostRelateDao {

    void addPostRelate(JSONObject jsonObject);

    void deletePostRelate(JSONObject jsonObject);

    JSONObject existPostRelate(JSONObject jsonObject);

}
