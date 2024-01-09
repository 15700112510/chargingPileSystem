package com.example.chargingPileSystem.Service.impl;

import com.example.chargingPileSystem.Service.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import com.example.chargingPileSystem.mapper.ChargingPileRecordMapper;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
public class PileRecordServiceImpl implements PileRecordService {
    @Resource
    private ChargingPileInfoMapper chargingPileInfoMapper;
    @Resource
    private ChargingPileRecordMapper chargingPileRecordMapper;
    @Resource
    private MqttClient mqttClient;

    @Override
    public R<?> openPile(String chargingPileId) throws MqttException {
        String content = "POWER_ENABLE";
        String topic = "CDZ/" + chargingPileId + "/Config";
        MqttMessage msg = new MqttMessage(content.getBytes());
        if (chargingPileInfoMapper.queryStage(chargingPileId).equals("6")) {
            mqttClient.publish(topic, msg);
            return R.ok("充电桩开启成功");
        }
        return R.fail(ErrorEnum.CHARGING_PILE_OPENING_ERROR);
    }

    @Override
    public R<?> closePile(String chargingPileId) throws MqttException {
        String content = "POWER_DISABLE";
        String topic = "CDZ/" + chargingPileId + "/Config";
        MqttMessage msg = new MqttMessage(content.getBytes());
        if (chargingPileRecordMapper.queryLastRecord(chargingPileId).getDownTime() == null) {
            mqttClient.publish(topic, msg);
            return R.ok("充电桩关闭成功");
        }
        return R.fail(ErrorEnum.CHARGING_PILE_OPENING_ERROR);
    }

    @Override
    public R<?> appointmentTime(String chargingPileId, String appointmentTime) throws MqttException {
        String topic = "CDZ/" + chargingPileId + "/Config";
        MqttMessage msg = new MqttMessage(appointmentTime.getBytes());
        if (chargingPileRecordMapper.queryLastRecord(chargingPileId).getDownTime() == null) {
            mqttClient.publish(topic, msg);
            return R.ok("充电桩预约成功");
        }
        return R.fail(ErrorEnum.CHARGING_PLIE_APPOINTMENT_ERROR);
    }
}
