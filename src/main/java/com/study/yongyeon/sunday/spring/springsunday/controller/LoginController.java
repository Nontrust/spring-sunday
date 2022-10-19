package com.study.yongyeon.sunday.spring.springsunday.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/permit")
public class LoginController {
    @GetMapping("/all")
    public String exAll(){
        log.info("exAll......");
        return "login/all";
    }

    @GetMapping("/member")
    public String exMember(){
        log.info("exMember......");
        return "login/member";
    }

    @GetMapping("/admin")
    public String exAdmin(){
        log.info("exAdmin......");
        return "login/admin";
    }
}
