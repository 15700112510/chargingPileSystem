package com.example.chargingPileSystem.Service.impl;

import com.example.chargingPileSystem.Service.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import com.example.chargingPileSystem.mapper.PileRecordMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.Normalizer;

@Service
public class PileRecordServiceImpl implements PileRecordService {
    @Resource
    private ChargingPileInfoMapper chargingPileInfoMapper;
    @Resource
    private MqttClient mqttClient;

    @Override
    public R<?> openPile(String chargingPileId) throws MqttException {
        String content  = "发送消息";
        String topic = "CDZ"+chargingPileId+"/Config";
        MqttMessage msg= new MqttMessage(content.getBytes());
        if (chargingPileInfoMapper.queryStage(chargingPileId) == "6"){
            mqttClient.publish(topic, msg);
            return R.ok("充电桩开启成功");
        }

        return null;
    }
}
