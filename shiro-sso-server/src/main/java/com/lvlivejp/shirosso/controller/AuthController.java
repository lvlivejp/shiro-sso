package com.lvlivejp.shirosso.controller;

import com.lvlivejp.shirosso.core.base.BaseResult;
import com.lvlivejp.shirosso.core.base.ShiroSsoConstant;
import com.lvlivejp.shirosso.core.enums.ResultEnum;
import com.lvlivejp.shirosso.core.utils.BaseResultUtils;
import com.lvlivejp.shirosso.core.utils.ShiroThreadLocal;
import com.lvlivejp.shirosso.service.UserService;
import com.lvlivejp.shirosso.service.WebTokenService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private WebTokenService webTokenService;

    @PostMapping("/login")
    public String login(String username,
                        String password,
                        boolean rememberMe,
                        String redirectUrl,
                        RedirectAttributes redirectAttributes) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(rememberMe);
        try {
            ShiroThreadLocal.setUserName(username);
            subject.login(token);
        } catch (ShiroException e) {
            if (e instanceof IncorrectCredentialsException) {
                redirectAttributes.addAttribute("errorMsg",URLEncoder.encode(ResultEnum.INCORRECT_CREDENTIALS.getName()));
            } else if (e instanceof UnknownAccountException) {
                redirectAttributes.addAttribute("errorMsg",URLEncoder.encode(ResultEnum.UNKNOWN_ACCOUNT.getName()));
            }
            if(StringUtils.hasText(redirectUrl)){
                redirectAttributes.addAttribute("redirectUrl",redirectUrl);
            }
            return "redirect:/loginview";
        } finally {
            ShiroThreadLocal.removeUserName();
        }

        if(StringUtils.hasText(redirectUrl)){
            String webToken = webTokenService.generateToken(subject.getSession().getId().toString());
            return "redirect:"+redirectUrl+"?"+ ShiroSsoConstant.SHIRO_SSO_CLIENT_TOKEN+ "="+webToken;
        }
        return "redirect:/indexview";
    }

    @PostMapping("/logout")
    public BaseResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return BaseResultUtils.success();
    }

    @PostMapping("/checkToken")
    public void checkToken(HttpServletResponse response,String token) {
        if(!StringUtils.hasText(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        boolean checkToken = webTokenService.checkToken(token);
        if(!checkToken){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}