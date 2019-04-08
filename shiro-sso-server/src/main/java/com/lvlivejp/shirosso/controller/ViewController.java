package com.lvlivejp.shirosso.controller;

import com.lvlivejp.shirosso.core.base.ShiroSsoConstant;
import com.lvlivejp.shirosso.service.WebTokenService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@Controller

public class ViewController {

    @Autowired
    private WebTokenService webTokenService;

    @GetMapping("/loginview")
    public String loginview(HttpServletRequest servletRequest, Model model){

        String redirectUrl = servletRequest.getParameter(ShiroSsoConstant.SHIRO_REDIRECT_URL);
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            if(StringUtils.hasText(redirectUrl)){
                String webToken = webTokenService.generateToken(subject.getSession().getId().toString());
                return "redirect:"+redirectUrl+"?"+ShiroSsoConstant.SHIRO_SSO_CLIENT_TOKEN+"="+webToken;
            }else{
                return "redirect:/indexview";
            }
        }
        model.addAttribute("redirectUrl",redirectUrl);
        if(StringUtils.hasText(servletRequest.getParameter("errorMsg"))){
            model.addAttribute("errorMsg", URLDecoder.decode(servletRequest.getParameter("errorMsg")));
        }
        return "login";
    }

    @GetMapping("/indexview")
    public String view(){
        return "index";
    }

}
