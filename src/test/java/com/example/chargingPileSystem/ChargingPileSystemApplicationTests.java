package com.example.chargingPileSystem;

import com.example.chargingPileSystem.domain.ChargingPlieInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

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
        getObjectProperty(new ChargingPlieInfo());
    }

}
