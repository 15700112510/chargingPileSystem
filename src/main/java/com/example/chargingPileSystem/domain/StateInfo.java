package com.example.chargingPileSystem.domain;

import lombok.Data;

@Data
public class StateInfo {
    private String voltage;
    private String current;
    private String power;
}
