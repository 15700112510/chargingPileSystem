package com.example.chargingPileSystem.Service.jsapi.impl;

import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.Service.jsapi.PileRecordService;
import com.example.chargingPileSystem.Service.manege.ChargingPileInfoService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.constant.ChargingPileRecordConstant;
import com.example.chargingPileSystem.domain.ChargingPileRecord;
import com.example.chargingPileSystem.domain.PaymentOrder;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import com.example.chargingPileSystem.mapper.ChargingPileRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

import static com.example.chargingPileSystem.constant.ChargingPileRecordConstant.ORDER_UNACCOMPLISHED;

@Service
@Slf4j
public class ChargingServiceImpl implements ChargingService {
    @Resource
    private ChargingPileInfoMapper chargingPileInfoMapper;
    @Resource
    private ChargingPileRecordMapper chargingPileRecordMapper;
    @Resource
    private MqttClient mqttClient;
    @Resource
    private PileRecordService pileRecordService;
    @Resource
    private ChargingPileInfoService chargingPileInfoService;

    @Override
    public R<?> openPile(PaymentOrder paymentOrder) throws MqttException {
        String content = "POWER_ENABLE";
        String topic = "CDZ/" + paymentOrder.getChargingPileId() + "/Config";
        MqttMessage msg = new MqttMessage(content.getBytes());
        System.out.println("mqttClient.isConnected():" + mqttClient.isConnected());
//        mqttClient.publish(topic, msg);
        //如果CP信号握手成功，则准备充电开启充电桩
//        if (chargingPileRecordMapper.queryStage(paymentOrder.getChargingPileId()) == 9 ) {
        mqttClient.publish(topic, msg);
        ChargingPileRecord chargingPileRecord = new ChargingPileRecord();

        //设置chargingPileId
        chargingPileRecord.setChargingPileId(paymentOrder.getChargingPileId());
        //设置chargingRecordId
        chargingPileRecord.setChargingRecordId(paymentOrder.getChargingRecordId());
        //设置预计充电量
        int Amount = paymentOrder.getAmount();
        int price = chargingPileInfoService.getChargingPrice(paymentOrder.getChargingPileId());
        String expectEnergy = String.valueOf(Amount / price);
        chargingPileRecord.setExpectEnergy(expectEnergy);
        //设置充电订单创建时间
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp creatTime = new Timestamp(currentTimeMillis);
        chargingPileRecord.setCreatTime(creatTime);
        //设置充电人员
        chargingPileRecord.setUserOpenid(paymentOrder.getUserOpenid());
        //设置订单状态（未完成）
        chargingPileRecord.setOrderStatus(ChargingPileRecordConstant.ORDER_UNACCOMPLISHED);
        chargingPileRecord.setSingleEnergy("0");
        //设置充电开始时间
        chargingPileRecord.setUpTime(creatTime);

//, #{upTime}, #{downTime}
        pileRecordService.insertChargingRecord(chargingPileRecord);
        return R.ok("充电桩开启成功");
//        }
//        return R.fail(ErrorEnum.CHARGING_PILE_OPENING_ERROR);
    }

    @Override
    public R<?> closePile(String chargingPileId) throws MqttException {
        String content = "POWER_DISABLE";
        String topic = "CDZ/" + chargingPileId + "/Config";
        MqttMessage msg = new MqttMessage(content.getBytes());
        if (pileRecordService.queryLastChargingRecord(chargingPileId).getDownTime() == null) {
            mqttClient.publish(topic, msg);
            //更新充电结束时间
            ChargingPileRecord chargingPileRecord = pileRecordService.queryLastChargingRecord(chargingPileId);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp downTime = new Timestamp(currentTimeMillis);
            chargingPileRecord.setDownTime(downTime);
            pileRecordService.insertChargingRecord(chargingPileRecord);
            return R.ok("充电桩关闭成功");
        }
        return R.fail(ErrorEnum.CHARGING_PILE_OPENING_ERROR);
    }

    @Override
    public R<?> appointmentTime(String chargingPileId, String appointmentTime) throws MqttException {
        String topic = "CDZ/" + chargingPileId + "/Config";
        MqttMessage msg = new MqttMessage(appointmentTime.getBytes());
        if (pileRecordService.queryLastChargingRecord(chargingPileId).getDownTime() == null) {
            mqttClient.publish(topic, msg);
            return R.ok("充电桩预约成功");
        }
        return R.fail(ErrorEnum.CHARGING_PLIE_APPOINTMENT_ERROR);
    }
    // 充电时充电桩状态
    @Override
    public R<?> state(String chargingPileId) {
        String stage = chargingPileInfoMapper.queryStage(chargingPileId);
        if ((stage == "12")) {
            return R.fail(ErrorEnum.CHARGING_PLIE_ID_NO_CONNECT_ERROR, "充电桩未连接");
        }
        StateForm stateForm = chargingPileInfoMapper.queryChargingPileState(chargingPileId);
        System.out.println(stateForm);
        return R.ok(stateForm);
    }

    //获取充电桩Status  0未插枪  1插枪但是没有任何动作 2插枪有命令下发充电但是未充电（车内预约充电状态/充满自停状态） 3正常充电状态  4离线
    @Override
    public R<?> status(String userOpenId) {
        int status = chargingPileInfoService.getChargingPileStatus(userOpenId).getCode();
        return R.ok(status);
    }

    //根据充电桩编码获取CP信号
    @Override
    public R<?> getStage(String chargingPileId) {
        return R.ok(chargingPileRecordMapper.queryStage(chargingPileId));
    }

    //获取订单状态
    @Override
    public int getOrderStatus(String chargingPileId) {

        Integer orderStatus = chargingPileRecordMapper.queryOrderStatus(chargingPileId);

        if (orderStatus == null) {
            throw new RuntimeException("Order status for Charging Pile ID " + chargingPileId + " is not found.");
        }

        return orderStatus;
    }

}
