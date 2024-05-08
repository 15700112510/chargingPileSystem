package com.example.chargingPileSystem.controller;


import com.example.chargingPileSystem.Service.manege.ChargingPileInfoService;
import com.example.chargingPileSystem.commen.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/charging/api/chargingPileInfo")
public class ChargingPileInfoController {
    @Resource
    private ChargingPileInfoService chargingPileInfoService;

    @GetMapping("/getChargingPrice")
    public R<?> getChargingPrice(@RequestParam String chargingPileId) throws Exception {
        return chargingPileInfoService.getChargingPrice(chargingPileId);
    }

    @GetMapping("/updateChargingPrice")
    public R<?> updateChargingPrice(@RequestParam String chargingPileId,@RequestParam Double chargingPrice) throws Exception {
        return chargingPileInfoService.updateChargingPrice(chargingPileId,chargingPrice);
    }

}
