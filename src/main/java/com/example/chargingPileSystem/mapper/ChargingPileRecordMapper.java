package com.example.chargingPileSystem.mapper;

import com.example.chargingPileSystem.domain.ChargingPileRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
    //根据userOpenid查找recordId表
    public String queryRecordIdByOpenId(String userOpenid);
    //根据订单号outTradeNo返回record实体类
    public ChargingPileRecord queryRecordByOutTradeNo(String outTradeNo);

    //获取开始充电时间
    public String queryUpTime(String chargingPileInfo);

    //获取充电CP信号
    public int queryStage(String chargingPileId);

    //获取订单状态
    public Integer queryOrderStatus(String chargingPileId);

    //更新订单状态
    public int updateOrderStatus(String chargingRecordId,int orderStatus);

    //根据用户openid返回该用户全部订单
    List<ChargingPileRecord> getAllRecordByUserOpenId(String userOpenId);


}
