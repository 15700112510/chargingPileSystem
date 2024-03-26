package com.example.chargingPileSystem.exception;

//自定义Token过期异常类
public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message) {
        super(message);
    }
}
