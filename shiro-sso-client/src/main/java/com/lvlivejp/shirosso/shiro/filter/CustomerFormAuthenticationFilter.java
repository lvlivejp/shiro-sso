package com.lvlivejp.shirosso.shiro.filter;

import com.lvlivejp.shirosso.core.base.ShiroSsoConstant;
import com.lvlivejp.shirosso.core.utils.CookieUtils;
import com.lvlivejp.shirosso.core.utils.SpringBootBeanUtils;
import com.lvlivejp.shirosso.shiro.authc.ShiroSsoCredentialsMatcher;
import com.lvlivejp.shirosso.shiro.authc.ShiroSsoToken;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

@Setter
@Getter
public class CustomerFormAuthenticationFilter extends FormAuthenticationFilter {

    private String loginHtmlUrl;

    /**
     * 1.请求中含有token，代表是从sso server回调，需要再次访问sso server，确认该token是否有效
     * 2.请求中不含有token，则直接重定向到sso server的登陆页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        /**
         * 请求中含有token，代表是从sso server回调，需要再次访问sso server，确认该token是否有效
         * 通过executeLogin的方法，调用ShiroSsoCredentialsMatcher中的doCredentialsMatch，去访问sso server。
         */
        if(StringUtils.hasText(request.getParameter("token"))){
            return this.executeLogin(request, response);
        }

        /**
         * 请求中不含有token，则直接重定向到sso server的登陆页面
         * 设置登录页为sso server的登陆页面，并且拼上回调地址
         */
        setLoginUrl(loginHtmlUrl+"?"+ ShiroSsoConstant.SHIRO_REDIRECT_URL+"="+ URLEncoder.encode(httpRequest.getRequestURL().toString()));
        return super.onAccessDenied(request, response);
    }

    /**
     * 创建sso client的token
     * @param request
     * @param response
     * @return
     */
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        return new ShiroSsoToken(request.getParameter("token"));
    }

    /**
     * 重写原生的executeLogin，因为在原生的方法内部，如果登录失败，不抛出异常，直接返回true
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        Subject subject = getSubject(request, response);
        subject.login(token);
        return onLoginSuccess(token, subject, request, response);
    }

    /**
     * 通过shiro内部鉴权后，获取sso server颁发的token，再去sso server认证
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean accessAllowed = super.isAccessAllowed(request, response, mappedValue);
        if(!accessAllowed){
            return false;
        }

        Subject subject = SecurityUtils.getSubject();
        RestTemplate restT = new RestTemplate();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("token", subject.getPrincipal());

        ResponseEntity<String> stringResponseEntity = null;
        try {
            stringResponseEntity = restT.postForEntity("http://127.0.0.1:9090/auth/checkToken", paramMap, String.class);
            return stringResponseEntity.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            e.printStackTrace();
            return false;
        }
    }
}
