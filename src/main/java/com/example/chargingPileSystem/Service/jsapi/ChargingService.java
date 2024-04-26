package com.example.chargingPileSystem.Service.jsapi;

import com.example.chargingPileSystem.commen.R;
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
    public R<?> openPile(String chargingPileId) throws MqttException;


    //关闭充电
    public R<?> closePile(String chargingPileId) throws MqttException;

    //预约充电
    public R<?> appointmentTime(String chargingPileId,String appointmentTime) throws MqttException;


}
