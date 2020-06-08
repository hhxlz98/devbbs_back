package com.scut.devbbs;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.dao.*;
import com.scut.devbbs.service.PlateService;
import com.scut.devbbs.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class DevbbsApplicationTests {

    @Autowired
    private PlateService plateService;

    @Autowired
    private PostService postService;

    @Resource
    private PostRelateDao postRelateDao;

    @Resource
    private CommentDao commentDao;

    @Resource
    private TaskDao taskDao;

    @Resource
    private TaskRelateDao taskRelateDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ReportRecordDao reportRecordDao;


    @Test
    void contextLoads() {
    }

    @Test
    public void testPlateList() throws  Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("postId", 1);
        jsonObject.put("userId", 3);
        System.out.println(postRelateDao.existPostRelate(jsonObject));


    }

    @Test
    public void testPlateAdd() throws  Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("plateId", 6);
        jsonObject.put("tab", "All");
        jsonObject.put("currentPage", 1);
        jsonObject.put("pageSize", "10");
        JSONObject res = postService.postList(jsonObject);
        System.out.println(res);
    }

    @Test
    public void userMessageCount() throws Exception {
        int type = 0;
        long typeId = 1;
//        List<Long> reportUsers = reportRecordDao.selectReportUsers(type, typeId);

    }

}
