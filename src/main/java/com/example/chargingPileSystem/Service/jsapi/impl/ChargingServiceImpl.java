package com.example.chargingPileSystem.Service.jsapi.impl;

import cn.hutool.core.date.DateUtil;
import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.commen.R;
import com.example.chargingPileSystem.domain.StockUserCharge;
import com.example.chargingPileSystem.domain.UserInfo;
import com.example.chargingPileSystem.enums.ErrorEnum;
import com.example.chargingPileSystem.form.StateForm;
import com.example.chargingPileSystem.mapper.ChargingMapper;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import com.example.chargingPileSystem.mapper.ChargingPileRecordMapper;
import com.example.chargingPileSystem.mapper.UserMapper;
import com.example.chargingPileSystem.util.wechatUtil;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class ChargingServiceImpl implements ChargingService {
    @Resource
    private ChargingMapper chargingMapper;
    @Resource
    private ChargingPileInfoMapper chargingPileInfoMapper;
    @Resource
    private ChargingPileRecordMapper chargingPileRecordMapper;
    @Resource
    private MqttClient mqttClient;
    @Override
    public R<?> openPile(String chargingPileId) throws MqttException {
        String content = "POWER_ENABLE";
        String topic = "CDZ/" + chargingPileId + "/Config";
        MqttMessage msg = new MqttMessage(content.getBytes());
        if (chargingPileInfoMapper.queryStage(chargingPileId).equals("6")) {
            mqttClient.publish(topic, msg);
            return R.ok("充电桩开启成功");
        }
        return R.fail(ErrorEnum.CHARGING_PILE_OPENING_ERROR);
    }

    @Override
    public R<?> closePile(String chargingPileId) throws MqttException {
        String content = "POWER_DISABLE";
        String topic = "CDZ/" + chargingPileId + "/Config";
        MqttMessage msg = new MqttMessage(content.getBytes());
        if (chargingPileRecordMapper.queryLastRecord(chargingPileId).getDownTime() == null) {
            mqttClient.publish(topic, msg);
            return R.ok("充电桩关闭成功");
        }
        return R.fail(ErrorEnum.CHARGING_PILE_OPENING_ERROR);
    }

    @Override
    public R<?> appointmentTime(String chargingPileId, String appointmentTime) throws MqttException {
        String topic = "CDZ/" + chargingPileId + "/Config";
        MqttMessage msg = new MqttMessage(appointmentTime.getBytes());
        if (chargingPileRecordMapper.queryLastRecord(chargingPileId).getDownTime() == null) {
            mqttClient.publish(topic, msg);
            return R.ok("充电桩预约成功");
        }
        return R.fail(ErrorEnum.CHARGING_PLIE_APPOINTMENT_ERROR);
    }

    @Override
    public R<?> state(String chargingPileId) {

        int stage = chargingMapper.queryStage(chargingPileId);
        if (!(stage == 12)) {
            return R.fail(ErrorEnum.CHARGING_PLIE_ID_NO_CONNECT_ERROR,"充电桩未连接");
        }
        StateForm stateForm = chargingMapper.queryChargingPileState(chargingPileId);
        System.out.println(stateForm);
        return R.ok(stateForm);
    }

    //获取充电桩Status  0未插枪  1插枪但是没有任何动作 2插枪有命令下发充电但是未充电（车内预约充电状态/充满自停状态） 3正常充电状态  4离线
    @Override
    public R<?> status(String userOpenId) {
        int status = chargingPileInfoMapper.queryStatus(userOpenId);
        return  R.ok(status);
    }
}
