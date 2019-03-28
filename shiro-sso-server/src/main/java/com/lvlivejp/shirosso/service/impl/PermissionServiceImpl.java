package com.lvlivejp.shirosso.service.impl;

import com.lvlivejp.shirosso.config.properties.ShiroProperties;
import com.lvlivejp.shirosso.mapper.TPermissionDao;
import com.lvlivejp.shirosso.model.TPermission;
import com.lvlivejp.shirosso.service.PermissionService;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    TPermissionDao tPermissionDao;

    @Autowired
    ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired
    ShiroProperties shiroProperties;

    @Override
    public List<TPermission> findAll() {
        return tPermissionDao.selectAll();
    }

    @Override
    @Transactional
    public void insert(TPermission tPermission) {
        tPermissionDao.insert(tPermission);
        List<TPermission> all = tPermissionDao.selectAll();
        Map<String, String> collect = all.stream().collect(Collectors.toMap(TPermission::getUrl, TPermission::getPermission));
        try {
            AbstractShiroFilter shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            // 获取过滤管理器
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager filterManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            filterManager.getFilterChains().clear();
            filterManager.createChain(shiroProperties.getUnLoginHtml(), "anon");
            filterManager.createChain(shiroProperties.getUnLoginJson(), "anon");
            filterManager.createChain(shiroProperties.getUnAuthorizedHtml(), "anon");
            filterManager.createChain(shiroProperties.getUnAuthorizedJson(), "anon");
            filterManager.createChain(shiroProperties.getLoginPath(), "anon");
            filterManager.createChain("/script/**", "anon");
            filterManager.createChain("/html/**", "anon");
            collect.forEach((x,y)->filterManager.createChain(x,"perms["+y+"]"));
            filterManager.createChain("/**", "authc");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
