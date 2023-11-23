package com.example.chargingPileSystem.domain;

import lombok.Data;

import java.sql.Timestamp;
@Data
public class ChargingPlieRecord {
    private int id;
    private String chargingPileId;
    private Timestamp upTime;
    private Timestamp downTime;
    private String userOpenId;
    private String stage;
    private int chargingTime;
    private String singleEnergy;
    private int chargingForm;
    private int gateStatus;
    private String CP;
}
