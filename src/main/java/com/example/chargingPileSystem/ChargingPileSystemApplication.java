package com.example.chargingPileSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ChargingPileSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChargingPileSystemApplication.class, args);
    }

}
