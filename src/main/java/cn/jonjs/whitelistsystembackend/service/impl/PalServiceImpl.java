package cn.jonjs.whitelistsystembackend.service.impl;

import cn.jonjs.whitelistsystembackend.service.PalService;
import cn.jonjs.whitelistsystembackend.util.PalUtil;
import cn.jonjs.whitelistsystembackend.util.WhitelistUtil;
import org.glavo.rcon.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PalServiceImpl implements PalService {

    @Autowired
    private PalUtil palUtil;

    @Override
    public String send(String command) throws AuthenticationException, IOException {
        return palUtil.send(command);
    }

    @Override
    public String connectTest() throws AuthenticationException, IOException {
        return palUtil.connectTest();
    }
}
