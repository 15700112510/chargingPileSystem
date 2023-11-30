package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.UserService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.UserInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public R<?> login(@RequestBody UserInfo user){
        return userService.login(user.getUserName());
    }

    @PostMapping("/register")
    public R<?> register(@RequestParam String userName, @RequestParam String chargingPileId) {
        return userService.register(userName, chargingPileId);
    }
}
