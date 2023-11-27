package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/record")
public class PileRecordController {
    @Resource
    private PileRecordService pileRecordService;

    @GetMapping(value = "/state")
    public R<?> state(@RequestParam String chargingPileId) {
        return pileRecordService.state(chargingPileId);
    }


}
