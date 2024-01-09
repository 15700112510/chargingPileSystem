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
    //上机时间
    @PropertyIgnore
    private Timestamp upTime;
    //下机时间
    @PropertyIgnore
    private Timestamp downTime;
    //用户ID
    private String userOpenId;
    //状态
    private String stage;
    //充电时长
    private int chargingTime;
    //单次充电电能
    private String singleEnergy;
    //充电类型
    private int chargingForm;
    //闸门状态
    private int gateStatus;
}
