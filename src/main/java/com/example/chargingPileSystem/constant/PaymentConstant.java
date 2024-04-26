package com.example.chargingPileSystem.constant;

public class PaymentConstant {
    public static final int PAY_SUCCESS = 0; //支付成功
    public static final int PAY_FAIL = 1; //支付失败
    public static final int PAY_PROCESSING = 2; //支付中
    public static final int PAY_REFUND_FAIL = 10; //退款失败
    public static final int PAY_REFUND_SUCCESS = 11; //退款成功
    public static final int PAY_REFUND_PROCESSING = 12; //退款中
    public static final String PAY_CALLBACK_URL = "https://zeddy.online/charging/app/WxPay/payBack";//支付回调地址
    public static final String PAY_REFUND_CALLBACK_URL = "https://zeddy.online/charging/app/WxPay/refundBack";//退款回调地址
}
