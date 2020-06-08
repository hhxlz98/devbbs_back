package com.scut.devbbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.Constant;
import com.scut.devbbs.constants.ParameterConstant;
import com.scut.devbbs.dao.CommentDao;
import com.scut.devbbs.dao.PostDao;
import com.scut.devbbs.dao.UserDao;
import com.scut.devbbs.dao.UserRelateDao;
import com.scut.devbbs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.scut.devbbs.util.CommonUtil;
import com.scut.devbbs.constants.ResponseEnum;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private PostDao postDao;

    @Resource
    private CommentDao commentDao;

    @Resource
    private UserRelateDao userRelateDao;

    @Autowired
    private ParameterConstant parameterConstant;

    @Override
    public JSONObject userLogin(JSONObject jsonObject) {
        String info = "";
        String[] parameters = parameterConstant.getLoginParameter();
        if (!CommonUtil.requestParameterCheck(jsonObject, parameters)){
            return CommonUtil.returnParameterErrorInfo(parameters);
        } else {
            String userEmail = jsonObject.getString("userEmail");
            String loginPass = jsonObject.getString("password");
            JSONObject queryUser = userDao.getUser(userEmail);
            if (queryUser == null) {
                info = userEmail + "用户不存在";
                log.warn("登陆邮箱{}不存在", userEmail);
                return CommonUtil.commonReturn(ResponseEnum.E_104, info);
            } else {
                BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
                if (encode.matches(loginPass, queryUser.getString("userPassword"))) {
                    String token = CommonUtil.generateToken(userEmail, loginPass);
                    Jedis jedis = new Jedis(Constant.REDIS_ADDRESS, Constant.REDIS_PORT);
                    jedis.set(userEmail, token);
                    //设置key生存时间，当key过期时，它会被自动删除，时间是秒
                    jedis.expire(userEmail, Constant.TOKEN_EXPIRE_TIME);
                    jedis.set(token, userEmail);
                    jedis.expire(token, Constant.TOKEN_EXPIRE_TIME);
                    Long currentTime = System.currentTimeMillis();
                    jedis.set(token + userEmail, currentTime.toString());

                    //用完关闭
                    jedis.close();
                    queryUser.put("token", token);
                    log.info("用户{}登陆成功，分配token为{}", userEmail, token);
                    info = "登录成功，密码匹配正确";
                    return CommonUtil.commonReturn(ResponseEnum.E_102, queryUser);
                } else {
                    info = "密码匹配错误";
                    log.warn("用户{}登陆密码错误", userEmail);
                    return CommonUtil.commonReturn(ResponseEnum.E_103, info);
                }
            }
        }
    }

    @Override
    public JSONObject registerUser(JSONObject jsonObject) {
        System.out.println(jsonObject);
        String info = "";
        String[] parameters = parameterConstant.getRegisterParameter();
        if ( !CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        } else {
            String userEmail = jsonObject.getString(parameters[0]);
            String userName = jsonObject.getString(parameters[1]);
            String userPass = jsonObject.getString(parameters[2]);
            if (emailRepeat(userEmail)) {
                info = userEmail + "邮箱已注册";
                log.warn("邮箱{}已注册", userEmail);
                return CommonUtil.commonReturn(ResponseEnum.E_105, info);
            } else if (usernameRepeat(userName)) {
                info = userName + "用户名重复";
                log.warn("用户名{}已使用", userName);
                return CommonUtil.commonReturn(ResponseEnum.E_105, info);
            }
            long registerTime = new Date().getTime();
            jsonObject.put("time", registerTime);
            jsonObject.put("points", 20);
            jsonObject.put("status", 0);
            jsonObject.put("changeCount", 2);
            jsonObject.put("userImage", Constant.USER_IMG_URL);

            if (jsonObject.get("userShow") == null) {
                jsonObject.put("userShow", "未设置个人签名");
            }
            if (jsonObject.get("userIntro") == null) {
                jsonObject.put("userIntro", "暂无");
            }
            if (jsonObject.get("userSex") == null) {
                jsonObject.put("userSex", " ");
            }
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            jsonObject.put("userPassword", bCryptPasswordEncoder.encode(userPass));
            System.out.println(jsonObject);
            userDao.addUser(jsonObject);
            JSONObject user = userDao.getUser(userEmail);
            info = "注册成功";
            log.info("用户：邮箱{}，用户名{}注册成功", userEmail, userName);
            return CommonUtil.commonReturn(ResponseEnum.E_106, user);
        }

    }

    @Override
    public boolean emailRepeat(String email) {
        JSONObject resultJson = userDao.existEmail(email);
        if (resultJson == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean usernameRepeat(String username) {
        JSONObject resultJson = userDao.existUsername(username);
        if (resultJson== null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public JSONObject updateUserShow(JSONObject jsonObject) {
        long userId = jsonObject.getLong("userId");
        if (userId > 0) {
            if (jsonObject.get("userShow") != null) {
                userDao.updateUserShow(jsonObject);
                log.info("用户id：{}修改签名成功", userId);
                return CommonUtil.commonReturn(ResponseEnum.E_702, jsonObject);
            }
            log.warn("用户id：{}修改签名失败，缺少userShow参数", userId);
            return CommonUtil.errorJson("签名不存在");
        }
        log.warn("用户Id：{}不合法");
        return CommonUtil.errorJson("用户Id不合法");
    }

    @Override
    public JSONObject updateUserImg(JSONObject jsonObject) {
        userDao.updateUserImg(jsonObject);
        log.info("用户{}修改头像为{}", jsonObject.getLong("userId"), jsonObject.getString("imgUrl"));
        return CommonUtil.commonReturn(ResponseEnum.E_704, jsonObject);
    }

    @Override
    public JSONObject updateUserName(JSONObject jsonObject) {
        long userId = jsonObject.getLong("userId");
        if (userId > 0) {
            if (jsonObject.get("userName") != null) {
                if (usernameRepeat(jsonObject.getString("userName"))) {
                    log.warn("用户Id{}修改用户名{}重复", userId, jsonObject.get("userName"));
                    return CommonUtil.commonReturn(ResponseEnum.E_706, jsonObject);
                }
                JSONObject user = userDao.queryUserById(userId);
                if (user.getIntValue("changeCount") < 1) {
                    log.warn("用户Id{}修改用户名失败，无修改次数", userId);
                    return CommonUtil.commonReturn(ResponseEnum.E_707, jsonObject);
                }
                userDao.updateUserName(jsonObject);
                log.info("用户Id{}修改用户名{}成功", userId, jsonObject.get("userName"));
                return CommonUtil.commonReturn(ResponseEnum.E_703, jsonObject);
            }
            log.warn("用户Id{}修改用户名失败，userName参数缺失", userId);
            return CommonUtil.errorJson("用户名不存在");
        }
        log.warn("用户Id{}不合法", userId);
        return CommonUtil.errorJson("用户Id不合法");
    }

    @Override
    public JSONObject updateUserInfo(JSONObject jsonObject) {
        long userId = jsonObject.getLong("userId");
        if (userId > 0) {
            if (jsonObject.get("userSex") == null) {
                return CommonUtil.errorJson("性别参数不存在");

            } else if (jsonObject.get("userIntro") == null) {
                return CommonUtil.errorJson("个人介绍不存在");
            }
            userDao.updateUserInfo(jsonObject);
            return CommonUtil.commonReturn(ResponseEnum.E_705, jsonObject);
        }
        return CommonUtil.errorJson("用户Id不合法");
    }

    @Override
    public JSONObject userInfo(JSONObject jsonObject) {
        long userId = jsonObject.getLong("userId");
        long myUserId = jsonObject.getLong("myUserId");
        if (userId > 0) {
            JSONObject user = userDao.queryUserById(userId);
            String info = "";
            if (user != null) {
                int publishCount = postDao.countPostForUser(userId);
                int goodCount = postDao.countGoodForUser(userId);
                int replyCount = commentDao.countCommentForUser(userId);
                int bestCount = commentDao.countBestForUser(userId);
                user.put("publishCount", publishCount);
                user.put("goodCount", goodCount);
                user.put("replyCount", replyCount);
                user.put("bestCount", bestCount);
                int postLikeNumber = postDao.postLikeNumberForUser(userId);
                int commentLikeNumber = commentDao.commentLikeNumberForUser(userId);
                user.put("postLikeNumber", postLikeNumber);
                user.put("commentLikeNumber", commentLikeNumber);
                user.put("isFriend", false);
                if (myUserId > 0 && userRelateDao.existUserRelate(myUserId, userId, 0) != null) {
                    user.put("isFriend", true);
                }
                return CommonUtil.commonReturn(ResponseEnum.E_701, user);
            } else {
                info = "用户名不存在：" + userId;
                return CommonUtil.errorJson(info);
            }
        }
        return CommonUtil.errorJson("用户ID不合法");
    }

    @Override
    public JSONObject queryUser(JSONObject jsonObject) {
        String info = "";
        String[] parameters = parameterConstant.getQueryUserParameter();
        if ( !CommonUtil.requestParameterCheck(jsonObject, parameters)) {
            return CommonUtil.returnParameterErrorInfo(parameters);
        }
        String type = jsonObject.getString("type");
        List<JSONObject> userList = new ArrayList<JSONObject>();
        if (type.equals("userId")) {
            userList.add(userDao.queryUserById(jsonObject.getLong("content")));
        } else if (type.equals("userEmail")) {
            userList.add(userDao.queryUserByEmail(jsonObject.getString("content")));
        } else if (type.equals("userName")) {
            userList = userDao.queryUserByUserName(jsonObject.getString("content"));
        }
        return CommonUtil.commonReturn(ResponseEnum.E_708, userList);
    }


}
