package com.scut.devbbs.constants;

public final class Constant {
    //用户默认头像图片路径
    public static final String USER_IMG_URL = "/userImg/default.jpg";

    //默认板块图片
    public static final String PLATE_IMG_URL = "/plateImg/default.jpg";

    //1天的时间戳值
    public static final long DAY_TIMESTAMP = 1000 * 60 * 60 * 24;

    //redis地址
    public static final String REDIS_ADDRESS = "127.0.0.1";
    public static final int REDIS_PORT = 6379;

    //token生存时间（/秒）
    public static final int TOKEN_EXPIRE_TIME = 600;

    //token重置界限（/毫秒）
    public static final int TOKEN_REST_TIME = 100000;
}
