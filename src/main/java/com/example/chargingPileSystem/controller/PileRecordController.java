package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.jsapi.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/charging/api/record")
public class PileRecordController {
    @Resource
    private PileRecordService pileRecordService;
    @GetMapping(value = "/open")
    public R<?> openPile(@RequestParam String chargingPileId) throws MqttException {
        return pileRecordService.openPile(chargingPileId);
    }
    @GetMapping(value = "/close")
    public R< ? > closePile(@RequestParam String chargingPileId) throws MqttException{
        return pileRecordService.closePile(chargingPileId);
    }
    @GetMapping(value = "/appointment")
    public R< ? > appointmentCharging(@RequestParam String chargingPileId,@RequestParam String appointmentTime) throws MqttException{
        return pileRecordService.appointmentTime(chargingPileId,appointmentTime);
    }


}
