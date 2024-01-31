package cn.jonjs.whitelistsystembackend.service.impl;

import cn.jonjs.whitelistsystembackend.mapper.WebsiteMapper;
import cn.jonjs.whitelistsystembackend.pojo.User;
import cn.jonjs.whitelistsystembackend.pojo.Website;
import cn.jonjs.whitelistsystembackend.service.WebsiteService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebsiteServiceImpl implements WebsiteService {

    @Autowired
    private WebsiteMapper websiteMapper;

    @Override
    public List<Website> getAllConfig() {
        QueryWrapper<Website> wrapper = new QueryWrapper<>();
        wrapper.select("*");
        return websiteMapper.selectList(wrapper);
    }

    @Override
    public String getConfig(String key) {
        List<Website> websites = getAllConfig();
        for (Website website : websites) {
            if (website.getK().equals(key)) {
                return website.getV();
            }
        }
        return "";
    }

    @Override
    public void set(Website website) {
        UpdateWrapper<Website> wrapper = new UpdateWrapper<Website>()
                .set("v", website.getV())
                .eq("k", website.getK());
        websiteMapper.update(wrapper);
    }
}
