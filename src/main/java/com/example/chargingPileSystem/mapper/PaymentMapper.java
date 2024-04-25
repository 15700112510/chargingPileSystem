package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.PaymentOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    //创建预订单
    public void insertPreOrder(String outTradeNo, String userOpenid, String chargingPileId, int amount, int status);

    //根据订单号查询订单信息
    public PaymentOrder selectByOrderNo(String outTradeNo);

    //更新订单状态
    public void updateOrderStatus(String outTradeNo, String transactionId, String chargingRecordId, int status);

    //更新订单状态
    public void updatePay(PaymentOrder paymentOrder);

//    //查询订单状态
//    public String queryOrderStatus(String orderId);
//    //查询订单金额
//    public String queryOrderAmount(String orderId);
}
