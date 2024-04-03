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

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(WxPayService.class)
@AllArgsConstructor
public class WxPayConfiguration {
    @Bean
    public WxPayConfig wxPayConfig(){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId("wx04a5a6484e9716c2 ");
        payConfig.setMchId("1672263753");
        payConfig.setApiV3Key("Zjr12345678912345678912345678900");
        payConfig.setKeyPath("apiclient_cert.p12");//p12
        payConfig.setPrivateKeyPath("certificate/apiclient_key.pem");//私钥
        payConfig.setPrivateCertPath("certificate/apiclient_cert.pem");//证书
        payConfig.setNotifyUrl("https://zeddy.online/charging/app/WxPay/wxBack");
        payConfig.setUseSandboxEnv(false);
        return  payConfig;
    }
    @Bean
    public WxPayService wxService() {
        WxPayService payService = new WxPayServiceImpl();
        payService.setConfig(wxPayConfig());
        return payService;
    }
}