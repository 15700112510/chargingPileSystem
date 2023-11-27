package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.ChargingPileInfo;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/record")
public class PileRecordController {
    @Resource
    private PileRecordService pileRecordService;




    @GetMapping(value = "/open")
    public R<?> state(@RequestParam String chargingPileId) throws MqttException {
        return pileRecordService.openPile(chargingPileId);
    }
}
