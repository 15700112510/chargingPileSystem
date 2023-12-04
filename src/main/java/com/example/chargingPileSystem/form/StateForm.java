package com.example.chargingPileSystem.form;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StateForm {
    //冲充桩名称
    private String bleName;
    //充电开始时间
    private String chargingTime;

    //充电电压；
    private String voltage;

    //充电电流
    private String current;

    //充电功率；
    private String power;





}
