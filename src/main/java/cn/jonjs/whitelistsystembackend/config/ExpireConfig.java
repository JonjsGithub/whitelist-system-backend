package cn.jonjs.whitelistsystembackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpireConfig {

    @Value("${time.token}")
    public int tokenExpireInMinutes;
    @Value("${time.vcode}")
    public int vcodeExpireInMinutes;

}
