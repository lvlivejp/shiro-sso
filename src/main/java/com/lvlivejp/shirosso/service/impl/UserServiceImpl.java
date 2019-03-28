package com.lvlivejp.shirosso.service.impl;

import com.lvlivejp.shirosso.base.BaseResult;
import com.lvlivejp.shirosso.mapper.TUserDao;
import com.lvlivejp.shirosso.model.TUser;
import com.lvlivejp.shirosso.service.UserService;
import com.lvlivejp.shirosso.utils.BaseResultUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TUserDao tUserDao;

    @Autowired
    private HashedCredentialsMatcher hashedCredentialsMatcher;

    @Override
    public BaseResult save(TUser tUser) {
        SimpleHash simpleHash = new SimpleHash(hashedCredentialsMatcher.getHashAlgorithmName(), tUser.getPassword(), tUser.getUserName(), hashedCredentialsMatcher.getHashIterations());
        // 记得注册的时候把密码加密一下
        tUser.setPassword(simpleHash.toString());
        tUserDao.insert(tUser);
        return BaseResultUtils.success(tUser);
    }
}
