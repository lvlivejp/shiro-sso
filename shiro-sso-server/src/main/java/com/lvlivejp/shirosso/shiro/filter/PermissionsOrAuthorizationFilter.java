package com.lvlivejp.shirosso.shiro.filter;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Setter
@Getter
public class PermissionsOrAuthorizationFilter extends AuthorizationFilter {

    private String unLoginHtml;
    private String unLoginJson;
    private String unAuthorizedHtml;
    private String unAuthorizedJson;
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;

        boolean isPermitted = false;
        for (String perm : perms) {
            if (subject.isPermitted(perm)) {
                isPermitted = true;
                break;
            }
        }
        return isPermitted;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestType = httpRequest.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestType)){
            //Ajax请求
            setLoginUrl(unLoginJson);
            setUnauthorizedUrl(unAuthorizedJson);
        }else{
            if(httpRequest.getRequestURI().indexOf("/rest/")>-1){
                setLoginUrl(unLoginJson);
                setUnauthorizedUrl(unAuthorizedJson);
            }else{
                setLoginUrl(unLoginHtml);
                setUnauthorizedUrl(unAuthorizedHtml);
            }
        }
        return super.onAccessDenied(request, response);
    }
}
