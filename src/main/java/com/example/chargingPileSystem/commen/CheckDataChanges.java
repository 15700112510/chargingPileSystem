package com.example.chargingPileSystem.commen;

import com.example.chargingPileSystem.domain.ChargingPlieInfo;

import java.lang.reflect.Field;
import java.util.Map;

public class CheckDataChanges {
    //判断从取出对象中的属性与本地没有的对象是否相同
    public static boolean contrastChargingPile(String imei, ChargingPlieInfo chargingPlieInfo, Map<String, ChargingPlieInfo> lastChargingPlieInfoMap) throws IllegalAccessException {
        ChargingPlieInfo lastChargingPlieInfo = lastChargingPlieInfoMap.get(imei);
        lastChargingPlieInfoMap.replace(imei, lastChargingPlieInfo);
        return getObjectProperty(chargingPlieInfo, lastChargingPlieInfo);
    }

    //对比本地对象和之前对象的属性是否变化
    public static boolean getObjectProperty(Object obj, Object obj2) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        int count = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(obj).equals(field.get(obj2)) && !field.isAnnotationPresent(PropertyIgnore.class)) {
                count++;
                if (field.getType() == int.class) {
                    field.set(obj, -1);
                } else if (field.getType() == String.class) {
                    field.set(obj, "-1");
                }
            } else {
                field.set(obj2, field.get(obj));
            }
        }
        return count == fields.length;
    }
}
