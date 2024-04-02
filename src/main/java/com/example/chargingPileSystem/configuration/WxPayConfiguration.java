package com.example.chargingPileSystem.configuration;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(WxPayService.class)
@AllArgsConstructor
public class WxPayConfiguration {
    @Bean
    public WxPayService wxService() {
        WxPayService payService = new WxPayServiceImpl();
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId("wx04a5a6484e9716c2 ");
        payConfig.setMchId("1672263753");
        payConfig.setApiV3Key("");
        payConfig.setKeyPath("apiclient_cert.p12");//p12
        payConfig.setPrivateKeyPath("apiclient_key.pem");//私钥
        payConfig.setPrivateCertPath("apiclient_cert.pem");//证书
        payConfig.setNotifyUrl("");
        payConfig.setUseSandboxEnv(false);
        payService.setConfig(payConfig);
        return payService;
    }
}