package com.jql.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class TestController {
    @RequestMapping("/index")
    public String index(Map<String,String>map){

        return "index";
    }
    @RequestMapping("/logout")
    public String logout(Map<String,String>map){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }@RequestMapping("/login")
    public String login(Map<String,String>map){

        return "login";
    }
    @RequestMapping("/userlogin")
    public String userlogin(String username,String password,Map<String,String> map){
        Subject subject = SecurityUtils.getSubject();
        String encodePwd = new Md5Hash(username,password).toString();
        UsernamePasswordToken token = new UsernamePasswordToken(username,encodePwd);
        try{
            subject.login(token);
            return "redirect:/index";
        }catch (Exception e){
            e.printStackTrace();
            map.put("msg","账号密码不匹配");
            return "login";
        }
    }

}
