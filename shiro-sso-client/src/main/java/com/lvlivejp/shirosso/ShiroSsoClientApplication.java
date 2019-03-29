package com.lvlivejp.shirosso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
public class ShiroSsoClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiroSsoClientApplication.class, args);
    }


}
