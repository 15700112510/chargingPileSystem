package com.example.chargingPileSystem.mqtt;

import com.example.chargingPileSystem.Service.jsapi.ChargingService;
import com.example.chargingPileSystem.Service.jsapi.PaymentService;
import com.example.chargingPileSystem.Service.manege.ChargingPileInfoService;
import com.example.chargingPileSystem.constant.ChargingPileRecordConstant;
import com.example.chargingPileSystem.domain.ChargingPileInfo;
import com.example.chargingPileSystem.domain.ChargingPileRecord;
import com.example.chargingPileSystem.domain.PaymentOrder;
import com.example.chargingPileSystem.mapper.ChargingPileInfoMapper;
import com.example.chargingPileSystem.mapper.ChargingPileRecordMapper;
import com.example.chargingPileSystem.mapper.UserMapper;
import com.example.chargingPileSystem.util.CheckDataChanges;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Data
@Component
public class MsgProcessor implements InitializingBean {
    private String topic;
    private MqttMessage mqttMessage;
    private ApplicationEventPublisher publisher;
    @Resource
    private ChargingPileInfoMapper chargingPileInfoMapper;
    @Resource
    private ChargingPileRecordMapper chargingPileRecordMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ChargingService chargingService;
    @Resource
    private ChargingPileInfoService chargingPileInfoService;
    @Resource
    private PaymentService paymentService;

    // 机器号/最近活跃时间毫秒数
    public static Map<String, Object[]> aliveStatus;
    public static Map<String, ChargingPileInfo> lastChargingPlieInfo;

    public void process() {
        try {
            String message = new String(this.mqttMessage.getPayload());
            if (this.topic.contains("CDZ")) {
                try {
                    persistence(message);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void persistence(String message) throws IllegalAccessException, ParseException {
        ChargingPileInfo chargingPileInfo = new ChargingPileInfo();

        String[] strings = new String[30];
        String[] split = message.replace("\r", "").split(("\n"));
        for (int i = 0; i < split.length; i++) {
            String name = split[i].substring(split[i].indexOf(":") + 1);
            strings[i] = name.replace("\"", "");
        }
        String imei = strings[0];
        if (chargingPileInfoMapper.queryId(imei) == null) {
            lastChargingPlieInfo.put(imei, new ChargingPileInfo());
            chargingPileInfoMapper.insertChargingPile(imei);
        }
        if (split.length == 2) {
//            setStatus(imei, "0");
            System.out.println("消息压缩为PING");
        } else if (split.length > 5) {
            //如果订单状态已完成，则返回
            if (chargingService.getOrderStatus(imei) == ChargingPileRecordConstant.ORDER_ACCOMPLISHED){
                return;
            }else {
                //开机总时间（秒）1234
                String post = strings[4];

                //0/1（主闸断开/主闸闭合）
                String relay = strings[2];

                //电压220V//1
                String voltage = strings[5].split(",")[3];

                //电流11.00A//1
                String current = strings[6].split(",")[3];

                //功率1.06w//1
                String power = strings[7].split(",")[3];

                //单次充电时长（秒）：1234//2
                int chargingTime = Integer.parseInt(strings[8]);

                //累计电能16608886.00000krh//1
                String accumulatedElectricEnergy = strings[9];

                //单次电能16608886.00000krh//2
                String singleEnergy = strings[10];

                //12:充电桩未连接 9:充电桩连接车体 6:充电桩握手成功，准备充电//1与18字段一样
                String stage = strings[11];

                //1. “CAUSE”：0 正常运行的周期上报 2. “CAUSE”：1 异常状态下的周期上报 3. “CAUSE”：2 异常动作上报 4. “CAUSE”：3 异常解除动作上报 5. “CAUSE”：4 主闸动作上报 6. “CAUSE”：5 CP握手成功 7. “CAUSE”：6 CP握手脱离 8. “CAUSE”：7 Config信息回复
                String cause = strings[12];

                //最近一次主闸动作指令来源:1,1,23,7,4,61879//2//说明：刷卡，合闸，23年，，47月号，61879秒
                String odr = strings[13];

                String[] chargingPileAction = odr.split(",");
                //1/2/3/4/5 (刷卡/4G/预约/BLE/ERROR)
                int chargingForm = Integer.parseInt(chargingPileAction[0]);
                //0/1 (闭闸/合闸)
                int gateStatus = Integer.parseInt(chargingPileAction[1]);
                String dateString = "20" + chargingPileAction[2] + "-" + chargingPileAction[3] + "-" + chargingPileAction[4] + " " + secondsConversion(chargingPileAction[5]);

                int error = Integer.parseInt(strings[14]);

                //说明：接收内容依次为刷卡(0关闭,1开启,2无此功能),蓝牙,4G,OLED,LED,语音输出,SPEAK_ODR//1
                String usesw = strings[15];

                //2表示重复预约充电 1表示单次预约充电 0表示预约充电已关闭,3600表示预约充电开始时间,4200表示预约充电结束时间,0,0,0,0,0,0,0分别表示周一至周日充电预约时间开关//1,3600,4200,0,0,0,0,0,0//1
                String appointmentTime = strings[16];

                //1 表示蓝牙以绑定 0，表示蓝牙未绑定
                String bleBound = strings[17];

                //蓝牙名称
                String bleName = strings[19];

                //2023-10-9 18：32：30 充电开始时间
                String chargingUpTime = strings[23];

                //设备温度 30
                int equipmentTemperature = Integer.parseInt(strings[3].substring(0, strings[3].indexOf('.')));

                //0未插枪,1插枪但是没有任何动作,2插枪有命令下发充电但是未充电(车内预约充电状态/充满自停状态),3正常充电状态
                String status = strings[25];

                //赋值chargingPlieInfo
                chargingPileInfo.setChargingPileId(imei);
                chargingPileInfo.setId(Integer.parseInt(chargingPileInfoMapper.queryId(imei)));
                chargingPileInfo.setVoltage(voltage);
                chargingPileInfo.setCurrent(current);
                chargingPileInfo.setPower(power);
                chargingPileInfo.setAccumulatedElectricEnergy(accumulatedElectricEnergy);
                chargingPileInfo.setStage(stage);
                chargingPileInfo.setError(error);
                chargingPileInfo.setAppointmentTime(appointmentTime);
                chargingPileInfo.setEquipmentTemperature(equipmentTemperature);
                chargingPileInfo.setStatus(Integer.parseInt(status));
                chargingPileInfo.setBleName(bleName);

                //查找是否存在充电订单
                ChargingPileRecord chargingPileRecord = chargingPileRecordMapper.queryLastRecord(imei);
                //赋值chargingPlieRecord
                chargingPileRecord.setChargingTime(chargingTime);
                chargingPileRecord.setSingleEnergy(singleEnergy);
                chargingPileRecord.setStage(stage);
                chargingPileRecord.setChargingForm(chargingForm);
                chargingPileRecord.setGateStatus(gateStatus);

                if (!odr.equals("0,0,23,1,1,0") && !chargingUpTime.equals("0-0-0 0:0:0")) {
                    chargingPileRecord.setUpTime(this.getTimestamp(chargingUpTime));
                    if (gateStatus == 0 && !this.getTimestamp(chargingUpTime).equals(this.getTimestamp(dateString))) {
                        chargingPileRecord.setDownTime(this.getTimestamp(dateString));
                        chargingPileRecord.setOrderStatus(ChargingPileRecordConstant.ORDER_ACCOMPLISHED);
                    }
                    //数据库更新chargingPlieInfo
                    if (contrastChargingPile(imei, chargingPileInfo)) {
                        chargingPileInfoMapper.updateChargingPile(chargingPileInfo);
                    }
                    contrastChargingPileRecord(imei, chargingPileRecord);
                }
            }
        }
    }

    //判断是否需要插入新的充电记录
    public void contrastChargingPileRecord(String imei, ChargingPileRecord chargingPileRecord) throws IllegalAccessException {
        //如果拔枪了，停止充电，完成订单
        if (chargingPileRecord.getGateStatus() == 0) {
            //更新停止充电时间
            long currentTimeMillis  = System.currentTimeMillis();
            Timestamp downTime = new Timestamp(currentTimeMillis);
            chargingPileRecord.setDownTime(downTime);
            remainingRefund(chargingPileRecord.getChargingPileId());
            chargingPileRecord.setOrderStatus(ChargingPileRecordConstant.ORDER_ACCOMPLISHED);
        } else {
            double singleEnergy = Double.parseDouble(chargingPileRecord.getSingleEnergy().substring(0, chargingPileRecord.getSingleEnergy().length() - 3));
            chargingPileRecord.setExpectEnergy("10");
            double expectEnergy = Double.parseDouble(chargingPileRecord.getExpectEnergy());

            if (singleEnergy >= expectEnergy) {
                //停止充电
                try {
                    chargingService.closePile(chargingPileRecord.getChargingPileId());
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
                //更新停止充电时间
                long currentTimeMillis  = System.currentTimeMillis();
                Timestamp downTime = new Timestamp(currentTimeMillis);
                chargingPileRecord.setDownTime(downTime);
                chargingPileRecord.setOrderStatus(ChargingPileRecordConstant.ORDER_ACCOMPLISHED);
                //更新充电记录
                chargingPileRecordMapper.updateChargingPileRecord(chargingPileRecord);
            }else {
                chargingPileRecordMapper.updateChargingPileRecord(chargingPileRecord);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<ChargingPileInfo> chargingPileInfoList = chargingPileInfoMapper.queryChargingPileList();
        aliveStatus = new HashMap<>(128);
        lastChargingPlieInfo = new HashMap<>(128);

        // 初始化
        for (int i = 1; i <= chargingPileInfoList.size(); i++) {
            aliveStatus.put(String.valueOf(i), new Object[]{System.currentTimeMillis(), "4"});
            lastChargingPlieInfo.put(chargingPileInfoList.get(i - 1).getChargingPileId(), chargingPileInfoList.get(i - 1));
        }
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            aliveStatus.forEach((id, obj) -> {
                if (System.currentTimeMillis() - (long) obj[0] > 60000) {
                    if (aliveStatus.get(id)[1] != "4") {
                        // 若60s内还不在线，则标记为离线状态
                        chargingPileInfoMapper.updateStatus(4,"12", Integer.parseInt(id));
                        aliveStatus.replace(id, new Object[]{obj[0], "4"});
                    }
                }
            });
        }, 1, 2, TimeUnit.SECONDS);
    }

    //设置aliveStatus和更新数据库状态
    public void setStatus(String imei,String stage , String status) {
        String id = chargingPileInfoMapper.queryId(imei);
        aliveStatus.replace(id, new Object[]{System.currentTimeMillis(), status});
        chargingPileInfoMapper.updateStatus(Integer.parseInt(status),stage ,Integer.parseInt(id));
    }

    //将“yyyy-mm-dd HH:mm:ss”字符串转化为Timestamp类型”
    public Timestamp getTimestamp(String dateString) throws ParseException {
        String pattern = "yyyy-M-d H:m:s";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date dateTime = sdf.parse(dateString);
        return new Timestamp(dateTime.getTime());
    }

    //根据秒数算出时分秒
    public String secondsConversion(String totalSeconds) {
        int seconds = Integer.parseInt(totalSeconds);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        int hour = minutes / 60;
        int remainingMinutes = minutes % 60;
        return hour + ":" + remainingMinutes + ":" + remainingSeconds;
    }

    //判断从取出对象中的属性与本地没有的对象是否相同
    public boolean contrastChargingPile(String imei, ChargingPileInfo chargingPileInfo) throws IllegalAccessException {
        ChargingPileInfo lastChargingPileInfo = this.lastChargingPlieInfo.get(imei);
        boolean result = CheckDataChanges.getObjectProperty(chargingPileInfo, lastChargingPileInfo);
        this.lastChargingPlieInfo.replace(imei, lastChargingPileInfo);
        return result;
    }

    public void remainingRefund(String chargingPileId) throws IllegalAccessException {
        ChargingPileRecord chargingPileRecord = chargingPileRecordMapper.queryLastRecord(chargingPileId);
        //计算剩余时间退款
        int singleEnergy = Integer.parseInt(chargingPileRecord.getSingleEnergy());
        int expectEnergy = Integer.parseInt(chargingPileRecord.getExpectEnergy());
        int surplusEnergy = expectEnergy - singleEnergy;
        int price = chargingPileInfoService.getChargingPrice(chargingPileRecord.getChargingPileId());
        int refundAmount = surplusEnergy / price;
        //根据当前充电桩编码查找最近支付订单
        PaymentOrder paymentOrder = paymentService.queryLastRecord(chargingPileRecord.getChargingPileId());
        try {
            paymentService.redRefundPay(paymentOrder, refundAmount);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        }
    }
}

































