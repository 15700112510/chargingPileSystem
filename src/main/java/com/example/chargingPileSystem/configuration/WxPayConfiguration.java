package com.example.chargingPileSystem.configuration;

import com.example.chargingPileSystem.util.IOUtil;
import com.github.binarywang.wxpay.config.WxPayConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;


@Configuration
@AllArgsConstructor
public class WxPayConfiguration {
    @Bean
    public WxPayConfig wxPayConfig(){
        byte[] inApiClientCertBytes = null;
        byte[] inApiClientKeyBytes = null;

        InputStream inApiClientCert = this.getClass().getResourceAsStream("/" + "apiclient_cert.pem");
        InputStream inApiClientKey = this.getClass().getResourceAsStream("/" + "apiclient_key.pem");
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
        String strCert = new String(inApiClientCertBytes);
        String strKey = new String(inApiClientKeyBytes);
        System.out.println("inApiClientCertBytes:" + strCert);
        System.out.println("inApiClientKeyBytes:" + strKey);
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId("wx04a5a6484e9716c2");
        payConfig.setMchId("1672263753");
        payConfig.setApiV3Key("Zjr12345678912345678912345678900");
        payConfig.setKeyPath("apiclient_cert.p12");//p12
        payConfig.setPrivateCertContent(inApiClientCertBytes);//私钥
        payConfig.setPrivateKeyContent(inApiClientKeyBytes);//证书
        payConfig.setNotifyUrl("https://zeddy.online/charging/app/WxPay/wxBack");
        payConfig.setUseSandboxEnv(false);
        return  payConfig;
    }
}