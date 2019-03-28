package com.lvlivejp.shirosso.config;

import com.lvlivejp.shirosso.config.properties.ShiroProperties;
import com.lvlivejp.shirosso.mapper.TPermissionDao;
import com.lvlivejp.shirosso.model.TPermission;
import com.lvlivejp.shirosso.shiro.cache.ShiroCacheManager;
import com.lvlivejp.shirosso.shiro.filter.CustomerFormAuthenticationFilter;
import com.lvlivejp.shirosso.shiro.filter.PermissionsOrAuthorizationFilter;
import com.lvlivejp.shirosso.shiro.realm.CustomRealm;
import com.lvlivejp.shirosso.shiro.session.CustomShiroWebSessionManager;
import com.lvlivejp.shirosso.core.shiro.RedisSessionDao;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    TPermissionDao permissionDao;

    @Autowired
    ShiroProperties shiroProperties;

    /**
     * 加密验证
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义验证与授权类
     *
     * @return
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
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
        defaultSecurityManager.setSessionManager(webSessionManager());
        //保存用户权限信息到缓存
        defaultSecurityManager.setCacheManager(shiroCacheManager());
        defaultSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultSecurityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        List<TPermission> permissions = permissionDao.selectAll();
        Map<String, String> permissionMap = new LinkedHashMap<>();
        permissions.stream().forEach((x)->permissionMap.put(x.getUrl(),"perms["+x.getPermission()+"]"));
        permissionMap.put(shiroProperties.getUnLoginHtml(), "anon");
        permissionMap.put(shiroProperties.getUnLoginJson(), "anon");
        permissionMap.put(shiroProperties.getUnAuthorizedHtml(), "anon");
        permissionMap.put(shiroProperties.getUnAuthorizedJson(), "anon");
        permissionMap.put(shiroProperties.getLoginPath(), "anon");
        permissionMap.put("/script/**", "anon");
        permissionMap.put("/html/**", "anon");
        permissionMap.put("/favicon.ico", "anon");
        permissionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(permissionMap);

        Map<String, Filter> filters = new HashMap<>();
        CustomerFormAuthenticationFilter filter = new CustomerFormAuthenticationFilter();
        filter.setUnLoginHtml(shiroProperties.getUnLoginHtml());
        filter.setUnLoginJson(shiroProperties.getUnLoginJson());

        PermissionsOrAuthorizationFilter permissionsOrAuthorizationFilter = new PermissionsOrAuthorizationFilter();
        permissionsOrAuthorizationFilter.setUnLoginHtml(shiroProperties.getUnLoginHtml());
        permissionsOrAuthorizationFilter.setUnLoginJson(shiroProperties.getUnLoginJson());
        permissionsOrAuthorizationFilter.setUnAuthorizedHtml(shiroProperties.getUnAuthorizedHtml());
        permissionsOrAuthorizationFilter.setUnAuthorizedJson(shiroProperties.getUnAuthorizedJson());

        filters.put("authc",filter);
        filters.put("perms",permissionsOrAuthorizationFilter);
        shiroFilterFactoryBean.setFilters(filters);

        //未登录访问资源时，返回以下url，如果是前后端分离，则该url返回json报文,不配置则返回默认的status和json报文
//        shiroFilterFactoryBean.setLoginUrl("/auth/unLogin");
        shiroFilterFactoryBean.setUnauthorizedUrl("/auth/unauthorized");
        return shiroFilterFactoryBean;

    }

    @Bean
    public RedisSessionDao redisSessionDao() {
        return new RedisSessionDao();
    }

    @Bean
    public DefaultWebSessionManager webSessionManager() {
        //使用自定义的WebSessionManager，将session保存在一个请求的Attribute中，不用频繁从Redis中获取
        CustomShiroWebSessionManager defaultWebSessionManager = new CustomShiroWebSessionManager();
//        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        //保存登录Session
        defaultWebSessionManager.setSessionDAO(redisSessionDao());
        return defaultWebSessionManager;
    }

    @Bean
    public ShiroCacheManager shiroCacheManager() {
        return new ShiroCacheManager();
    }

    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        Cookie cookie = new SimpleCookie();
        cookie.setName("springbootRememberMe");
        cookie.setMaxAge(60 * 60 * 24);
        rememberMeManager.setCookie(cookie);
        return rememberMeManager;
    }

}
