package com.lvlivejp.shirosso.core.base;

public class ShiroSsoConstant {

    /**
     * sso server服务中session在redis中的前缀
     */
    public final static String SHIRO_SESSION_PREFIX = "shiro_session_id:";

    /**
     * sso server服务中token在redis中的前缀
     */
    public final static String SHIRO_TOKEN_PREFIX = "shiro_token_id:";

    /**
     * 重定向参数名
     */
    public final static String SHIRO_REDIRECT_URL="redirectUrl";

    /**
     * token的参数名
     */
    public static final String SHIRO_SSO_CLIENT_TOKEN="token";

    /**
     * sso client中保存cookie的参数名
     * 防止和sso server同一域名时，sso server的sessionId被覆盖
     */
    public static final String SSO_CLIENT_ID="SSO_CLIENT_ID";

}
