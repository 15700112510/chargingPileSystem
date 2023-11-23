package com.example.chargingPileSystem.configuration;

import com.example.chargingPileSystem.commen.BeanFactoryWrapper;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanFactoryConfiguration {
    @Bean
    public BeanFactoryWrapper beanFactoryWrapper(DefaultListableBeanFactory defaultListableBeanFactory) {
        return new BeanFactoryWrapper(defaultListableBeanFactory);
    }
}
