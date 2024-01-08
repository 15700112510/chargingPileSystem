package com.example.chargingPileSystem.mqtt;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Arrays;

@Slf4j
public class mqttCallBack implements MqttCallback {
    private MqttClient mqttClient;
    private final MqttProperties mqttProperties;
    private final MsgProcessor msgProcessor;
    @Autowired
    private DefaultListableBeanFactory beanFactoryWrapper;

    public mqttCallBack(MqttProperties mqttProperties, MqttClient mqttClient, MsgProcessor msgProcessor) {
        this.mqttClient = mqttClient;
        this.mqttProperties = mqttProperties;
        this.msgProcessor = msgProcessor;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.warn("Connection of mqtt client [{}] to broker [{}] has lost",
                mqttProperties.getClientId(), mqttProperties.getHost(), throwable);
        log.debug("Ready to reconnect to broker [{}]", mqttProperties.getHost());

        beanFactoryWrapper.removeBeanDefinition("mqttClient");
        beanFactoryWrapper.registerSingleton("mqttClient", mqttClient);
        this.mqttClient = beanFactoryWrapper.getBean(MqttClient.class);

        try {
            MyMqttClient.connect(mqttClient, mqttProperties);
            log.debug("Mqtt client [{}] reconnected to broker [{}]", mqttProperties.getClientId(), mqttProperties.getHost());
        } catch (MqttException e) {
            //throw new SystemException("Mqtt failure occurred when attempted to reconnect to broker", e);
        }

        Arrays.stream(new String[]{mqttProperties.getTopic()}).forEach(s -> {
            try {
                mqttClient.subscribe(s);
                log.debug("Mqtt topic [{}] resubscribed");
            } catch (MqttException e) {
                //throw new SystemException("Mqtt failure occurred when attempted to resubscribe topics", e);
            }
        });

        mqttClient.setCallback(this);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        MsgProcessorImpl processor = (MsgProcessorImpl) msgProcessor;
        processor.setTopic(topic);
        processor.setMqttMessage(mqttMessage);
        processor.process();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
