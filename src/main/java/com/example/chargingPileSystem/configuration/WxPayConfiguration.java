package com.example.chargingPileSystem.configuration;

import com.example.chargingPileSystem.util.IOUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.io.InputStream;


@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfiguration {
    private  final WxPayProperties wxPayProperties;

    public WxPayConfiguration(WxPayProperties wxPayProperties) {
        this.wxPayProperties = wxPayProperties;
    }

    @Bean
    public WxPayConfig wxPayConfig(){
        byte[] inApiClientCertBytes = null;
        byte[] inApiClientKeyBytes = null;

        InputStream inApiClientCert = this.getClass().getResourceAsStream("/" + wxPayProperties.getCertPath());
        InputStream inApiClientKey = this.getClass().getResourceAsStream("/" + wxPayProperties.getKeyPath());
        // 读取证书
        if (inApiClientCert != null) {
            IOUtil converterCert = new IOUtil();
            try {
                inApiClientCertBytes = converterCert.convertToByteArray(inApiClientCert);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 读取密钥
        if (inApiClientKey != null) {
            IOUtil converterKey = new IOUtil();
            try {
                inApiClientKeyBytes = converterKey.convertToByteArray(inApiClientKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("File not found: " + "apiclient_key.pem");
        }
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(wxPayProperties.getAppId());
        payConfig.setMchId(wxPayProperties.getMchId());
        payConfig.setApiV3Key(wxPayProperties.getApiV3Key());
        payConfig.setKeyPath(wxPayProperties.getP12Path());//p12
        payConfig.setUseSandboxEnv(wxPayProperties.isUseSandboxEnv());
        payConfig.setPrivateCertContent(inApiClientCertBytes);//私钥
        payConfig.setPrivateKeyContent(inApiClientKeyBytes);//证书
        return  payConfig;
    }

    @Bean
    @ConditionalOnBean(WxPayConfig.class)
    @Scope("prototype")
    public WxPayService wxPayService() {
        WxPayService payService = new WxPayServiceImpl();
        payService.setConfig(wxPayConfig());
        return payService;
    }
}