package com.example.chargingPileSystem.mapper;


import com.example.chargingPileSystem.form.StateForm;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface ChargingMapper {
    //获取充电状态
    public StateForm queryChargingPileState(String chargingPileId);

    //获取开始充电时间
    public String queryUpTime(String chargingPileInfo);

    //获取充电CP信号
    public int queryStage(String chargingPileId);


}
