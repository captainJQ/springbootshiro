package com.jql.realm;

import com.jql.entity.User;
import com.jql.mapper.UserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SessionDAO sessionDao;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();

        // 授权
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<String> roles = userMapper.getRolesByUserName(username);
        simpleAuthorizationInfo.addRoles(roles);
//        simpleAuthorizationInfo.addRoles();\
        List<String> permissions = userMapper.selectPermissionsByRoleId(1);
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        Collection<Session> sessions = sessionDao.getActiveSessions();
        for (Session session : sessions) {
            String loginedUsername = String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
            if(loginedUsername.equals(username)){
                session.setTimeout(0);
                break;
            }
        }
        User user = userMapper.getUserByName(username);
        if (user != null) {
            SimpleAuthenticationInfo authInfo = new SimpleAuthenticationInfo(username, user.getPassword(),
                    "userRealm");
            return authInfo;
        }
        return null;
    }
}
