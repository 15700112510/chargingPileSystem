package com.example.chargingPileSystem.Service.manege;

import com.example.chargingPileSystem.commen.R;

public interface ChargingPileInfoService {
    //1.获取当前所有充电桩
    public R<?> ChargingPileInfo();

    //2.新增充电桩
    public R<?> insertChargingPile(String chargingPileInfo);

    //3.获取当前充电桩状态
    public R<?> getChargingPileStatus(String chargingPileId);

    //4.获取当前机床充电情况
    public R<?> getCharging(String chargingPileId);

    //4.删除机器
    public R<?> deleteChargingPile(String chargingPileId);

    //获取充电单价
    public int getChargingPrice(String chargingPileId);

    //修改充电单价
    public R<?> updateChargingPrice(String chargingPileId, Double chargingPrice);


}
