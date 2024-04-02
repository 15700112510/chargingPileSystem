package com.example.chargingPileSystem.wechat;

import com.github.binarywang.wxpay.config.WxPayConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Data
@Slf4j
@ConfigurationProperties("wechat")
public class WechatConfigProperties {
    @Resource
    private WxPayConfig wxPayConfig;
    private String appId;

    private String appSecret;

    private String GrantType = "client_credential";

    /**
     * 生成微信登录请求地址
     *
     * @param code code
     * @return 请求地址
     */
    public String getWxLoginUrl(String code) {

        return String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", wxPayConfig.getAppId(), appSecret, code);
    }

    /**
     * 生成微信ACCESS_TOKEN请求地址
     * <p>
     * 模板样式: https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s
     *
     * @return 请求地址
     */
    public String getATokenUrl() {
        return String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appId, appSecret);
    }


    /**
     * 获取手机号url post请求
     *
     * @param access_token
     * @return
     */
    public String getPhoneUrl(String access_token) {
        return String.format("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=%s", access_token);
    }

    /**
     * 获取不限制的微信二维码url
     *
     * @param accessToken
     * @return
     */
    public String getWxACodeUnLimitUrl(String access_token) {
        return String.format("${wechat.wxBaseRequestUrl}/wxa/getwxacodeunlimit?access_token=%s", access_token);
    }

    /**
     * 获取(限制一)的微信二维码url
     *
     * @param accessToken
     * @return
     */
    public String getWxACodeUrl(String access_token) {
        return String.format(" ${wechat.wxBaseRequestUrl}/wxa/getwxacode?access_token=%s", access_token);
    }

    /**
     * 获取(限制二)的微信二维码url
     *
     * @param accessToken
     * @return
     */
    public String getWxAQrcodeUrl(String access_token) {
        return String.format("${wechat.wxBaseRequestUrl}/cgi-bin/wxaapp/createwxaqrcode?access_token=%s", access_token);
    }
}
