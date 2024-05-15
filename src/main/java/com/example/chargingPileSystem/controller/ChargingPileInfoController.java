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
    public int getChargingPrice(@RequestParam String chargingPileId) throws Exception {
        return chargingPileInfoService.getChargingPrice(chargingPileId);
    }

    @GetMapping("/updateChargingPrice")
    public R<?> updateChargingPrice(@RequestParam String chargingPileId,@RequestParam Double chargingPrice) throws Exception {
        return chargingPileInfoService.updateChargingPrice(chargingPileId,chargingPrice);
    }

    //1.获取当前所有充电桩
    @GetMapping("/all")
    public R<?> ChargingPileInfo() {
        return chargingPileInfoService.ChargingPileInfo();
    }

    //2.新增充电桩
    @PostMapping("/insert")
    public R<?> InsertInfo(@RequestBody String chargingPileInfo) {
        return chargingPileInfoService.insertChargingPile(chargingPileInfo);
    }

    //3.获取当前充电桩状态
    @GetMapping("/status")
    public R<?> ChargingPileStatus(@RequestBody String ChargingPileId) {
        return chargingPileInfoService.getChargingPileStatus(ChargingPileId);
    }

    //4.获取当前机床充电情况
    @PostMapping("/charging")
    public R<?> InsertCont(@RequestBody String ChargingPileId) {
        return chargingPileInfoService.getCharging(ChargingPileId);
    }

    //5.删除机器
    @DeleteMapping ("/delete")
    public R<?> DeleteChargingPile(@RequestParam String ChargingPileId) {
        return chargingPileInfoService.deleteChargingPile(ChargingPileId);
    }


}
