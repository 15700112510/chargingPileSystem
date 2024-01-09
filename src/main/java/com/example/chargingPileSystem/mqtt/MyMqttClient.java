package com.example.chargingPileSystem.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class MyMqttClient {
    private final MqttProperties mqttProperties;
    private final DefaultListableBeanFactory beanFactoryWrapper;

    private final MsgProcessor msgProcessor;

    private MqttClient mqttClient;

    public MyMqttClient(MqttProperties mqttProperties, DefaultListableBeanFactory beanFactoryWrapper,MsgProcessor msgProcessor,MqttClient mqttClient) {
        this.mqttProperties = mqttProperties;
        this.beanFactoryWrapper = beanFactoryWrapper;
        this.msgProcessor =  msgProcessor;
        this.mqttClient = mqttClient;
    }


    @PostConstruct
    public void init() throws MqttException {
        // 连接服务器
        connect(mqttClient, mqttProperties);
        mqttClient.subscribe(mqttProperties.getTopic());
        mqttClient.setCallback(new mqttCallBack(mqttProperties, mqttClient, msgProcessor));
    }

    /**
     * 连接broker
     */

    public static void connect(MqttClient mqttClient, MqttProperties mqttProperties) throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        options.setConnectionTimeout(mqttProperties.getTimeout());
        options.setKeepAliveInterval(mqttProperties.getKeepAlive());
        mqttClient.connect(options);
    }

//    /**
//     * 断开连接
//     */
//    public void disConnect(){
//        try {
//            mqttClient.disconnect();
//        } catch (MqttException e) {
//        }
//    }
//
//    /**
//     * 重连
//     */
//    public void reConnect(){
//        try {
//           mqttClient.reconnect();
//            MqttConnectOptions options = new MqttConnectOptions();
//            options.setUserName(mqttProperties.getUsername());
//            options.setPassword(mqttProperties.getPassword().toCharArray());
//            options.setConnectionTimeout(mqttProperties.getTimeout());
//            options.setKeepAliveInterval(mqttProperties.getKeepAlive());
//            mqttClient.connect(options);
//            mqttClient.subscribe(mqttProperties.getTopic());
//        } catch (MqttException e) {
//        }
//    }
//
//    /**
//     * 发布消息
//     * @param topic
//     * @param msg
//     * @param retain
//     */
//    public void publish(String topic, String msg,  boolean retain){
//        MqttMessage mqttMessage = new MqttMessage();
//        mqttMessage.setPayload(msg.getBytes());
//        mqttMessage.setRetained(retain);
//        try {
//            mqttClient.publish(topic,mqttMessage);
//        } catch (MqttException e) {
//        }
//    }



}
