package com.example.chargingPileSystem;

import com.example.chargingPileSystem.domain.PaymentOrder;
import com.example.chargingPileSystem.mapper.PaymentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ChargingPileSystemApplicationTests {
    @Resource
    PaymentMapper  paymentMapper;
    @Test
    void contextLoads() {
//        int paymentOrder = paymentMapper.selectByUserOpenid("o9ji76wiK9Uy0HLaBZ7EGgmZGAm8","20240424193651");

        PaymentOrder paymentOrder = paymentMapper.selectByOrderNo("20240424193651");
        System.out.println(paymentOrder);
    }
    long timestamp = System.currentTimeMillis();
//    @Resource
//    RedisUtil redisUtil;
//
//    @Resource
//    RedisService redisService;
//
//    public void getObjectProperty(Object obj){
//        Class<?> clazz = obj.getClass();
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field: fields) {
//            field.setAccessible(true);
//
//            System.out.println(field.getName());
//        }
//    }
//
//    @Test
//    void test() throws Exception {
//        KeyPair keyPair = RSAUtils.getKeyPair();
//        String str = JSON.toJSONString(keyPair);
//        redisService.setCacheObject("test", str);
//
//        String obj = (String) redisService.getCacheObject("test");
//        System.out.println(obj);
//    }
//
//    @Test
//    void contextLoads() throws Exception {
//        Map<String, Object> claims = new HashMap<>();
//        // 3. 生成token
//        claims.put("userPhone", "userPhoneuserPhone");
//        claims.put("role", "rolerole");
//        String token = JwtUtil.creatToken(claims, JwtConstant.VALID_TOKEN_TTL);
//        Claims parseToken = JwtUtil.parseJwt(token);
//        System.out.println("pare========="+parseToken);
//        String userPhone = (String) parseToken.get("userPhone");
//        System.out.println(userPhone);
//
//        try{
//            Claims parseToken1 = JwtUtil.parseJwt("eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjozLCJ1c2VyUGhvbmUiOjE1NzAwMTEyNTEwLCJleHAiOjE3MDY2MDMxNzB9.GmE9rnUN8ocP2YnXdxzOpQa82F7365Bng6OOGEJ47oAXkxlXtRHA8jq_THGGMWgd6pojgAql-JVQyqEIwg4OUw");
////            Claims parseToken1 = JwtUtil.parseJwt(token);
//            System.out.println("pare========="+parseToken1);
//        }catch (ExpiredJwtException e){
//            System.out.println("token过期");
//        }catch (Exception e){
//            System.out.println("token无效"+ e.getMessage());
//        }
//        String  str = "这是需要加密的内容";
//        System.out.println(str);
//
//
//        //生成密钥对
//        KeyPair keyPair = RSAUtils.getKeyPair();
//        PrivateKey privateKey =keyPair.getPrivate();
//        PublicKey publicKey = keyPair.getPublic();
//
//        //将私钥和公钥转换为字符串
//        String privateKeyStr = RSAUtils.getPrivateKeyStr(privateKey);
//        String publicKeyStr = RSAUtils.getPublicKeyStr(publicKey);
//        System.out.println("这是私钥"+privateKeyStr);
//        System.out.println("这是公钥"+publicKeyStr);
//
//        PrivateKey privateKey1 = RSAUtils.getPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKIuRRbAL4VXHzQ/x8XTVT02Xkv5ltT3JH1P45/4ZLc4zEHidQIvtQZRJ6E/P45yBGolWZR+XtJ+H18pJ1GUzIiRr/poB4Q4M4nf76AyyKO0O+v7A6o98txBcjVA3KAMbuztWg81UYQ7aCyIPqeShijpVY9aF6D10uC7xrtrLNkJAgMBAAECgYBdF/g6lfjxLlBQGqheyu9r4rCAJfXJq0+7ysbbhaaycZz12LrXlXDw/lakPX0LFDGqiGQCVAO0CgeSBTp3ntVESBNejq+BAldksLQt7kVS6L++uYtKvhZzFvR+2vOahRT+oi1SEK3gL6wlWqxaADZgXVc1VItHzfyKT3hREfy6fQJBAOPHQg5ZjpAoAzMKQdNOC5HT3Sn7+NlxWpmGCap1W2M+JHtSONyRcEQx/FCetKgmKShhIKTNAC9pWg0R6K9P4YcCQQC2Rl4BfEHmyPjl2TefL60PO21PTgq04taUmxNUySfs21AvXlJA4OZXOp5G5br13AEB/mkyn4hUVGM1ISSD31TvAkEAxamcwScmeCLA5AH6rYwErj/UpSJsCQY9QQz4RlwhOjYrInlvrrjnW8ak1rFixKiY/tmKonjjuEEMTFtGORxw8QJASIsa8NZmWBaWUMwxwe40n0JUj+KMik9ZfY12lSD1/eOPNzr/24hmxEK1KZxW23DzCbsaXSZ3GLD5RvTo1n81CwJAUKRgflL5HYWtS/Ty1QcrQ74dMd6IKGPLDJj2ZDLUNrAL8MW6UVMj8SLzanoPJOhg05n2PjPCOLetQSM7+mkYkQ==");
//        PublicKey publicKey1 = RSAUtils.getPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiLkUWwC+FVx80P8fF01U9Nl5L+ZbU9yR9T+Of+GS3OMxB4nUCL7UGUSehPz+OcgRqJVmUfl7Sfh9fKSdRlMyIka/6aAeEODOJ3++gMsijtDvr+wOqPfLcQXI1QNygDG7s7VoPNVGEO2gsiD6nkoYo6VWPWheg9dLgu8a7ayzZCQIDAQAB");
//        System.out.println("是否为相同密钥========="+ privateKey1.equals(privateKey));
//        //注：privateKey1和privateKey一样
//
//        str = "0123qwer";
//
//        //使用公钥加密
//        String encryptStr = RSAUtils.encrypt(str,publicKey1);
//        System.out.println("这是加密后的内容"+encryptStr);
//
//        //使用私钥解密
//        String decryptStr = RSAUtils.decrypt(encryptStr,privateKey1);
//        System.out.println("这是解密后的内容"+decryptStr);
//
//
//        String sign = RSAUtils.sign(str, privateKey1);
//        System.out.println("这是签名"+sign);
//
//        boolean result = RSAUtils.verify(str, publicKey1, sign);
//        System.out.println("验签结果"+result);
//
//        redisUtil.saveRSAKeyPair();
//        System.out.println("===========");
//
//        if (redisService.hasKey("privateKey") && redisService.hasKey("publicKey")){
//            System.out.println("redis中存在密钥对");
//            System.out.println("privateKey:"+redisUtil.getPrivateKey());
//            System.out.println("publicKey:"+redisUtil.getPublicKey());
//            }else {
//
//            System.out.println("redis中不存在密钥对");
//            redisUtil.saveRSAKeyPair();
//        }
////        KeyPair keyPair2 =  redisUtil.getRSAKeyPair();
////        PrivateKey privateKey2 =keyPair2.getPrivate();
////        PublicKey publicKey2 = keyPair2.getPublic();
////        System.out.println("这是私钥"+privateKey2);
////        System.out.println("这是公钥"+publicKey2);
////
////        PrivateKey privateKeyStr = RSAUtils.getPrivateKey(privateKey.getEncoded());
////        PublicKey publicKeyStr = RSAUtils.getPublicKey(publicKey);
//    }

}
