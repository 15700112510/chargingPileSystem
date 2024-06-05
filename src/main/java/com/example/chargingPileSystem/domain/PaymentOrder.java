package com.example.chargingPileSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  PaymentOrder {
    //数据库记录id
    private Integer id;
    //订单号
    private String outTradeNo;
    //用户openid
    private String userOpenid;
    //金额
    private Integer amount;
    //充电桩id
    private String chargingPileId;
    //微信支付订单号
    private String transactionId;
    //充电记录id
    private String chargingRecordId;
    //创建时间
    private Timestamp createTime;
    //状态
    private Integer status;
    //退款金额
    private Integer refundAmount;
    //退款单号
    private String outRefundNo;
}
