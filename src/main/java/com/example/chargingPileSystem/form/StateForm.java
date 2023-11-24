package com.example.chargingPileSystem.form;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StateForm {

    //充电开始时间
    private Timestamp upTime;

    private int stage;
    //充电电压；
    private String voltage;

    //充电电流
    private String current;

    //充电功率；
    private String power;



    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}
