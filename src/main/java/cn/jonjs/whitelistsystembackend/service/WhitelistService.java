package cn.jonjs.whitelistsystembackend.service;

import org.glavo.rcon.AuthenticationException;

import java.io.IOException;

public interface WhitelistService {

    int queryNowPermission(String name);

    void addWhitelist(String name) throws AuthenticationException, IOException;

    void removeWhitelist(String name) throws AuthenticationException, IOException;

    String connectTest() throws AuthenticationException, IOException;

}
