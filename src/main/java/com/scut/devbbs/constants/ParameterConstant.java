package com.scut.devbbs.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.scut")
@PropertySource("classpath:constant.properties")
public class ParameterConstant {
    //请求必要参数表

    //登陆参数
    private String[] loginParameter;
    //注册参数
    private String[] registerParameter;
    //版块信息参数：添加版块、更新版块
    private String[] addPlateParameter;
    //新增版块关联参数：关注、管理
    private String[] plateRelateParameter;
    private String[] myPlateParameter;
    private String[] addPostParameter;
    private String[] postListParameter;
    private String[] postRelateParameter;
    private String[] addCommentParameter;
    private String[] commentListParameter;
    private String[] commentRelateParameter;
    private String[] rewardAssignParameter;
    private String[] userListParameter;
    private String[] addUserRelateParameter;
    private String[] userRelateListParameter;
    private String[] userMessageListParameter;
    private String[] addFriendApplyParameter;
    private String[] updateFriendApplyParameter;
    private String[] addMailParameter;
    private String[] mailListParameter;
    private String[] sysMailListParameter;
    private String[] sendedMailListParameter;
    private String[] addTaskRelateParameter;
    private String[] addTaskParameter;
    private String[] updateTaskParameter;
    private String[] finishTaskParameter;

    //更新帖子的内容
    private String[] updatePostContentParameter;

    //发帖页面参数
    private String[] publishInfoParameter;
    //版块举报列表
    private String[] plateReportListParameter;
    //用户举报列表
    private String[] userReportListParameter;
    //忽略举报
    private String[] ignoreReportParameter;
    //举报处理
    private String[] dealReportParameter;
    //封禁用户
    private String[] banUserParameter;
    //用户封禁记录
    private String[] userPunishListParameter;
    //用户撤销封禁
    private String[] cancelPunishParameter;


    private String[] plateNameExistParameter;
    private String[] plateUpdateStateParameter;
    private String[] queryUserParameter;

    //搜索帖子
    private String[] searchPostParameter;
    //用户发送举报
    private String[] userReportParameter;
    //添加首页广告
    private String[] addNewsParameter;
    //更新广告
    private String[] updateNewsParameter;
    //更新广告启用状态
    private String[] updateNewsValidParameter;
    //删除广告
    private String[] deleteNewsParameter;
}
