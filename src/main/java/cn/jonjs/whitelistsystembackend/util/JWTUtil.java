package cn.jonjs.whitelistsystembackend.util;

import cn.jonjs.whitelistsystembackend.config.ExpireConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {

    @Autowired
    private ExpireConfig expireConfig;


    private static final String key = "ColorfulWorld-JWTPassword";
    public static Date expires = null;

    public int getExpireMin() {
        return expireConfig.tokenExpireInMinutes;
    }

    public String generateToken(Map<String, Object> claims) {
        expires = new Date(System.currentTimeMillis() + 1000L * 60 * expireConfig.tokenExpireInMinutes);
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(expires)
                .sign(Algorithm.HMAC256(key));
    }

    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(key))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

}
