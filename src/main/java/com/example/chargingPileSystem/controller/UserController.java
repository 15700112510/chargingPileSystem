package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.UserService;
import com.example.chargingPileSystem.commen.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/login")
    public R<?> login(@RequestParam String userOpenId) {
        return userService.login(userOpenId);
    }

    @PostMapping("/register")
    public R<?> register(@RequestParam String userName,@RequestParam String chargingPileId) {
        return userService.register(userName, chargingPileId);
    }
}
