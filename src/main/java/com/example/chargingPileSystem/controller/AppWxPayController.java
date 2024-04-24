package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.StockUserCharge;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.mapper.UserMapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户充值操作
 * @author kongling
 * @package com.suda.platform.controller.app
 * @date 2019-06-17  11:21
 * @project charging_pile_cloud
 */
@RestController
@RequestMapping("/charging/app/WxPay")
public class AppWxPayController {

    @Resource
    private WxPayConfig wxPayConfig;


    @Autowired
    private ChargingService chargingService;
    @Resource
    private UserMapper userMapper;

    /**
     * 用户小程序充值
     */

    @PostMapping( "/wxAppPay")
    public Object wxPay(@RequestBody StockUserCharge charge) throws WxPayException {

        System.out.println("用户充值参数："+charge+"测试service:"+wxPayConfig.getAppId());
//        if (charge.getUserOpenid() == null || charge.getUserOpenid().equals("") ){
//            return R.fail(ErrorEnum.USER_ID_EMPTY_ERROR,"用户openid为空");
//        }else if (userMapper.queryUserByPhone(charge.getUserOpenid()) == null ){
//            return R.fail(ErrorEnum.USER_ID_EMPTY_ERROR,"用户openid为不存在");
//        }
        Object result= chargingService.createUserCharge(charge);

        return result;
    }


    /**
     * 微信充值回调
     */
    @ResponseBody
    @RequestMapping("/wxBack")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        WxPayService wxPayService = getWxPayService(wxPayConfig);
        try {
            System.out.println("进入回调");
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            System.out.println("xmlResult:"+xmlResult);
            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
            System.out.println("回调结果："+result);
            // 结果正确
            String orderId = result.getOutTradeNo();
            String tradeNo = result.getTransactionId();
            String deviceInfo = result.getDeviceInfo();
            String openId = result.getOpenid();
            int totalfeee = result.getTotalFee();
//            //自己处理订单的业务逻辑，需要判断订单是否已经支付过，否则可能会重复调用
//            StockUserCharge stockUserCharge = stockUserChargeService.getOne(new QueryWrapper<StockUserCharge>()
//            .eq("swift_no",orderId));
//            if(stockUserCharge.getWithdrawStatus() == WithdrawStatusEnum.STATUS_4.getCode().byteValue()){
//               //更新付款状态 用户付款成功
//                stockUserCharge.setTranId(tradeNo);
//                stockUserChargeService.withdrawStatusSuccess(stockUserCharge);
//            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    public WxPayService getWxPayService(WxPayConfig wxPayConfig) {
        System.out.println("wxPayService 开始创建");
        WxPayService payService = new WxPayServiceImpl();
        System.out.println("wxPayService 构造方法结束");
        payService.setConfig(wxPayConfig);
        System.out.println("wxPayService 创建完成");
        System.out.println("wx04a5a6484e9716c2".equals(payService.getConfig().getAppId()));
        return payService;
    }

}
