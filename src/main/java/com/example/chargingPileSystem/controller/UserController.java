package com.example.chargingPileSystem.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.chargingPileSystem.Service.jsapi.UserService;
import com.example.chargingPileSystem.annotation.AllowedRole;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.form.LoginForm;
import com.example.chargingPileSystem.util.RSAUtils;
import com.example.chargingPileSystem.util.RedisUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/charging/api/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginForm loginForm) throws Exception {
        return userService.login(loginForm);
    }


    @GetMapping("/publicKey")
    public String getPublicKey() throws Exception {
        return RSAUtils.getPublicKeyStr(redisUtil.getPublicKey());
    }

    @PostMapping("/getPhoneNumber")
    public R<?> getPhoneNumber(@RequestBody String code) throws Exception {
        return userService.getPhoneNumber(code);
    }


//
//    @PostMapping("/register")
//    public R<?> register(@RequestBody UserInfo userInfo) throws Exception {
//        return userService.register(userInfo);
//    }

    @GetMapping("/test")
    //@AllowedRole(role = 0 )
    public String getTest(@RequestParam String name) {
        System.out.println("=========="+name);
        return "getTest!";
    }

    @PostMapping ("/test")
    //@AllowedRole(role = 0 )
    public String postTest(@RequestParam String name) {
        System.out.println("=========="+name);
        return "postTest!";
    }
}
