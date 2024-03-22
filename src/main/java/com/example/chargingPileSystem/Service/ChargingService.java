package com.example.chargingPileSystem.Service;

import com.example.chargingPileSystem.commen.R;

public interface ChargingService {

    // 充电时充电桩状态
    public R<?> state(String userOpenId);

    //充电前status
    public R<?> status(String userOpenId);
}
