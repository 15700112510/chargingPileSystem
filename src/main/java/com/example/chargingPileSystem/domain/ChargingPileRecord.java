package com.example.chargingPileSystem.domain;

import com.example.chargingPileSystem.commen.PropertyIgnore;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class ChargingPileRecord {
    @PropertyIgnore
    private int id;
    @PropertyIgnore
    private String chargingPileId;
    private Timestamp upTime;
    private Timestamp downTime;
    private String userOpenId;
    private String stage;
    private int chargingTime;
    private String singleEnergy;
    private int chargingForm;
    private int gateStatus;
}
