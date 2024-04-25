package com.example.chargingPileSystem.Service.jsapi;

import com.example.chargingPileSystem.domain.PaymentOrder;
import com.example.chargingPileSystem.domain.StockUserCharge;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.springframework.stereotype.Service;


public interface PaymentService {

    /**
     * 用户充值创建订单
     * @param
     * @return
     */
    Object createPreOrder(PaymentOrder paymentOrder) throws WxPayException;


    /**
     * 支付回调
     */
    void PayCallback(String xmlData);

    /**
     *  退款
     */
    public void redRefundPay(PaymentOrder paymentOrder,int refundAmount) throws WxPayException;

    /**
     * 退款回调
     */
//    public String redRefundNotify(String xmlData);
}
