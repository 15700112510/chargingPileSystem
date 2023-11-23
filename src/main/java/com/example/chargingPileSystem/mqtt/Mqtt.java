package com.example.chargingPileSystem.mqtt;

import com.likain.core.AbstractMqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;


//@Component
public class Mqtt extends AbstractMqttCallbackExtended {
    private MsgProcessor msgProcessor;

    @Override
    public String clientId() {
        return "client-szd";
    }

    @Override
    public String serverUrl() {
        return "tcp://192.168.50.128:1883";
    }

    @Override
    public String username() {
        return "admin";
    }

    @Override
    public String password() {
        return "admin";
    }

    @Override
    public String[] topics() {
        return new String[]{"test/#"};
    }

    @Override
    public int timeout() {
        return 30;
    }

    @Override
    public int keepAlive() {
        return 60;
    }

    @Override
    public void connectComplete(boolean b, String s) {

    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        if (msgProcessor.getClass().getPackage().toString()
                .equals("package com.workshop.system.mqtt.v1")) {
            MsgProcessorImpl processor = (MsgProcessorImpl) msgProcessor;
//            processor.setTopic(topic).setMqttMessage(mqttMessage).process();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
