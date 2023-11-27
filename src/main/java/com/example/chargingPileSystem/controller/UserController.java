package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.UserService;
import com.example.chargingPileSystem.commen.R;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private MqttClient mqttClient;

    @RequestMapping("/login")
    public R<?> login(@RequestParam String userOpenId) {
        return userService.login(userOpenId);
    }

    @RequestMapping("/register")
    public R<?> register(@RequestParam String userOpenId, @RequestParam String chargingPileId) {
        return userService.register(userOpenId, chargingPileId);
    }
}
