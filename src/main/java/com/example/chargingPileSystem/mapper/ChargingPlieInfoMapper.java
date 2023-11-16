package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.ChargingPlieInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChargingPlieInfoMapper {
    //更新充电桩状态
    public int updateStatus(Integer status, Integer id);
    //获取全部充电桩
    public List<ChargingPlieInfo> queryChargingPileList();
    //根据imei号查找id
    public String queryId(String ChargingPileIId);
    //加入新充电桩
    public int insertChargingPile(String chargingPileInfo);
    public int updateChargingPile(ChargingPlieInfo chargingPileInfo);
}
