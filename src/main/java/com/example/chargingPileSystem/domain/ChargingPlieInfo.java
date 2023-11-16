package com.example.chargingPileSystem.domain;

import lombok.Data;

@Data
public class ChargingPlieInfo  {
    private int id;
    private String chargingPileId;
    private String voltage;
    private String current;
    private String power;
    private String accumulatedElectricEnergy;
    private int error;
    private String appointmentTime;
    private int equipmentTemperature;
    private int status;
    private String bleName;

}
