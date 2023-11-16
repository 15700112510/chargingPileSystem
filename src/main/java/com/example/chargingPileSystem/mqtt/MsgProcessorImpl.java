package com.example.chargingPileSystem.mqtt;

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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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
    @Override
    public void process() {
        try {
            String message = new String(this.mqttMessage.getPayload());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if (this.topic.contains("CDZ")){try {
                persistence(message);
            } catch (Throwable e) {
                e.printStackTrace();
            }}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void persistence(String message){
        ChargingPlieInfo chargingPlieInfo = new ChargingPlieInfo();
        ChargingPlieRecord chargingPlieRecord = new ChargingPlieRecord();
        String[] strings = new String[26];
        String[] split = message.replace("\r", "").split(("\n"));
        for (int i = 0; i < split.length; i++) {
            String name = split[i].substring(split[i].indexOf(":") + 1);
            strings[i] = name.replace("\"", "");;
        }
        String imei = strings[0];
        if (chargingPlieInfoMapper.queryId(imei) == null){
            chargingPlieInfoMapper.insertChargingPile(imei);
        }
        if(split.length == 2) {
            setStatus(imei, "0");
        }else if(split.length == 26){
            String post = strings[4];//开机总时间（秒）1234 //
            String relay = strings[2];//0/1（主闸断开/主闸闭合） //
            String voltage = strings[5].split(",")[3];//电压220V//1
            chargingPlieInfo.setVoltage(voltage);
            String current = strings[6].split(",")[3];//电流11.00A//1
            chargingPlieInfo.setCurrent(current);
            String power = strings[7].split(",")[3]; //功率1.06w//1
            chargingPlieInfo.setPower(power);
            int chargingTime = Integer.parseInt(strings[8]);//单次充电时长（秒）：1234//2
            chargingPlieRecord.setChargingTime(chargingTime);
            String accumulatedElectricEnergy = strings[9];//累计电能16608886.00000krh//1
            chargingPlieInfo.setAccumulatedElectricEnergy(accumulatedElectricEnergy);
            String singleEnergy = strings[10];//2
            chargingPlieRecord.setSingleEnergy(singleEnergy);
            String CP = strings[11];//12:充电桩未连接 9:充电桩连接车体 6:充电桩握手成功，准备充电//1与18字段一样
            String cause = strings[12];
//            1. “CAUSE”：0 正常运行的周期上报
//            2. “CAUSE”：1 异常状态下的周期上报
//            3. “CAUSE”：2 异常动作上报
//            4. “CAUSE”：3 异常解除动作上报
//            5. “CAUSE”：4 主闸动作上报
//            6. “CAUSE”：5 CP握手成功
//            7. “CAUSE”：6 CP握手脱离
//            8. “CAUSE”：7 Config信息回复
            String odr = strings[13];//1,1,23,7,4,61879//2
//            最近一次主闸动作指令来源
//            内容：1/2/3/4/5 (刷卡/4G/预约/BLE/ERROR) ，0/1 (闭闸/合闸) “23”表示23年，“7”表示7月，“4”表示4号，“61879 ”表示当天的时间
//            说明：刷卡，合闸，23年，7月，4号，61879秒
            String[] chargingPileAction = odr.split(",");
            int chargingForm = Integer.parseInt(chargingPileAction[0]);//1/2/3/4/5 (刷卡/4G/预约/BLE/ERROR)
            int gateStatus = Integer.parseInt(chargingPileAction[1]);//0/1 (闭闸/合闸)
            if (gateStatus == 0){
                String upTime = chargingPileAction[3];
            }else if (gateStatus == 1) {
                String downTime = chargingPileAction[3];
            }
            int error = Integer.parseInt(strings[14]);//0//1
//            1. “ERROR”: 0 无故障
//            2. “ERROR”: 1 漏电保护
//            3. “ERROR”: 2 CP故障
//            4. “ERROR”: 3 过压故障
//            5. “ERROR”: 4 欠压故障
//            6. “ERROR”: 5 过流故障
//            7. “ERROR”: 6 接地检测8. “ERROR”: 7 急停按下
//            9. “ERROR”: 8 短路故障
//            10. “ERROR”: 9 过温检测
//            11. “ERROR”: 10 温度芯片故障
//            12. “ERROR”: 11 计量芯片故障
//            13. “ERROR”: 12 漏电自检故障
//            14. “ERROR”: 13 继电器粘连故障
//            15. “ERROR”: 14 输出短路
//            16. “ERROR”: 15 入网失败（信号差
            String usesw = strings[15];//1,1,1,1,1,2,2//1
//            说明：接收内容依次为刷卡（0关闭，1开启，2无此功能），蓝牙（0关闭，1开启，2无此功能），
//            4G（0关闭，1开启，2无此功能），OLED（0关闭，1开启，2无此功能），LED（0关闭，1开启，
//            2无此功能），语音输出（0关闭，1开启，2无此功能），SPEAK_ODR（0关闭，1开启，2无此功
//            能）
            String appointmentTime = strings[16];//1,3600,4200,0,0,0,0,0,0//1
//            2表示重复预约充电 1 表示单次预约充电 0，表示预约充电已关闭
//            3600 表示预约充电开始时间
//            4200 表示预约充电结束时间
//            0,0,0,0,0,0,0 分别表示周一至周日充电预约时间开关
            String bleBound = strings[17];//1 表示蓝牙以绑定 0，表示蓝牙未绑定
            String bleName = strings[19];//蓝牙名称
            String chargingUpTime = strings[22];//2023-10-9 18：32：30 充电开始时间
            int equipmentTemperature = Integer.parseInt(strings[3].substring(0, strings[3].indexOf('.')));//设备温度 30
            String status = strings[24];//1
//            Connect_STA：0 未插枪
//            Connect_STA：1 插枪但是没有任何动作
//            Connect_STA：2 插枪有命令下发充电但是未充电（车内预约充电状态/充满自停状态）
//            Connect_STA：3 正常充电状态
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<ChargingPlieInfo> chargingPlieInfoList = chargingPlieInfoMapper.queryChargingPileList();
        aliveStatus = new HashMap<>(128);
        // 初始化
        for (int i = 1; i <= chargingPlieInfoList.size(); i++) {
            aliveStatus.put(String.valueOf(i),new Object[]{System.currentTimeMillis(), "4"});
        }
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            aliveStatus.forEach((id, obj) -> {
                if (System.currentTimeMillis() - (long) obj[0] > 60000) {
                    if (aliveStatus.get(id)[1] != "4") {
                        // 若60s内还不在线，则标记为离线状态
                        chargingPlieInfoMapper.updateStatus(4,Integer.parseInt(id));
                        aliveStatus.replace(id, new Object[]{obj[0], "4"});
                    }
                }
            });
        }, 1, 2, TimeUnit.SECONDS);
    }
    //设置aliveStatus和更新数据库状态
    public void setStatus(String imei,String status){
        String id = chargingPlieInfoMapper.queryId(imei);
        aliveStatus.replace(id, new Object[]{System.currentTimeMillis(), status});
        chargingPlieInfoMapper.updateStatus(Integer.parseInt(status),Integer.parseInt(id));
    }

}
