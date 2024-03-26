package com.example.chargingPileSystem.Service.manage;

import com.alibaba.fastjson2.JSONObject;

public interface UserService {
   // public R<?> login(LoginForm loginForm) throws Exception;
    //public R<?> register(UserInfo userInfo) throws Exception;
    public JSONObject getPhoneNumber(String code) throws Exception;
}
