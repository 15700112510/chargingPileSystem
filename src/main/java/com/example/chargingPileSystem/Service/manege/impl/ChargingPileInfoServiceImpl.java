package com.example.chargingPileSystem.Service.manege.impl;

import com.example.chargingPileSystem.Service.manege.ChargingPileInfoService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.ChargingPileInfo;
import com.example.chargingPileSystem.form.ChargingPileInfoForm;
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
    public R<?> getChargingPileStatus(String ChargingPileId) {
        return R.ok(chargingPileInfoMapper.queryStatus(ChargingPileId));
    }

    //4.获取当前机床充电情况
    @Override
    public R<?> getCharging(String ChargingPileId) {
        return R.ok(chargingPileInfoMapper.queryCharging(ChargingPileId));
    }

    //5、删除机器
    @Override
    public R<?> deleteChargingPile(String ChargingPileId) {
        int i = chargingPileInfoMapper.deleteChargingPile(ChargingPileId);
        int a = chargingPileRecordMapper.deleteChargingPileInfo(ChargingPileId);
        return R.ok("删除数据"+i+"；删除总数据"+a);
    }

    //获取充电单价
    @Override
    public R<?> getChargingPrice(String ChargingPileId) {
        return R.ok(chargingPileInfoMapper.getPrice(ChargingPileId));
    }
    //修改充电单价
    public R<?> updateChargingPrice(String ChargingPileId,Double ChargingPrice) {
        return R.ok(chargingPileInfoMapper.updatePrice(ChargingPileId, ChargingPrice));
    }

}
