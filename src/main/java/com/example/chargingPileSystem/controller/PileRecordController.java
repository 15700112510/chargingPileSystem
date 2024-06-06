package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.jsapi.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.ChargingPileRecord;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/charging/api/record")
public class PileRecordController {

    @Resource
    private PileRecordService pileRecordService;

    //查找最近一条充电记录
    @GetMapping("/lastRecord")
    public ChargingPileRecord queryLastChargingRecord(@RequestParam String chargingPileId) throws MqttException {
        return pileRecordService.queryLastChargingRecord(chargingPileId);
    }

    //插入充电记录
    @PostMapping("/insert")
    public R<?> insertChargingRecord(@RequestBody ChargingPileRecord chargingPileRecord) throws MqttException {
        return pileRecordService.insertChargingRecord(chargingPileRecord);
    }

    //根据openid返回recordId数组
    @PostMapping("/recordId")
    public R<?> getRecordIdByUserOpenid(@RequestBody String userOpenid) throws MqttException {
        return pileRecordService.getRecordIdByUserOpenid(userOpenid);
    }

    //根据订单号outTradeNo返回record实体类
    @PostMapping("/record")
    public ChargingPileRecord getRecordByOutTradeNo(@RequestBody String outTradeNo) throws MqttException {
        return pileRecordService.getRecordByOutTradeNo(outTradeNo);
    }

    //根据用户openid返回该用户全部订单
    @GetMapping("/getUserRecord")
    public R<List<ChargingPileRecord>> getUserRecord(@RequestParam String userOpenId) {
        List<ChargingPileRecord> records = pileRecordService.getAllRecordByUserOpenId(userOpenId);
        return R.ok(records);
    }


}
