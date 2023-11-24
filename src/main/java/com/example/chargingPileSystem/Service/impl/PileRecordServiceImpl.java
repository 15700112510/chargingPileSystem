package com.example.chargingPileSystem.Service.impl;

import com.example.chargingPileSystem.Service.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.PileRecordMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.Normalizer;

@Service
public class PileRecordServiceImpl implements PileRecordService {
    @Resource
    private PileRecordMapper pileRecordMapper;

    @Override
    public R<?> state(String chargingPileId){

        StateForm stateForm = pileRecordMapper.queryChargingPileState(chargingPileId);
//        Timestamp uptime = stateForm.getUptime();
//
//        long uptimeTime = uptime.getTime();
//
//        long currentTimeMillis = System.currentTimeMillis();
//        long temp = currentTimeMillis - uptimeTime;
//
//        return R.ok(temp);
        System.out.println(stateForm.getUpTime());
        return R.ok();
    }
}
