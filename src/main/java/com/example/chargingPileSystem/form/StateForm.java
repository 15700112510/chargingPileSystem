package com.example.chargingPileSystem.form;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StateForm {

    //充电开始时间
    private Timestamp upTime;

    //充电电压；
    private String voltage;

    //充电电流
    private String current;

    //充电功率；
    private String power;

    


}
