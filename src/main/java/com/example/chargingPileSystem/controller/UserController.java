package com.example.chargingPileSystem.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.chargingPileSystem.Service.jsapi.UserService;
import com.example.chargingPileSystem.util.RSAUtils;
import com.example.chargingPileSystem.util.RedisUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;


//    @PostMapping("/login")
//    public R<?> login(@RequestBody LoginForm loginForm) throws Exception {
//        return userService.login(loginForm);
//    }

    @GetMapping("/publicKey")
    public String getPublicKey() throws Exception {
        return RSAUtils.getPublicKeyStr(redisUtil.getPublicKey());
    }

    @GetMapping("/getPhoneNumber")
    public JSONObject getPhoneNumber(String code) throws Exception {
        return userService.getPhoneNumber(code);
    }

//
//    @PostMapping("/register")
//    public R<?> register(@RequestBody UserInfo userInfo) throws Exception {
//        return userService.register(userInfo);
//    }

    @GetMapping("/test")
    //@AllowedRole(role = 0 )
    public String test(HttpServletRequest request) {
        System.out.println("=========="+request.getParameter("name"));
        return "Hello, World!";
    }
}
