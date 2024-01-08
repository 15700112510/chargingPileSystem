package com.example.chargingPileSystem;

import com.alibaba.fastjson2.JSON;
import com.example.chargingPileSystem.redis.RedisService;
import com.example.chargingPileSystem.util.RedisUtil;
import com.example.chargingPileSystem.util.RSAUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@SpringBootTest
class ChargingPileSystemApplicationTests {
    @Resource
    RedisUtil redisUtil;

    @Resource
    RedisService redisService;

    public void getObjectProperty(Object obj){
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);

            System.out.println(field.getName());
        }
    }

    @Test
    void test() throws Exception {
        KeyPair keyPair = RSAUtils.getKeyPair();
        String str = JSON.toJSONString(keyPair);
        redisService.setCacheObject("test", str);

        String obj = (String) redisService.getCacheObject("test");
        System.out.println(obj);
    }

    @Test
    void contextLoads() throws Exception {
        String  str = "这是需要加密的内容";
        System.out.println(str);


        //生成密钥对
        KeyPair keyPair = RSAUtils.getKeyPair();
        PrivateKey privateKey =keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        //将私钥和公钥转换为字符串
        String privateKeyStr = RSAUtils.getPrivateKeyStr(privateKey);
        String publicKeyStr = RSAUtils.getPublicKeyStr(publicKey);
        System.out.println("这是私钥"+privateKeyStr);
        System.out.println("这是公钥"+publicKeyStr);

        PrivateKey privateKey1 = RSAUtils.getPrivateKey(privateKeyStr);
        PublicKey publicKey1 = RSAUtils.getPublicKey(publicKeyStr);
        System.out.println("是否为相同密钥========="+ privateKey1.equals(privateKey));
        //注：privateKey1和privateKey一样


        //使用公钥加密
        String encryptStr = RSAUtils.encrypt(str,publicKey1);
        System.out.println("这是加密后的内容"+encryptStr);

        //使用私钥解密
        String decryptStr = RSAUtils.decrypt(encryptStr,privateKey1);
        System.out.println("这是解密后的内容"+decryptStr);


        String sign = RSAUtils.sign(str, privateKey1);
        System.out.println("这是签名"+sign);

        boolean result = RSAUtils.verify(str, publicKey1, sign);
        System.out.println("验签结果"+result);

        redisUtil.saveRSAKeyPair();
        System.out.println("===========");

        if (redisService.hasKey("privateKey") && redisService.hasKey("publicKey")){
            System.out.println("redis中存在密钥对");
            System.out.println("privateKey:"+redisUtil.getPrivateKey());
            System.out.println("publicKey:"+redisUtil.getPublicKey());
            }else {
            System.out.println("redis中不存在密钥对");
            redisUtil.saveRSAKeyPair();
        }
//        KeyPair keyPair2 =  redisUtil.getRSAKeyPair();
//        PrivateKey privateKey2 =keyPair2.getPrivate();
//        PublicKey publicKey2 = keyPair2.getPublic();
//        System.out.println("这是私钥"+privateKey2);
//        System.out.println("这是公钥"+publicKey2);
//
//        PrivateKey privateKeyStr = RSAUtils.getPrivateKey(privateKey.getEncoded());
//        PublicKey publicKeyStr = RSAUtils.getPublicKey(publicKey);
    }

}
