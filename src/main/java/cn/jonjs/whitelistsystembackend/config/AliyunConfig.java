package cn.jonjs.whitelistsystembackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunConfig {

    @Value("${aliyun.access-key-id}")
    public String ACCESS_KEY_ID;
    @Value("${aliyun.access-key-secret}")
    public String ACCESS_KEY_SECRET;
    @Value("${aliyun.mail-html}")
    public String MAIL_HTML;

}
