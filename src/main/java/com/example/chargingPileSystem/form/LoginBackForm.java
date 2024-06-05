package com.example.chargingPileSystem.form;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginBackForm {
    private String token;
    private String userPhone;
    private String userOpenid;
    private String userName;
    private String chargingPileId;
    private Integer role;
}
