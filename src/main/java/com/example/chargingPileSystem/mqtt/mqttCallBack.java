package com.example.chargingPileSystem.mqtt;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import javax.annotation.Resource;

@Slf4j
public class mqttCallBack implements MqttCallback {


    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        MsgProcessor processor = new MsgProcessor();
        processor.setTopic(topic);
        processor.setMqttMessage(mqttMessage);
        processor.process();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
