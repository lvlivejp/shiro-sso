package com.lvlivejp.shirosso.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@Controller

public class ViewController {

    @GetMapping("/loginview")
    public String loginview(HttpServletRequest servletRequest, Model model){

        String redirectUrl = servletRequest.getParameter("redirectUrl");
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            if(StringUtils.hasText(redirectUrl)){
                return "redirect:/"+redirectUrl;
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
