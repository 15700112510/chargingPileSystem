package com.example.chargingPileSystem.annotation;

public @interface AuthRequire {
    // 是否必须登录
    boolean required() default true;
}
