package com.example.chargingPileSystem.Service.manege.impl;

import com.example.chargingPileSystem.Service.manege.ChargingPileInfoService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.ChargingPileInfo;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import com.example.chargingPileSystem.mapper.ChargingPileRecordMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service("ChargingPileInfoService")
public class ChargingPileInfoServiceImpl implements ChargingPileInfoService {

    @Resource
    private ChargingPileInfoMapper chargingPileInfoMapper;

    @Resource
    private ChargingPileRecordMapper chargingPileRecordMapper;

    //1.获取当前所有充电桩
    @Override
    public R<?> ChargingPileInfo() {
        List<ChargingPileInfo> chargingPileInfo = chargingPileInfoMapper.queryChargingPileList();
        return R.ok(chargingPileInfo);
    }

    //2.新增充电桩
    @Override
    public R<?> insertChargingPile(String chargingPileInfo) {
        int num = chargingPileInfoMapper.insertChargingPile(chargingPileInfo);
        return R.ok(num);
    }

    //3、获取当前充电桩状态
    @Override
    public R<?> getChargingPileStatus(String chargingPileId) {
        return R.ok(chargingPileInfoMapper.queryStatus(chargingPileId));
    }

    //4.获取当前机床充电情况
    @Override
    public R<?> getCharging(String chargingPileId) {
        return R.ok(chargingPileInfoMapper.queryCharging(chargingPileId));
    }

    //5、删除机器
    @Override
    public R<?> deleteChargingPile(String chargingPileId) {
        int i = chargingPileInfoMapper.deleteChargingPile(chargingPileId);
        int a = chargingPileRecordMapper.deleteChargingPileInfo(chargingPileId);
        return R.ok("删除数据"+i+"；删除总数据"+a);
    }


    //获取充电单价
    @Override
    public int getChargingPrice(String chargingPileId) {
        return chargingPileInfoMapper.getPrice(chargingPileId);
    }

    //修改充电单价
    public R<?> updateChargingPrice(String chargingPileId,Double chargingPrice) {
        return R.ok(chargingPileInfoMapper.updatePrice(chargingPileId, chargingPrice));
    }

}
