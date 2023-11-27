package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.ChargingPileRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChargingPileRecordMapper {
    public ChargingPileRecord queryLastRecord(String chargingPileId);
    public int updateChargingPileRecord(ChargingPileRecord chargingPileRecord);
    public int insertChargingPileRecord(ChargingPileRecord chargingPileRecord);
}
