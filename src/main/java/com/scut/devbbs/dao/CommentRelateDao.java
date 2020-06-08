package com.scut.devbbs.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

public interface CommentRelateDao {

    void addCommentRelate(JSONObject jsonObject);

    void deleteCommentRelate(JSONObject jsonObject);

    JSONObject existCommentRelate(JSONObject jsonObject);

}
