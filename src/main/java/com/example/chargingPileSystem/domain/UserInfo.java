package com.example.chargingPileSystem.domain;

import lombok.Data;

@Data
public class UserInfo {
    private String userOpenId;
    private String userName;
    private String updateTime;
    private String userPhone;
    private String chargingPileId;
}
