package com.example.chargingPileSystem.util;

import com.example.chargingPileSystem.redis.RedisService;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class RedisUtil {
    @Resource
    RedisService redisService;

    // 保存RSA密钥对到Redis中
    public void saveRSAKeyPair() throws Exception {
        // 生成RSA密钥对
        KeyPair keyPair = RSAUtils.getKeyPair();
        if (!ObjectUtils.isEmpty(keyPair)) {
            String privateKey = RSAUtils.getPrivateKeyStr(keyPair.getPrivate());
            String publicKey = RSAUtils.getPublicKeyStr(keyPair.getPublic());
            redisService.setCacheObject("privateKey", privateKey);
            redisService.setCacheObject("publicKey", publicKey);
        }
    }

    // 获取公钥和私钥
    public PrivateKey getPrivateKey() throws Exception {
        if (!redisService.hasKey("privateKey") || !redisService.hasKey("publicKey")){
            this.saveRSAKeyPair();
        }
        String privateStr = (String) redisService.getCacheObject("privateKey");
        return RSAUtils.getPrivateKey(privateStr);
    }
    public PublicKey getPublicKey() throws Exception {
        if (!redisService.hasKey("privateKey") || !redisService.hasKey("publicKey")){
            this.saveRSAKeyPair();
        }
        String publicStr = (String) redisService.getCacheObject("publicKey");
        return RSAUtils.getPublicKey(publicStr);}
    }


