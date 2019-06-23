package com.jql.shiro.config;

import com.jql.realm.UserRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
@Configuration
public class ShiroConfiguration {
    // ShiroFilterFactoryBean 处理拦截问题,核心配置
    @Bean
    public ShiroFilterFactoryBean shirFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 放开请求
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/userlogin", "anon");
        // 其他的请请求拦截
        filterChainDefinitionMap.put("/**", "authc");
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return em;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    // 配置org.apache.shiro.web.session.mgt.DefaultWebSessionManager
    @Bean
    public DefaultWebSessionManager getDefaultWebSessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(getMemorySessionDAO());
        defaultWebSessionManager.setGlobalSessionTimeout(1 * 60 * 60 * 1000);
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        defaultWebSessionManager.setSessionIdCookie(getSimpleCookie());
        return defaultWebSessionManager;
    }

    // 配置org.apache.shiro.session.mgt.eis.MemorySessionDAO
    @Bean
    public MemorySessionDAO getMemorySessionDAO() {
        MemorySessionDAO memorySessionDAO = new MemorySessionDAO();
        memorySessionDAO.setSessionIdGenerator(javaUuidSessionIdGenerator());
        return memorySessionDAO;
    }

    @Bean
    public JavaUuidSessionIdGenerator javaUuidSessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    // session自定义cookie名
    @Bean
    public SimpleCookie getSimpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("security.session.id");
        simpleCookie.setPath("/");
        return simpleCookie;
    }

    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(UserRealm userRealm) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(userRealm);
        // <!-- 用户授权/认证信息Cache, 采用EhCache 缓存 -->
        dwsm.setCacheManager(getEhCacheManager());
        dwsm.setSessionManager(getDefaultWebSessionManager());
        return dwsm;
    }

    @Bean
    public UserRealm userRealm(EhCacheManager cacheManager) {
        UserRealm userRealm = new UserRealm();
        userRealm.setCacheManager(cacheManager);
        return userRealm;
    }

    // 开启shrio注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(UserRealm userRealm) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager(userRealm));
        return aasa;
    }
}
