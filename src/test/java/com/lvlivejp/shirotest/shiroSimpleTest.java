package com.lvlivejp.shirotest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class shiroSimpleTest {

    private SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void generateAccount() {
        simpleAccountRealm.addAccount("lvlivejp", "123456", "admin");
    }

    @Test
    public void testShiroAuth() {
        //1.定义默认的security管理器
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //2.定义校验主体
        Subject subject = SecurityUtils.getSubject();
        //3.定义校验数据来源
        defaultSecurityManager.setRealm(simpleAccountRealm);

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("lvlivejp", "123456");
        subject.login(usernamePasswordToken);

        System.out.println("isAuthenticated:" + subject.isAuthenticated());
        subject.checkRoles("admin1");
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456", "lv");
        System.err.println(md5Hash.toString());
    }
}
