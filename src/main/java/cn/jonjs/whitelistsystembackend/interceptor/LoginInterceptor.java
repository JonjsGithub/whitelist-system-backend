package cn.jonjs.whitelistsystembackend.interceptor;

import cn.jonjs.whitelistsystembackend.pojo.Result;
import cn.jonjs.whitelistsystembackend.util.JWTUtil;
import cn.jonjs.whitelistsystembackend.util.RedisUtils;
import cn.jonjs.whitelistsystembackend.util.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //token验证
        String token = request.getHeader("Authorization");
        try {
            String redisToken = RedisUtils.get(stringRedisTemplate, token);
            if (redisToken == null) {
                throw new RuntimeException("登录已失效, 请重新登录");
            }
            Map<String, Object> claims = JWTUtil.parseToken(token);
            //业务数据储存
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
