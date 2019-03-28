package com.lvlivejp.shirotest;

import com.lvlivejp.shirosso.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomRealmTest {

    @Test
    public void testShiroAuth() {
        //1.定义默认的security管理器
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        CustomRealm customRealm = new CustomRealm();
        //2.定义校验主体
        Subject subject = SecurityUtils.getSubject();
        //3.定义校验数据来源
        defaultSecurityManager.setRealm(customRealm);

        //4.密码加密
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("lvlivejp", "123456");
        subject.login(usernamePasswordToken);

        System.out.println("isAuthenticated:" + subject.isAuthenticated());
        subject.checkRoles("admin");
        subject.checkPermission("select");
    }
}
