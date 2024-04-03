package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.ChargingPileRecord;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/charging/api/charging")
public class ChargingController {
    @Resource
    private ChargingService chargingService;

    @PostMapping( "/state")
    public R<?> state(@RequestBody ChargingPileRecord chargingPileRecord) {
        return chargingService.state(chargingPileRecord.getChargingPileId());
    }


}
