package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.ChargingPileRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChargingPileRecordMapper {
    //获取最近一条充电记录
    public ChargingPileRecord queryLastRecord(String chargingPileId);
    //更新充电记录
    public int updateChargingPileRecord(ChargingPileRecord chargingPileRecord);
    //插入充电记录
    public int insertChargingPileRecord(ChargingPileRecord chargingPileRecord);
    //删除指定充电桩所有数据
    public int deleteChargingPileInfo(String chargingPileId);
}
