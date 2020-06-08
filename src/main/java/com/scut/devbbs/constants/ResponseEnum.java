package com.scut.devbbs.constants;

public enum  ResponseEnum {

    E_101("101","请求参数异常"),

    //账号登录
    E_102("102","登录成功"),
    E_103("103","密码错误"),
    E_104("104", "账号不存在"),

    //账号注册
    E_105("105", "已存在"),
    E_106("106", "注册成功"),

    //板块相关
    E_201("201", "返回板块列表"),
    E_202("202", "版块名已存在"),
    E_203("203", "更新版块信息"),
    E_204("204", "更新版块状态"),
    E_205("205","板块已存在"),
    E_206("206","板块创建成功"),
    E_207("207","板块信息查询成功"),
    E_208("208", "返回版块列表+管理名单"),
    E_209("209", "版块权限不够"),

    //板块联系
    E_302("302", "联系创建成功"),
    E_303("303","联系删除成功"),
    E_304("304", "联系不存在"),
    E_305("305", "关系已存在"),

    //帖子
    E_402("402", "帖子创建成功"),
    E_403("403", "返回帖子列表"),
    E_406("406", "返回帖子内容"),
    E_407("407", "删除帖子关联成功"),
    E_408("408", "帖子关联创建成功"),
    E_409("409", "帖子关联已存在"),
    E_410("410", "帖子关联不存在"),
    E_411("411", "返回用户帖子列表"),
    E_412("412", "更新帖子状态"),
    E_413("413", "返回搜索帖子列表"),
    E_414("414", "返回发帖数据"),
    E_415("415", "更新帖子内容"),
    E_416("416", "最新帖子"),
    E_417("417", "热门帖子"),

    //评论
    E_602("602", "评论添加成功"),
    E_603("603", "查询评论列表成功"),
    E_604("604", "评论联系创建成功"),
    E_605("605", "评论联系删除成功"),
    E_606("606", "最佳结帖成功"),
    E_607("607", "热心结帖成功"),
    E_608("608", "热心奖励已经分配完了"),
    E_609("609", "返回用户评论"),
    E_610("610", "返回用户关联评论"),
    E_611("611", "返回用户信消息数目"),
    E_612("612", "更新评论状态"),

    E_701("701", "用户信息返回"),
    E_702("702", "修改用户签名成功"),
    E_703("703", "修改用户名成功"),
    E_704("704", "修改用户头像成功"),
    E_705("705", "用户信息更新成功"),
    E_706("706","用户名已存在"),
    E_707("707", "缺少修改次数"),
    E_708("708", "返回查询用户"),

    E_801("801", "用户关系添加成功"),
    E_802("802", "用户关系已存在"),
    E_803("803", "用户关系删除成功"),
    E_804("804", "用户关系不存在"),
    E_805("805", "返回用户关系列表"),
    E_806("806", "好友申请发送成功"),
    E_807("807", "好友申请已存在"),
    E_808("808", "好友申请状态已更新"),
    E_809("809", "返回申请列表"),

    E_901("901", "邮件创建成功"),
    E_902("902", "更新邮件状态成功"),
    E_903("903", "邮件列表返回成功"),
    E_904("904", "更新邮件的fromdelete"),

    E_1001("1001", "返回可接任务列表"),
    E_1002("1002", "领取任务成功"),
    E_1003("1003", "领取任务奖励成功"),
    E_1004("1004", "进行中任务列表"),
    E_1005("1005", "已完成任务列表"),
    E_1006("1006", "任务创建成功"),
    E_1007("1007", "任务更新成功"),
    E_1008("1008", "全部任务列表"),
    E_1009("1009", "任务删除成功"),

    E_1101("1101", "举报成功"),
    E_1102("1102", "版块举报列表"),
    E_1103("1103", "举报已处理"),
    E_1104("1104", "用户举报列表"),
    E_1105("1105", "用户封禁成功"),
    E_1106("1106", "用户封禁信息"),
    E_1107("1107", "撤销处罚成功"),

    E_1201("1201", "广告添加成功"),
    E_1202("1202", "广告更新信息成功"),
    E_1203("1203", "广告启用状态已更新"),
    E_1204("1204", "广告全部列表"),
    E_1205("1205", "启用广告列表"),
    E_1206("1206", "广告已删除"),


    //上传
    E_504("504", "文件为空"),
    E_505("505", "文件创建失败"),
    E_506("506", "上传成功");

    private String code;

    private String msg;

    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
