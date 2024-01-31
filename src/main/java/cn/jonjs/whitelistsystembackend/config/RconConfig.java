package cn.jonjs.whitelistsystembackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RconConfig {

    @Value("${rcon.host}")
    public String host;
    @Value("${rcon.port}")
    public int port;
    @Value("${rcon.password}")
    public String password;

    @Value("${rcon.pal.host}")
    public String palHost;
    @Value("${rcon.pal.port}")
    public int palPort;
    @Value("${rcon.pal.password}")
    public String palPassword;

}
