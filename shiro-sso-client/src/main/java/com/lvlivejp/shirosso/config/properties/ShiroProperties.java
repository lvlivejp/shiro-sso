package com.lvlivejp.shirosso.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ToString
@ConfigurationProperties(prefix = "shiro")
@Configuration
public class ShiroProperties {
    private String unLoginHtml;
    private String unAuthorizedHtml;
    private String unAuthorizedJson;
    private String logoutPath;
    private String ssoServerTokenCheckUrl;
}
