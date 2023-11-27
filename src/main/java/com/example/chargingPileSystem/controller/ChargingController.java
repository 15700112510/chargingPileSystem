package com.example.chargingPileSystem.controller;

import com.example.chargingPileSystem.Service.ChargingService;
import com.example.chargingPileSystem.commen.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/charging")
public class ChargingController {
    @Resource
    private ChargingService chargingService;

    @GetMapping(value = "/state")
    public R<?> state(@RequestParam String chargingPileId)  {
        return chargingService.state(chargingPileId);
    }


}
