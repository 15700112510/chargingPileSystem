package com.example.chargingPileSystem.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

@Data
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    private boolean enable;
    private String host;
    private String username;
    private String password;
    private Integer timeout;
    private Integer keepAlive;
    private String topic;
    private final String clientId = ("SYS-" + UUID.randomUUID().toString().substring(0, 4)).toUpperCase();
}
