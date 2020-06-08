package com.scut.devbbs.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.scut.devbbs.annotation.AuthToken;
import com.scut.devbbs.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private String httpHeaderName = "Authorization";

    private String unauthorizedErrorMessage = "401 unauthorized";

    private int unauthorizedErrorCode = HttpServletResponse.SC_UNAUTHORIZED;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws  Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.getAnnotation(AuthToken.class) != null || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {
            String token = request.getHeader(httpHeaderName);

            log.info("Get token from request is {} ", token);
            String email = "";
            Jedis jedis = new Jedis(Constant.REDIS_ADDRESS, Constant.REDIS_PORT);
            if (token != null && token.length() != 0) {
                email = jedis.get(token);
                log.info("Get email from Redis is {}", email);
            }
            if (email != null && !email.trim().equals("")) {
                //log.info("token birth time is: {}",jedis.get(username+token));
                Long tokeBirthTime = Long.valueOf(jedis.get(token + email));
                log.info("token Birth time is: {}", tokeBirthTime);
                Long diff = System.currentTimeMillis() - tokeBirthTime;
                log.info("token is exist : {} ms", diff);
                //重新设置Redis中的token过期时间
                if (diff > Constant.TOKEN_REST_TIME) {
                    jedis.expire(email, Constant.TOKEN_EXPIRE_TIME);
                    jedis.expire(token, Constant.TOKEN_EXPIRE_TIME);
                    log.info("Reset expire time success!");
                    Long newBirthTime = System.currentTimeMillis();
                    jedis.set(token + email, newBirthTime.toString());
                }

                //用完关闭
                jedis.close();
                return true;
            } else {
                JSONObject jsonObject = new JSONObject();
                PrintWriter out = null;
                try {
                    response.setStatus(unauthorizedErrorCode);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                    jsonObject.put("code", ((HttpServletResponse) response).getStatus());
                    jsonObject.put("message", HttpStatus.UNAUTHORIZED);
                    out = response.getWriter();
                    out.println(jsonObject);
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != out) {
                        out.flush();
                        out.close();
                    }
                }

            }
        }
        return true;
    }
}
