package com.lvlivejp.shirosso.shiro.filter;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Setter
@Getter
public class CustomerFormAuthenticationFilter extends FormAuthenticationFilter {

    private String unLoginHtml;
    private String unLoginJson;


//    @Override
//    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        String requestType = httpRequest.getHeader("X-Requested-With");
//        if("XMLHttpRequest".equals(requestType)){
//            //Ajax请求
//            WebUtils.issueRedirect(request, response,unLoginJson);
//        }else{
//            if(httpRequest.getRequestURI().indexOf("/rest/")>-1){
//                WebUtils.issueRedirect(request, response, unLoginJson);
//            }else{
//                WebUtils.issueRedirect(request, response, unLoginHtml);
//            }
//        }
//    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestType = httpRequest.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestType)){
            //Ajax请求
            setLoginUrl(unLoginJson);
        }else{
            if(httpRequest.getRequestURI().indexOf("/rest/")>-1){
                setLoginUrl(unLoginJson);
            }else{
                setLoginUrl(unLoginHtml);
            }
        }
        return super.onAccessDenied(request, response);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        return super.executeLogin(request, response);
    }
}
