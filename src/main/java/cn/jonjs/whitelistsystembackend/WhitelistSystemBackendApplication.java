package cn.jonjs.whitelistsystembackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.jonjs.whitelistsystembackend.mapper")
public class WhitelistSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhitelistSystemBackendApplication.class, args);
    }

}
