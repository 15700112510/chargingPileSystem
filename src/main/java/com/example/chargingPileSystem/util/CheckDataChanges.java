package com.example.chargingPileSystem.util;

import com.example.chargingPileSystem.commen.PropertyIgnore;

import java.lang.reflect.Field;
import java.sql.Timestamp;

public class CheckDataChanges {
    //对比本地对象和之前对象的属性是否变化
    public static boolean getObjectProperty(Object obj, Object obj2) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        int count = 0;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(PropertyIgnore.class)) {
                count++;
            } else if (field.get(obj) == null || field.get(obj) == "") {
                if (field.get(obj2) == null || field.get(obj2) == "") {
                    count++;
                } else
                    field.set(obj2, field.get(obj));
            } else if (field.get(obj).equals(field.get(obj2)) && !field.isAnnotationPresent(PropertyIgnore.class)) {
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
        return count != fields.length;
    }
}
