package com.example.chargingPileSystem.Service.impl;

import com.example.chargingPileSystem.Service.PileRecordService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.PileRecordMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class PileRecordServiceImpl implements PileRecordService {
    @Resource
    private PileRecordMapper pileRecordMapper;

    @Override
    public R<?> state(String chargingPileId){

        StateForm stateForm = pileRecordMapper.queryChargingPileState(chargingPileId);
        System.out.println(stateForm.getUpTime());

        Timestamp uptime = stateForm.getUpTime();
        long uptimeTime = uptime.getTime();
        long currentTimeMillis = System.currentTimeMillis();
        long temp = currentTimeMillis - uptimeTime;

        long hours = TimeUnit.MILLISECONDS.toHours(temp);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(temp) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(temp) % 60;
        String time = hours + "小时" + minutes + "分" + seconds + "秒";
//        System.out.println(time);
//        return R.ok(temp);
//        System.out.println(currentTimeMillis);
//        stateForm.setUpTime();

        return R.ok();

    }
}
