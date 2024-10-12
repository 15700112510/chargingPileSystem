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
     *  剩余退款
     */
    public void remainingRefund(String chargingPileId);
    /**
     *  全额退款
     */
    public void fullRefund(String chargingPileId) ;
    /**
     * 退款回调
     */
    public void redRefundNotify(String xmlData);

//    根据充电桩编码查找最新支付记录
    public PaymentOrder queryLastRecord(String chargePileId);

;
}
