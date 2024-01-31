package cn.jonjs.whitelistsystembackend.util;

import cn.jonjs.whitelistsystembackend.config.ExpireConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class FindPwdUtil {

    @Autowired
    private ExpireConfig expireConfig;

    private final String key = "ColorfulWorld-FindPassword";
    public Date expires = null;

    public int getExpireMin() {
        return (int) expireConfig.vcodeExpireInMinutes;
    }

    public String generateVCode(Map<String, Object> claims) {
        expires = new Date(System.currentTimeMillis() + 1000L * 60 * expireConfig.vcodeExpireInMinutes);
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(expires)
                .sign(Algorithm.HMAC256(key));
    }

    public Map<String, Object> parseVCode(String token) {
        return JWT.require(Algorithm.HMAC256(key))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }


}
