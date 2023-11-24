package com.example.chargingPileSystem.mqtt;

import com.example.chargingPileSystem.commen.PropertyIgnore;
import com.example.chargingPileSystem.domain.ChargingPlieInfo;
import com.example.chargingPileSystem.domain.ChargingPlieRecord;
import com.example.chargingPileSystem.mapper.ChargingPlieInfoMapper;
import javafx.fxml.Initializable;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.net.URL;
import java.rmi.MarshalledObject;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Data
public class MsgProcessorImpl implements MsgProcessor, InitializingBean {
    private String topic;
    private MqttMessage mqttMessage;
    private ApplicationEventPublisher publisher;
    @Resource
    private MqttClient mqttClient;
    @Resource
    private ChargingPlieInfoMapper chargingPlieInfoMapper;

    // 机器号/最近活跃时间毫秒数
    public static Map<String, Object[]> aliveStatus;
    public static Map<String, ChargingPlieInfo> lastChargingPlieInfo;

    @Override
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
        ChargingPlieInfo chargingPlieInfo = new ChargingPlieInfo();
        ChargingPlieRecord chargingPlieRecord = new ChargingPlieRecord();

        String[] strings = new String[26];
        String[] split = message.replace("\r", "").split(("\n"));
        for (int i = 0; i < split.length; i++) {
            String name = split[i].substring(split[i].indexOf(":") + 1);
            strings[i] = name.replace("\"", "");
        }
        String imei = strings[0];
        if (chargingPlieInfoMapper.queryId(imei) == null) {
            lastChargingPlieInfo.put(imei, new ChargingPlieInfo());
            chargingPlieInfoMapper.insertChargingPile(imei);
        }
        if (split.length == 2) {
            setStatus(imei, "0");
        } else if (split.length == 26) {
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

            //最近一次主闸动作指令来源:1,1,23,7,4,61879//2//说明：刷卡，合闸，23年，7月，4号，61879秒
            String odr = strings[13];
            String[] chargingPileAction = odr.split(",");
            //1/2/3/4/5 (刷卡/4G/预约/BLE/ERROR)
            int chargingForm = Integer.parseInt(chargingPileAction[0]);
            //0/1 (闭闸/合闸)
            int gateStatus = Integer.parseInt(chargingPileAction[1]);

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
            String chargingUpTime = strings[22];

            //设备温度 30
            int equipmentTemperature = Integer.parseInt(strings[3].substring(0, strings[3].indexOf('.')));

            //0未插枪,1插枪但是没有任何动作,2插枪有命令下发充电但是未充电(车内预约充电状态/充满自停状态),3正常充电状态//1
            String status = strings[24];

            //赋值chargingPlieInfo
            chargingPlieInfo.setChargingPileId(imei);
            chargingPlieInfo.setId(Integer.parseInt(chargingPlieInfoMapper.queryId(imei)));
            chargingPlieInfo.setVoltage(voltage);
            chargingPlieInfo.setCurrent(current);
            chargingPlieInfo.setPower(power);
            chargingPlieInfo.setAccumulatedElectricEnergy(accumulatedElectricEnergy);
            chargingPlieInfo.setAppointmentTime(appointmentTime);
            chargingPlieInfo.setBleName(bleName);
            chargingPlieInfo.setEquipmentTemperature(equipmentTemperature);
            chargingPlieInfo.setStatus(Integer.parseInt(status));

            //赋值chargingPlieRecord
            chargingPlieRecord.setChargingPileId(imei);
            chargingPlieRecord.setChargingTime(chargingTime);
            chargingPlieRecord.setSingleEnergy(singleEnergy);
            chargingPlieRecord.setStage(stage);
            chargingPlieRecord.setChargingForm(chargingForm);
            chargingPlieRecord.setGateStatus(gateStatus);
            if (gateStatus == 0) {
                chargingPlieRecord.setUpTime(this.getTimestamp(chargingPileAction[3]));
            } else if (gateStatus == 1) {
                chargingPlieRecord.setDownTime(this.getTimestamp(chargingPileAction[3]));
            }

            //数据库更新chargingPlieInfo
            if (contrastChargingPile(imei, chargingPlieInfo)) {
                chargingPlieInfoMapper.updateChargingPile(chargingPlieInfo);
            }

        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<ChargingPlieInfo> chargingPlieInfoList = chargingPlieInfoMapper.queryChargingPileList();
        aliveStatus = new HashMap<>(128);
        lastChargingPlieInfo = new HashMap<>(128);

        // 初始化
        for (int i = 1; i <= chargingPlieInfoList.size(); i++) {
            aliveStatus.put(String.valueOf(i), new Object[]{System.currentTimeMillis(), "4"});
            lastChargingPlieInfo.put(chargingPlieInfoList.get(i-1).getChargingPileId(), chargingPlieInfoList.get(i-1));
        }
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            aliveStatus.forEach((id, obj) -> {
                if (System.currentTimeMillis() - (long) obj[0] > 60000) {
                    if (aliveStatus.get(id)[1] != "4") {
                        // 若60s内还不在线，则标记为离线状态
                        chargingPlieInfoMapper.updateStatus(4, Integer.parseInt(id));
                        aliveStatus.replace(id, new Object[]{obj[0], "4"});
                    }
                }
            });
        }, 1, 2, TimeUnit.SECONDS);
    }

    //设置aliveStatus和更新数据库状态
    public void setStatus(String imei, String status) {
        String id = chargingPlieInfoMapper.queryId(imei);
        aliveStatus.replace(id, new Object[]{System.currentTimeMillis(), status});
        chargingPlieInfoMapper.updateStatus(Integer.parseInt(status), Integer.parseInt(id));
    }

    public boolean contrastChargingPile(String imei, ChargingPlieInfo chargingPlieInfo) throws IllegalAccessException {
        ChargingPlieInfo lastChargingPlieInfo = this.lastChargingPlieInfo.get(imei);
        this.lastChargingPlieInfo.replace(imei, lastChargingPlieInfo);
        return this.getObjectProperty(chargingPlieInfo, lastChargingPlieInfo);
    }

    public boolean getObjectProperty(Object obj, Object obj2) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        int count = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(obj).equals(field.get(obj2)) && !field.isAnnotationPresent(PropertyIgnore.class)) {
                count++;
                if (field.getType() == int.class) {
                    field.set(obj, -1);
                } else if (field.getType() == String.class ) {
                    field.set(obj, "-1");
                }
            } else {
                field.set(obj2, field.get(obj));
            }
        }
        return count==fields.length;
    }
    public Timestamp getTimestamp(String dateString) throws ParseException {
        //定义日期格式
        String pattern = "yyyy-MM-dd HH:mm:ss";
//创建SimpleDateFormat类对象
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//将String类型转换为datetime类型
        Date dateTime = sdf.parse(dateString);
        return new Timestamp(dateTime.getTime());
    }
}
