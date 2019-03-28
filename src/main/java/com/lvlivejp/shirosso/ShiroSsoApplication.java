package com.lvlivejp.shirosso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("com.lvlivejp.shirosso.mapper")
public class ShiroSsoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiroSsoApplication.class, args);
    }

}
