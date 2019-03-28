package com.lvlivejp.shirosso.shiro.realm;

import com.lvlivejp.shirosso.mapper.TRolePermissionDao;
import com.lvlivejp.shirosso.mapper.TUserDao;
import com.lvlivejp.shirosso.mapper.TUserRoleDao;
import com.lvlivejp.shirosso.model.TPermission;
import com.lvlivejp.shirosso.model.TUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private TUserDao tUserDao;

    @Autowired
    private TUserRoleDao tUserRoleDao;

    @Autowired
    private TRolePermissionDao tRolePermissionDao;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
//        List<String> roles = tUserRoleDao.getRoleNameByUserName(username);
        List<TPermission> permission = tRolePermissionDao.getPerNameByUserName(username);
        List<String> roles = Arrays.asList();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(new HashSet<>(roles));
        simpleAuthorizationInfo.setStringPermissions(permission.stream().map(TPermission::getPermission).collect(Collectors.toSet()));
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        TUser tUser = new TUser();
        tUser.setUserName(username);
        List<TUser> tUserList = tUserDao.select(tUser);
        if (tUserList.isEmpty()) {
            return null;
        }
        tUser = tUserList.get(0);
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(tUser.getUserName(), tUser.getPassword(), super.getName());
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(tUser.getUserName()));
        return simpleAuthenticationInfo;

    }
}
