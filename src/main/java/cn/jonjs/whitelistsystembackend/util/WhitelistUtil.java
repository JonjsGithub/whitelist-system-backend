package cn.jonjs.whitelistsystembackend.util;

import cn.jonjs.whitelistsystembackend.config.RconConfig;
import org.glavo.rcon.AuthenticationException;
import org.glavo.rcon.Rcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WhitelistUtil {

    @Autowired
    private RconConfig rconConfig;

    public void addWhitelist(String name) throws AuthenticationException, IOException {
        Rcon rcon = new Rcon(rconConfig.host, rconConfig.port, rconConfig.password);
        String result = rcon.command("whitelist add " + name);
        rcon.disconnect();
    }

    public void removeWhitelist(String name) throws AuthenticationException, IOException {
        Rcon rcon = new Rcon(rconConfig.host, rconConfig.port, rconConfig.password);
        String result = rcon.command("whitelist remove " + name);
        rcon.disconnect();
    }

    public String connectTest() throws AuthenticationException, IOException {
        Rcon rcon = new Rcon(rconConfig.host, rconConfig.port, rconConfig.password);
        String result = rcon.command("list");
        rcon.disconnect();
        return result;
    }
}
