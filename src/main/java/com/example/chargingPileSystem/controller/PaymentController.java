package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.jsapi.PaymentService;
import com.example.chargingPileSystem.domain.PaymentOrder;
import com.example.chargingPileSystem.domain.StockUserCharge;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户充值操作
 *
 * @author kongling
 * @package com.suda.platform.controller.app
 * @date 2019-06-17  11:21
 * @project charging_pile_cloud
 */
@RestController
@RequestMapping("/charging/app/WxPay")
public class PaymentController {
    @Resource
    private PaymentService paymentService;

    /**
     * 用户小程序充值
     */
    @PostMapping("/wxAppPay")
    public Object prePayment(@RequestBody PaymentOrder paymentOrder) throws WxPayException {
        Object result = paymentService.createPreOrder(paymentOrder);
        return result;
    }

    /**
     * 微信充值回调
     */
    @ResponseBody
    @RequestMapping("/payBack")
    public String payNotify(@RequestBody String xmlData) {
        paymentService.PayCallback(xmlData);
        return WxPayNotifyResponse.success("处理成功!");
    }

    /**
     * 微信退款回调
     */
    @ResponseBody
    @RequestMapping("/refundBack")
    public String refundNotify(@RequestBody String xmlData) {
        paymentService.redRefundNotify(xmlData);
        return WxPayNotifyResponse.success("处理成功!");

    }


}
