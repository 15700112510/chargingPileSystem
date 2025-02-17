package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.ChargingPileInfo;
import com.example.chargingPileSystem.form.StateForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChargingPileInfoMapper {
    //更新充电桩状态
    public int updateStatus(Integer status, String stage, Integer id);
    //获取充电状态
    public StateForm queryChargingPileState(String chargingPileId);

    //获取全部充电桩
    public List<ChargingPileInfo> queryChargingPileList();
    //根据imei号查找id
    public String queryId(String chargingPileId);
    //加入新充电桩
    public int insertChargingPile(String chargingPileInfo);
    //更新充电桩信息
    public int updateChargingPile(ChargingPileInfo chargingPileInfo);
    //获取CP信号
    public String queryStage(String chargingPileId);
    //获取充电桩Status  0未插枪  1插枪但是没有任何动作 2插枪有命令下发充电但是未充电（车内预约充电状态/充满自停状态） 3正常充电状态  4离线
    public int queryStatus(String chargingPileId);
    //获取充电桩充电状态
    public String queryCharging(String chargingPileId);
    //删除充电桩
    public int deleteChargingPile(String chargingPileId);
    //获取充电单价
    public int getPrice(String chargingPileId);
    //修改充电单价
    public int updatePrice(String chargingPileId,Double chargingPrice);

}
