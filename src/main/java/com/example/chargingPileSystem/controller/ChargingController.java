package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.Service.jsapi.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.ChargingPileRecord;
import com.example.chargingPileSystem.domain.PaymentOrder;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/charging/api/charging")
public class ChargingController {
    @Resource
    private ChargingService chargingService;
    @Resource
    private MqttClient mqttClient;

    @GetMapping(value = "/open")
    public R<?> openPile(@RequestParam PaymentOrder paymentOrder) throws MqttException {
        return chargingService.openPile(paymentOrder);
    }

    @GetMapping(value = "/testOpen")
    public void openPile(@RequestParam String chargingPileId) throws MqttException {
        String content = "POWER_ENABLE";
        String topic = "CDZ/" + chargingPileId + "/Config";
        MqttMessage msg = new MqttMessage(content.getBytes());
        mqttClient.publish(topic, msg);
    }
    @GetMapping(value = "/close")
    public R< ? > closePile(@RequestParam String chargingPileId) throws MqttException{
        return chargingService.closePile(chargingPileId);
    }
    @GetMapping(value = "/appointment")
    public R< ? > appointmentCharging(@RequestParam String chargingPileId,@RequestParam String appointmentTime) throws MqttException{
        return chargingService.appointmentTime(chargingPileId,appointmentTime);
    }

    @PostMapping( "/state")
    public R<?> state(@RequestBody ChargingPileRecord chargingPileRecord) {
        return chargingService.state(chargingPileRecord.getChargingPileId());
    }

    //获取CP信号
    @GetMapping("/getCP")
    public R<?> getStage(@RequestParam String ChargingPileId) {
        return chargingService.getStage(ChargingPileId);
    }


}
