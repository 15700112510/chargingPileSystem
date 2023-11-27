package com.example.chargingPileSystem;

import com.example.chargingPileSystem.domain.ChargingPileInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@SpringBootTest
class ChargingPileSystemApplicationTests {
    public void getObjectProperty(Object obj){
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);

            System.out.println(field.getName());
        }
    }

    @Test
    void contextLoads() {
    }

}
