package com.example.chargingPileSystem.Service.jsapi;

import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.ChargingPileRecord;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

public interface PileRecordService {
    //开始充电

    //查找最近一条充电记录
    public ChargingPileRecord queryLastChargingRecord(String chargingPileId);

    //插入充电记录
    public R<?> insertChargingRecord(ChargingPileRecord chargingPileRecord);

    //根据openid返回recordId数组
    public R<?> getRecordIdByUserOpenid(String userOpenid);

    //根据订单号outTradeNo返回record实体类
    public ChargingPileRecord getRecordByOutTradeNo(String outTradeNo);

    //根据用户openid返回该用户全部订单
    List<ChargingPileRecord> getAllRecordByUserOpenId(String userOpenId);

    //获取充电所付款金额
    public R<?> getPaidPrice(String chargingPileId) ;

}
