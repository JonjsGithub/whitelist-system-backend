package cn.jonjs.whitelistsystembackend.service;

import cn.jonjs.whitelistsystembackend.pojo.Website;

import java.util.List;

public interface WebsiteService {
    List<Website> getAllConfig();

    String getConfig(String key);

    void set(Website website);
}
