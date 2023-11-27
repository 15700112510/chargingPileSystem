package com.example.chargingPileSystem.Service;

import com.example.chargingPileSystem.commen.R;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface PileRecordService {
    public R<?> openPile(String chargingPileId) throws MqttException;
    public R<?> closePile(String chargingPileId) throws MqttException;
    public R<?> appointmentTime(String chargingPileId,String appointmentTime) throws MqttException;
}
