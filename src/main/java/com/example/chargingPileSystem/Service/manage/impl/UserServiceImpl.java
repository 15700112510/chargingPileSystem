package com.example.chargingPileSystem.Service.manage.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.chargingPileSystem.Service.manage.UserService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.constant.JwtConstant;
import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.form.LoginBackForm;
import com.example.chargingPileSystem.mapper.UserMapper;
import com.example.chargingPileSystem.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.util.UUID;

import static com.example.chargingPileSystem.constant.JwtConstant.SECRET;

@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;


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

    public JSONObject getPhoneNumber(String code) {
        JSONObject phone;
        // 获取token
        String token_url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appID, SECRET);
        try {
            JSONObject token = JSON.parseObject(HttpClientSslUtils.doGet(token_url));
            if (token == null) {
                log.info("获取token失败");
                return null;
            }
            String accessToken = token.getString("access_token");
            if (StringUtils.isEmpty(accessToken)) {
                log.info("获取token失败");
                return null;
            }
            log.info("token : {}", accessToken);
            //获取phone
            String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber"
                    + "?access_token=" + accessToken;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", code);
            String reqJsonStr = JsonUtil.objToString(jsonObject);
            phone = JSON.parseObject(HttpClientSslUtils.doPost(url, reqJsonStr));

            if (phone == null) {
                log.info("获取手机号失败");
                return null;
            }
            return phone;
        } catch (Exception e) {
            e.printStackTrace();
        }
<<<<<<< HEAD
        return null;
=======

        //判断密码是否为空
        if (loginForm.getPassword() == null) {
            return R.fail(ErrorEnum.PASSWORD_EMPTY_ERROR);
        }

        //判断用户是否存在
        UserInfo userInfo = userMapper.queryUserByPhone(loginForm.getUserPhone());
        if (userInfo == null) {
            return R.fail(ErrorEnum.USERNAME_NO_EXIST_ERROR);
        }

        //判断密码是否正确
        //获取私钥解密
        PrivateKey privateKey = redisUtil.getPrivateKey();
        String decodePsw = RSAUtils.decrypt(loginForm.getPassword(), privateKey);
        //加盐加密


        String salt = userInfo.getSlat();
        String hashPsw = HashEncodeUtil.getEncodePsw(decodePsw,salt);
        if (!userInfo.getPassword().equals(hashPsw)) {
            return R.fail(ErrorEnum.PASSWORD_NO_MATCH_ERROR);
        }

        //判断用户角色是否为空
        if (userInfo.getRole() == null) {
            return R.fail(ErrorEnum.ROLE_EMPTY_ERROR);
        }

        //判断用户名是否为空
        if (userInfo.getUserName() == null) {
            return R.fail(ErrorEnum.USERNAME_EMPTY_ERROR);
        }

//        //判断充电桩是否绑定
//        if (userInfo.getChargingPileId() == null) {
//            return R.fail(ErrorEnum.CHARGING_PLIE_ID_EMPTY_ERROR);
//        }

        //判断openid是否为空
        if (userInfo.getUserOpenId() == null) {
            return R.fail(ErrorEnum.USER_OPEN_ID_EMPTY_ERROR);
        }

        // 3. 生成token
        claims.put("userPhone", userInfo.getUserPhone());
        claims.put("role", userInfo.getRole());
        String token = JwtUtil.creatToken(claims, JwtConstant.VALID_TOKEN_TTL);

        // 4. 返回token
        LoginBackForm loginBackForm = LoginBackForm.builder().
                token(token).
                chargingPileId(userInfo.getChargingPileId()).
                userPhone(userInfo.getUserPhone()).
                userOpenId(userInfo.getUserOpenId()).
                userName(userInfo.getUserName()).
                role(userInfo.getRole()).build();
        return R.ok(loginBackForm);

    }


<<<<<<< HEAD
=======
        //判断密码是否为空
        if (userInfo.getPassword() == null) {
            return R.fail(ErrorEnum.PASSWORD_EMPTY_ERROR);
        }

        //判断用户角色是否为空
        if (userInfo.getRole() == null) {
            return R.fail(ErrorEnum.ROLE_EMPTY_ERROR);
        }

        //判断用户名是否为空
        if (userInfo.getUserName() == null) {
            return R.fail(ErrorEnum.USERNAME_EMPTY_ERROR);
        }

//        //判断充电桩是否为空
//        if (userInfo.getChargingPileId() == null) {
//            return R.fail(ErrorEnum.CHARGING_PLIE_ID_EMPTY_ERROR);
//        }

        //判断openid是否为空
        if (userInfo.getUserOpenId() == null) {
            return R.fail(ErrorEnum.USER_OPEN_ID_EMPTY_ERROR);
        }

        //获取私钥解密
        PrivateKey privateKey = redisUtil.getPrivateKey();
        String decodePsw = RSAUtils.decrypt(userInfo.getPassword(), privateKey);

        //加盐加密
        String salt = UUID.randomUUID().toString().replace("-", "");
        String hashPsw = HashEncodeUtil.getEncodePsw(decodePsw,salt);
        userInfo.setPassword(hashPsw);
        userInfo.setSlat(salt);
        userMapper.register(userInfo);
        return R.ok();
    }
>>>>>>> 6a33e8da1bec5b1a2752b7f6ef9fb08de2fef08e
}
