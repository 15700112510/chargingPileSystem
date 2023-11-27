package com.example.chargingPileSystem.mqtt;
import com.example.chargingPileSystem.commen.BeanFactoryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Arrays;

@Slf4j
public class mqttCallBack implements MqttCallback {
    private MqttClient mqttClient;
    private final MqttProperties mqttProperties;
    private final MsgProcessor msgProcessor;
    private final BeanFactoryWrapper beanFactoryWrapper;

    public mqttCallBack(MqttProperties mqttProperties, MqttClient mqttClient, BeanFactoryWrapper beanFactoryWrapper) {
        this.mqttClient = mqttClient;
        this.mqttProperties = mqttProperties;
        this.beanFactoryWrapper = beanFactoryWrapper;
        this.msgProcessor = this.beanFactoryWrapper.getBean(MsgProcessor.class);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.warn("Connection of mqtt client [{}] to broker [{}] has lost",
                mqttProperties.getClientId(), mqttProperties.getHost(), throwable);
        log.debug("Ready to reconnect to broker [{}]", mqttProperties.getHost());

        beanFactoryWrapper.removeBeanDefinition("mqttClient");
        beanFactoryWrapper.registerBeanDefinition(
                "mqttClient", MqttClient.class, mqttProperties.getHost(), mqttProperties.getClientId(), null);
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
            if (iMqttDeliveryToken.isComplete()) {
                try {
                    log.debug("Message [{}] delivery completed", new String(iMqttDeliveryToken.getMessage().getPayload()));
                } catch (MqttException e) {

                }
            }
    }
}
