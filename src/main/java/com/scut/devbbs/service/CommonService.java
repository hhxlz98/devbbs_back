package com.scut.devbbs.service;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ResponseEnum;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {

    //上传图片到指定位置
    ResponseEnum uploadImg(MultipartFile file, String path, String fileName);

    //将图片修成合适的大小（暂时弃用）
    void imageToSquare(String path, String suf) throws Exception;

    //添加page信息，为无限滚动列表
    JSONObject addInfoForPaging(JSONObject jsonObject, int listSize, JSONObject resultJson);

    //news
    //添加广告
    JSONObject addNews(JSONObject jsonObject);
    //所有广告列表
    JSONObject allNewsList();
    //启用广告列表
    JSONObject homeNewsList();
    //更新广告
    JSONObject updateNews(JSONObject jsonObject);
    //更新广告启用
    JSONObject updateNewsValid(JSONObject jsonObject);
    //删除广告
    JSONObject deleteNews(JSONObject jsonObject);

}
