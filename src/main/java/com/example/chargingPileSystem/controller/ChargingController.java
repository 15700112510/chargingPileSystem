package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.Service.jsapi.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.ChargingPileRecord;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/charging/api/charging")
public class ChargingController {
    @Resource
    private ChargingService chargingService;

    @GetMapping(value = "/open")
    public R<?> openPile(@RequestParam String chargingPileId) throws MqttException {
        return chargingService.openPile(chargingPileId);
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


}
