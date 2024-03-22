package com.example.chargingPileSystem.Service;

import com.example.chargingPileSystem.commen.R;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface PileRecordService {
    //开始充电
    public R<?> openPile(String chargingPileId) throws MqttException;

    //关闭充电
    public R<?> closePile(String chargingPileId) throws MqttException;

    //预约充电
    public R<?> appointmentTime(String chargingPileId,String appointmentTime) throws MqttException;
}
