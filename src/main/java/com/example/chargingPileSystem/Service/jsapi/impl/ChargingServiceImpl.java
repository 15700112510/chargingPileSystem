package com.example.chargingPileSystem.Service.jsapi.impl;

import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.ChargingMapper;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ChargingServiceImpl implements ChargingService {
    @Resource
    private ChargingMapper chargingMapper;
    @Resource
    private ChargingPileInfoMapper chargingPileInfoMapper;

    @Override
    public R<?> state(String chargingPileId) {

        int stage = chargingMapper.queryStage(chargingPileId);
//        System.out.println(stage);
//        return R.ok();
        if (!(stage == 12)) {
            return R.fail(ErrorEnum.CHARGING_PLIE_ID_NO_CONNECT_ERROR,"充电桩未连接");
        }
        StateForm stateForm = chargingMapper.queryChargingPileState(chargingPileId);
        System.out.println(stateForm);
        return R.ok(stateForm);
    }

    //获取充电桩Status  0未插枪  1插枪但是没有任何动作 2插枪有命令下发充电但是未充电（车内预约充电状态/充满自停状态） 3正常充电状态  4离线
    @Override
    public R<?> status(String userOpenId) {
        int status = chargingPileInfoMapper.queryStatus(userOpenId);
        return  R.ok(status);
    }
}
