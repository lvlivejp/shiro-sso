package com.lvlivejp.shirosso.config;

import com.lvlivejp.shirosso.config.properties.ShiroProperties;
import com.lvlivejp.shirosso.core.base.ShiroSsoConstant;
import com.lvlivejp.shirosso.shiro.authc.ShiroSsoCredentialsMatcher;
import com.lvlivejp.shirosso.shiro.authc.ShiroSsoToken;
import com.lvlivejp.shirosso.shiro.filter.CustomerFormAuthenticationFilter;
import com.lvlivejp.shirosso.shiro.filter.PermissionsOrAuthorizationFilter;
import com.lvlivejp.shirosso.shiro.realm.CustomRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {


    @Autowired
    ShiroProperties shiroProperties;

    @Bean
    public ShiroSsoCredentialsMatcher shiroSsoCredentialsMatcher(){
        return new ShiroSsoCredentialsMatcher();
    }

    /**
     * 自定义验证与授权类
     *
     * @return
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(shiroSsoCredentialsMatcher());
        customRealm.setAuthenticationTokenClass(ShiroSsoToken.class);
        return customRealm;
    }

    /**
     * 验证管理器
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(customRealm());

        /**
         * 同一域名时，防止客户端的sessionId覆盖SSO Server的SessionId
         */
        Cookie cookie = new SimpleCookie(ShiroSsoConstant.SSO_CLIENT_ID);
        cookie.setHttpOnly(true); //more secure, protects against XSS attacks

        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionIdCookie(cookie);
        defaultSecurityManager.setSessionManager(defaultWebSessionManager);
        //保存用户权限信息到缓存
        return defaultSecurityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> permissionMap = new LinkedHashMap<>();
        permissionMap.put(shiroProperties.getUnAuthorizedJson(), "anon");
        permissionMap.put("/script/**", "anon");
        permissionMap.put("/html/**", "anon");
        permissionMap.put("/favicon.ico", "anon");
        permissionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(permissionMap);

        Map<String, Filter> filters = new HashMap<>();
        CustomerFormAuthenticationFilter filter = new CustomerFormAuthenticationFilter();
        filter.setLoginHtmlUrl(shiroProperties.getUnLoginHtml());
        filter.setSsoServerTokenCheckUrl(shiroProperties.getSsoServerTokenCheckUrl());
        PermissionsOrAuthorizationFilter permissionsOrAuthorizationFilter = new PermissionsOrAuthorizationFilter();
        permissionsOrAuthorizationFilter.setUnLoginHtml(shiroProperties.getUnLoginHtml());
        permissionsOrAuthorizationFilter.setUnAuthorizedHtml(shiroProperties.getUnAuthorizedHtml());
        permissionsOrAuthorizationFilter.setUnAuthorizedJson(shiroProperties.getUnAuthorizedJson());

        filters.put("authc",filter);
        filters.put("perms",permissionsOrAuthorizationFilter);
        shiroFilterFactoryBean.setFilters(filters);

        //未登录访问资源时，返回以下url，如果是前后端分离，则该url返回json报文,不配置则返回默认的status和json报文
//        shiroFilterFactoryBean.setLoginUrl("/auth/unLogin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/auth/unauthorized");
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getUnLoginHtml());
        return shiroFilterFactoryBean;

    }

}
