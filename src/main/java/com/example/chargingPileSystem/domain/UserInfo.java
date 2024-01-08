package com.example.chargingPileSystem.domain;

import lombok.Data;

@Data
public class UserInfo {
    private String userOpenId;
    private String userName;
    private String updateTime;
    private Long userPhone;
    private String password;
    private String chargingPileId;
    private Integer role;
    private String slat;
}
