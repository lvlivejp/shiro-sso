package com.lvlivejp.shirosso.controller;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@Controller

public class ViewController {

    @GetMapping("/loginview")
    public String loginview(HttpServletRequest servletRequest, Model model){
        String redirectUrl = servletRequest.getParameter("redirectUrl");
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
