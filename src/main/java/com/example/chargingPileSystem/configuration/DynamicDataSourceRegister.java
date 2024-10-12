package com.example.chargingPileSystem.configuration;

import com.example.chargingPileSystem.domain.UserInfo;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 创建数据源 Bean 定义
        RootBeanDefinition dataSourceBeanDefinition = new RootBeanDefinition(UserInfo.class);
        // 注册数据源 Bean
        registry.registerBeanDefinition("myDataSource", dataSourceBeanDefinition);
    }
}
