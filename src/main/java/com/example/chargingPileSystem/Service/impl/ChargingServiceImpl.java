package com.example.chargingPileSystem.Service.impl;

import com.example.chargingPileSystem.Service.ChargingService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.ChargingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ChargingServiceImpl implements ChargingService {
    @Resource
    private ChargingMapper chargingMapper;

    @Override
    public R<?> state(String chargingPileId) {
        StateForm stateForm = chargingMapper.queryChargingPileState(chargingPileId);

        return R.ok(stateForm);
    }
}
