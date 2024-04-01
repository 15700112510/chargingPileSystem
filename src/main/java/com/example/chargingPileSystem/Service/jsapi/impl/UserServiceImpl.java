package com.example.chargingPileSystem.Service.jsapi.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSONObject;
import com.example.chargingPileSystem.Service.jsapi.UserService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.form.LoginForm;
import com.example.chargingPileSystem.mapper.UserMapper;
import com.example.chargingPileSystem.redis.RedisService;
import com.example.chargingPileSystem.util.*;
import com.example.chargingPileSystem.wechat.WechatConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.chargingPileSystem.enums.ErrorEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private RestTemplate restTemplate = new RestTemplate();
    private final WechatConfigProperties wechatConfigProperties;
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RedisService redisService;

    public R<?> login(LoginForm loginForm) {
        Map<String, Object> map = new HashMap<>();
        String code = loginForm.getCode();
        JSONObject response = null;
        try {

            // 1. 想微信服务器发送请求获取用户信息
            String url = wechatConfigProperties.getWxLoginUrl(code);
            log.info("===> 请求微信url是: {}", url);

            //2. 远程调用微信接口
            String res = restTemplate.getForObject(url, String.class);
            response = JSONObject.parseObject(res);

            //3. 解析返回参数 报错则进行对应处理
            if (response != null) {
                //3.1 获取session_key和openid
                String session_key = response.getString("session_key");
                String openid = response.getString("openid");
                log.info("===> openid:  {}", openid);
                if (session_key == null || openid == null) {
                    log.error("小程序授权失败,session_key或open_id是空!");
                    return R.fail(MISSING_RETURN_PARAMETER);
                }
                log.info("===> 微信回调信息: {}", response);
            }
            return R.ok();
//
//            // 解析相应内容（转换成json对象）
//            JSONObject json = JSONObject.parseObject(res);
//            log.info("解析code请求结果:" + json.toString());
//
//            //获取openid
//            String openid = json.getString("openid");
//            log.info("openid:" + openid);
//
//            // 获取session_key
//            String session_key = json.getString("session_key");
//            log.info("session_key:" + session_key);
//            map.put("openId", openid);
//            map.put("session_key", session_key);
//            String token = JwtUtil.creatToken(map, JwtConstant.VALID_TOKEN_TTL);
//            redisService.setCacheObject("token", token);
//            return R.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("openId生成失败");
            return R.fail(PASSWORD_EMPTY_ERROR);//随便写的
        }
    }



//    public R<?> login(LoginForm loginForm) {
//        Map<String, Object> map = new HashMap<>();
//        String code = loginForm.getCode();
//
//        try {
//            // 授权（必填）固定
//            String grantType = "authorization_code";
//            // 发送请求
//            String res = String.format("https://api.weixin.qq.com/sns/jscode2session", APPID, SECRET, code, grantType);
//
//            // 解析相应内容（转换成json对象）
//            JSONObject json = JSONObject.parseObject(res);
//            log.info("解析code请求结果:" + json.toString());
//
//            //获取openid
//            String openid = json.getString("openid");
//            log.info("openid:" + openid);
//
//            // 获取session_key
//            String session_key = json.getString("session_key");
//            log.info("session_key:" + session_key);
//            map.put("openId", openid);
//            map.put("session_key", session_key);
//            String token = JwtUtil.creatToken(map, JwtConstant.VALID_TOKEN_TTL);
//            redisService.setCacheObject("token", token);
//            return R.ok(token);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("openId生成失败");
//            return R.fail(PASSWORD_EMPTY_ERROR);//随便写的
//        }
//    }

    public R<?> getPhoneNumber(String code) {
        System.out.println("调用getPhoneNumber（）");
        String access_token = getAccessToken();
        if (access_token == null) {
            log.info("获取token失败");
            return null;
        }
        log.info("token : {}", access_token);
        //获取phone
        String phoneUrl = wechatConfigProperties.getPhoneUrl(access_token);
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);

        JSONObject jsonObject = sendPostRestTemplate(phoneUrl, map, JSONObject.class);
        System.out.println(jsonObject);
        if (jsonObject.containsKey("errcode")) {
            /*如果异常码是0 说明正常*/
            if (!Objects.equals(String.valueOf(jsonObject.get("errcode")), "0")) {
                log.error("===> 获取手机号的异常信息 : {}", jsonObject + "");
                return null;
            }
        }
        JSONObject phoneInfo = jsonObject.getJSONObject("phone_info");
        System.out.println("手机号："+phoneInfo.getString("phoneNumber"));
        return R.ok(phoneInfo.getString("phoneNumber"));
    }

    /**
     * 2. 获取缓存中的AccessToken
     * <p>
     * 没有从微信拉取[可配合定时]
     *
     * @return accessToken
     */
    public String getAccessToken() {
        /*校验: 缓存中有accessToken的key*/
        System.out.println("getAccessToken（）");
        if (redisService.hasKey("access_token")){
            log.info("取出accessToken成功!");
            String access_token = (String) redisService.getCacheObject("access_token");
            return access_token;
        }
        return getWxMiniAccessToken();
    }

    /**
     * 3. 访问微信官方获取两小时的 accessToken
     *
     * @return accessToken
     */
    public String getWxMiniAccessToken() {
        System.out.println("getWxMiniAccessToken（）");
        Map<String, String> query = new HashMap<>();
        query.put("grant_type", wechatConfigProperties.getGrantType());//client_credential
        query.put("secret", wechatConfigProperties.getAppSecret());
        query.put("appid", wechatConfigProperties.getAppId());
        try {
            String aTokenUrl = wechatConfigProperties.getATokenUrl();
//            String result = restTemplate.getForObject(aTokenUrl,String.class);
//            if (result == null){
//                return R.fail(MISSING_RETURN_PARAMETER)
//            }
//            Object result = restTemplate.getForEntity(aTokenUrl, JSONObject.class, query);

//            ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(aTokenUrl, query, JSONObject.class);
            ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(aTokenUrl, JSONObject.class, query);
            // HttpStatus statusCode = responseEntity.getStatusCode(); //状态码
            JSONObject responseJsonBody = responseEntity.getBody();//响应体
            log.info("[请求微信小程序官方接口] => 获取accessToken请求成功返回值：{}", responseJsonBody);
            if (responseJsonBody == null) {
                log.info("微信小程序获取accessToken请求返回result是null！");
                return null;
//                    throw new ServiceException(ResponseEnum.A10005);
            }
            //获取accessToken
            String access_token = responseJsonBody.getString("access_token");
            if (StringUtils.isEmpty(access_token)) {
                log.info("微信小程序获取accessToken请求返回access_token是null！");
                return null;
//                    throw new ServiceException(ResponseEnum.A10005);
            }
            //放入缓存中
            redisService.expire("access_token", 120, TimeUnit.MINUTES);
            //redisService.setCacheObject("access_token", access_token, wechatConfigProperties.getExpiredTime(), TimeUnit.MINUTES);

            return access_token;
        }catch(Exception e){
            e.printStackTrace();
            log.info("微信小程序获取accessToken请求异常信息 {}", e.getMessage());
            return null;
//            throw new ServiceException(ResponseEnum.A10005);
        }
    }




        /**
         * 远程调用 restTemplate方法 post请求
         *
         * @param url
         * @param body
         * @return
         */
        public <T>T sendPostRestTemplate(String url, Map<String, Object> body, Class<T> responseType) {
            return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, null), responseType).getBody();
        }




//    @Override
//    public R<?> login(LoginForm loginForm) throws Exception {
//        Map<String, Object> claims = new HashMap<>();
//
//        //判断手机号是否存在
//        if (loginForm.getUserPhone() == null) {
//            return R.fail(ErrorEnum.Phone_EMPTY_ERROR);
//        }
//
//        //判断密码是否为空
//        if (loginForm.getPassword() == null) {
//            return R.fail(ErrorEnum.PASSWORD_EMPTY_ERROR);
//        }
//
//        //判断用户是否存在
//        UserInfo userInfo = userMapper.queryUserByPhone(loginForm.getUserPhone());
//        if (userInfo == null) {
//            return R.fail(ErrorEnum.USERNAME_NO_EXIST_ERROR);
//        }
//
//        //判断密码是否正确
//        //获取私钥解密
//        PrivateKey privateKey = redisUtil.getPrivateKey();
//        String decodePsw = RSAUtils.decrypt(loginForm.getPassword(), privateKey);
//        //加盐加密
//        String salt = userInfo.getSlat();
//        String hashPsw = HashEncodeUtil.getEncodePsw(decodePsw,salt);
//        if (!userInfo.getPassword().equals(hashPsw)) {
//            return R.fail(ErrorEnum.PASSWORD_NO_MATCH_ERROR);
//        }
//
//        //判断用户角色是否为空
//        if (userInfo.getRole() == null) {
//            return R.fail(ErrorEnum.ROLE_EMPTY_ERROR);
//        }
//
//        //判断用户名是否为空
//        if (userInfo.getUserName() == null) {
//            return R.fail(ErrorEnum.USERNAME_EMPTY_ERROR);
//        }
//
//        //判断充电桩是否绑定
//        if (userInfo.getChargingPileId() == null) {
//            return R.fail(ErrorEnum.CHARGING_PLIE_ID_EMPTY_ERROR);
//        }
//
//        //判断openid是否为空
//        if (userInfo.getUserOpenId() == null) {
//            return R.fail(ErrorEnum.USER_OPEN_ID_EMPTY_ERROR);
//        }
//
//        // 3. 生成token
//        claims.put("userPhone", userInfo.getUserPhone());
//        claims.put("role", userInfo.getRole());
//        String token = JwtUtil.creatToken(claims, JwtConstant.VALID_TOKEN_TTL);
//
//        // 4. 返回token
//        LoginBackForm loginBackForm = LoginBackForm.builder().
//                token(token).
//                chargingPileId(userInfo.getChargingPileId()).
//                userPhone(userInfo.getUserPhone()).
//                userOpenId(userInfo.getUserOpenId()).
//                userName(userInfo.getUserName()).
//                role(userInfo.getRole()).build();
//        return R.ok(loginBackForm);
//    }

    //    @Override
//    public R<?> register(UserInfo userInfo) throws Exception {
//        if (userMapper.queryUserByPhone(userInfo.getUserPhone()) != null) {
//            return R.fail(ErrorEnum.USERNAME_NO_EXIST_ERROR);
//        }
//        //判断手机号是否存在
//        if (userInfo.getUserPhone() == null) {
//            return R.fail(ErrorEnum.Phone_EMPTY_ERROR);
//        }
//
//        //判断密码是否为空
//        if (userInfo.getPassword() == null) {
//            return R.fail(ErrorEnum.PASSWORD_EMPTY_ERROR);
//        }
//
//        //判断用户角色是否为空
//        if (userInfo.getRole() == null) {
//            return R.fail(ErrorEnum.ROLE_EMPTY_ERROR);
//        }
//
//        //判断用户名是否为空
//        if (userInfo.getUserName() == null) {
//            return R.fail(ErrorEnum.USERNAME_EMPTY_ERROR);
//        }
//
//        //判断充电桩是否为空
//        if (userInfo.getChargingPileId() == null) {
//            return R.fail(ErrorEnum.CHARGING_PLIE_ID_EMPTY_ERROR);
//        }
//
//        //判断openid是否为空
//        if (userInfo.getUserOpenId() == null) {
//            return R.fail(ErrorEnum.USER_OPEN_ID_EMPTY_ERROR);
//        }
//
//        //获取私钥解密
//        PrivateKey privateKey = redisUtil.getPrivateKey();
//        String decodePsw = RSAUtils.decrypt(userInfo.getPassword(), privateKey);
//
//        //加盐加密
//        String salt = UUID.randomUUID().toString().replace("-", "");
//        String hashPsw = HashEncodeUtil.getEncodePsw(decodePsw,salt);
//        userInfo.setPassword(hashPsw);
//        userInfo.setSlat(salt);
//        userMapper.register(userInfo);
//        return R.ok();
//    }
//

}


