package com.example.chargingPileSystem.form;

import com.example.chargingPileSystem.domain.UserInfo;
import lombok.Data;

@Data
public class LoginForm{

    //冲充桩名称
    private String bleName;
    private String userName;
    private String chargingPileId;
}
