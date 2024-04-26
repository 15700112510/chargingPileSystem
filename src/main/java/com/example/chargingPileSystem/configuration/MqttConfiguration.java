package com.example.chargingPileSystem.configuration;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false) //Spring不要为该类创建代理实例，而是直接使用原始实例
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfiguration implements InitializingBean {
    private  final MqttProperties mqttProperties;
    private MqttClient mqttClient;

    public MqttConfiguration(MqttProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    @Bean
    //它检查配置属性mqtt.enable的值是否为true，如果是，则启用该功能；否则，禁用该功能
    @ConditionalOnProperty(value = "mqtt.enable", havingValue = "true")
    public MqttClient mqttClient() throws SystemException {
        try {
            mqttClient = new MqttClient(mqttProperties.getHost(), mqttProperties.getClientId(), null);
        } catch (MqttException e) {
            log.error("MqttClient初始化失败", e);
        }
        return mqttClient;
    }

//    @Bean
//    //当Spring创建当前Bean时，它会检查名为mqttClient的Bean是否已经创建。如果已经创建，则将mqttClient注入到当前Bean中；否则，当前Bean的创建将会被延迟到mqttClient创建之后。
////    @DependsOn("mqttClient")
//    public MsgProcessor msgProcessor() {
//        return new MsgProcessorImpl();
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("Mqtt initialization task executing");
    }
}
