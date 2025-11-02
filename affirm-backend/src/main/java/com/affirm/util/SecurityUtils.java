package com.affirm.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * 安全工具类，用于获取当前认证用户信息
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户的用户名
     * @return 用户名，如果未登录则返回null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject();
        }
        return null;
    }

    /**
     * 获取当前JWT token
     * @return Jwt对象，如果未登录则返回null
     */
    public static Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            return (Jwt) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 检查当前用户是否已认证
     * @return true如果已认证，false如果未认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null 
            && authentication.isAuthenticated() 
            && authentication.getPrincipal() instanceof Jwt;
    }
}

