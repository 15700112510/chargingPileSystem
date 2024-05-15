package com.example.chargingPileSystem.Service.jsapi;

import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.PaymentOrder;
import com.example.chargingPileSystem.domain.StockUserCharge;
import com.example.chargingPileSystem.domain.UserInfo;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface ChargingService {

    // 充电时充电桩状态
    public R<?> state(String userOpenId);

    //充电前status
    public R<?> status(String userOpenId);

    //充电
    public R<?> openPile(PaymentOrder paymentOrder) throws MqttException;


    //关闭充电
    public R<?> closePile(String chargingPileId) throws MqttException;

    //预约充电
    public R<?> appointmentTime(String chargingPileId,String appointmentTime) throws MqttException;

    //根据充电桩编码获取CP信号
    public R<?> getStage(String ChargingPileId);

    //获取订单状态
    public int getOrderStatus(String ChargingPileId);


}
