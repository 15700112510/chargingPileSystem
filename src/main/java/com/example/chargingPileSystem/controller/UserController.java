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

    @GetMapping("/login")
    public R<?> login(@RequestBody String userName) {
        return userService.login(userName);
    }

    @PostMapping("/register")
    public R<?> register(@RequestParam String userName, @RequestParam String chargingPileId) {
        return userService.register(userName, chargingPileId);
    }
}
