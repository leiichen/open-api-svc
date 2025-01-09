package com.leichen.apiInterface.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/name")
public class UserController {

    @GetMapping("/")
    public String getName(String name) {
        return "你的名字是" + name;
    }
}
