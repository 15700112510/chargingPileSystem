package com.example.chargingPileSystem.mqtt;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import javax.annotation.Resource;

@Slf4j
public class mqttCallBack implements MqttCallback {
    private MsgProcessor processor;

    public mqttCallBack(MsgProcessor processor) {
        this.processor = processor;
    }


    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        processor.setTopic(topic);
        processor.setMqttMessage(mqttMessage);
        processor.process();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
