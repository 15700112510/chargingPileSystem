package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.ChargingPileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChargingPileInfoMapper {
    //更新充电桩状态
    public int updateStatus(Integer status, Integer id);
    //获取全部充电桩
    public List<ChargingPileInfo> queryChargingPileList();
    //根据imei号查找id
    public String queryId(String ChargingPileId);
    //加入新充电桩
    public int insertChargingPile(String chargingPileInfo);
    //更新充电桩信息
    public int updateChargingPile(ChargingPileInfo chargingPileInfo);
    //获取CP信号
    public String queryStage(String id);
}
