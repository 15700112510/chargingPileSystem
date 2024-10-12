package com.example.chargingPileSystem.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("wepay")
public class WxPayProperties {
    private String appId;
    private String mchId;
    private String apiV3Key;
    private String certPath;
    private String keyPath;
    private String p12Path;
    private String notifyUrl;
    private boolean useSandboxEnv;
}
