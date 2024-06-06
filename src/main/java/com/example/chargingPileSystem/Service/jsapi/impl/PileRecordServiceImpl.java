package com.example.chargingPileSystem.Service.jsapi.impl;

import com.example.chargingPileSystem.Service.jsapi.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.ChargingPileRecord;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import com.example.chargingPileSystem.mapper.ChargingPileRecordMapper;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class PileRecordServiceImpl implements PileRecordService {
    @Resource
    ChargingPileRecordMapper chargingPileRecordMapper;

    //查找最近一条充电记录
    @Override
    public ChargingPileRecord queryLastChargingRecord(String chargingPileId) {
        ChargingPileRecord chargingPileRecord = chargingPileRecordMapper.queryLastRecord(chargingPileId);
        return chargingPileRecord;
    }

    //插入充电记录
    @Override
    public R<?> insertChargingRecord(ChargingPileRecord chargingPileRecord) {
        return R.ok(chargingPileRecordMapper.insertChargingPileRecord(chargingPileRecord));
    }

    //根据openid返回recordId数组
    @Override
    public R<?> getRecordIdByUserOpenid(String userOpenid) {
        return R.ok(chargingPileRecordMapper.queryRecordIdByOpenId(userOpenid));
    }

    //根据订单号outTradeNo返回record实体类
    @Override
    public ChargingPileRecord getRecordByOutTradeNo(String outTradeNo) {
        return chargingPileRecordMapper.queryRecordByOutTradeNo(outTradeNo);
    }

    //根据用户openid返回该用户全部订单
    @Override
    public List<ChargingPileRecord> getAllRecordByUserOpenId(String userOpenId) {
        return chargingPileRecordMapper.getAllRecordByUserOpenId(userOpenId);
    }

    //获取充电所付款金额
    @Override
    public R<?> getPaidPrice(String chargingPileId) {
        return R.ok(chargingPileRecordMapper.getPaidPrice(chargingPileId));
    }
}
