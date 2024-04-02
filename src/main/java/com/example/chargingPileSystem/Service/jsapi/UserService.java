package com.example.chargingPileSystem.Service.jsapi;

import com.alibaba.fastjson2.JSONObject;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.form.LoginForm;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    public R<?> login(LoginForm loginForm) throws Exception;
    public R<?> register(UserInfo userInfo) throws Exception;
    public JSONObject getPhoneNumber(String code) throws Exception;
}
