package com.example.chargingPileSystem.form;

import lombok.Data;

@Data
public class LoginForm{
    //冲充桩名称
    private String code;
    private String bleName;
    private String userName;
    private String userPhone;
    private String password;
    private String chargingPileId;
}
