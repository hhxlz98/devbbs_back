package com.scut.devbbs.util;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.constants.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

@Slf4j
public class CommonUtil {
    /**
     * 将request参数值转为json
     */
    public static JSONObject request2Json(HttpServletRequest request) {
        JSONObject requestJson = new JSONObject();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] pv = request.getParameterValues(paramName);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pv.length; i++) {
                if (pv[i].length() > 0) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(pv[i]);
                }
            }
            requestJson.put(paramName, sb.toString());
        }
        return requestJson;
    }

    //wangEditor特定的编辑器图片上传返回格式
    public static JSONObject imgUploadSuccess(Object info){
        JSONObject resultJson = new JSONObject();
        resultJson.put("errno", "0");
        resultJson.put("data",info);
        return resultJson;
    }

    //简单的错误返回
    public static JSONObject errorJson(String info){
        JSONObject resultJson = new JSONObject();
        resultJson.put("msg", info);
        return resultJson;
    }

    //普通的带状态码响应，附带info信息
    public static JSONObject commonReturn(ResponseEnum responseEnum, Object info){
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", responseEnum.getCode());
        resultJson.put("msg", responseEnum.getMsg());
        resultJson.put("info", info);
        return resultJson;
    }

    //根据函数参数数组确认请求参数是否满足
    public static boolean requestParameterCheck(JSONObject requestJson, String[] parameters) {
        log.info("请求参数：{}", requestJson);
        for (String parameter : parameters) {
            if(requestJson.get(parameter) == null ) {
                log.warn("参数{}缺失", parameter);
                return false;
            }
        }
        return true;
    }

    //参数不完全时，返回错误信息
    public static JSONObject returnParameterErrorInfo(String[] parameters) {
        String info = "参数异常，请求参数为" + Arrays.toString(parameters);
        return CommonUtil.commonReturn(ResponseEnum.E_101, info);
    }

    public static String generateToken(String... strings) {
        long timestamp = System.currentTimeMillis();
        String tokenMeta = "";
        for (String s : strings) {
            tokenMeta = tokenMeta + s;
        }
        tokenMeta = tokenMeta + timestamp;
        String token = DigestUtils.md5DigestAsHex(tokenMeta.getBytes());
        return token;
    }
}
