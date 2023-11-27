package com.example.chargingPileSystem.enums;



public enum ErrorEnum {
    USERNAME_NO_EXIST_ERROR(30007, "用户名不存在！"),
    CHARGING_PLIE_ID_NO_EXIST_ERROR(30008, "充电桩不存在！");

    private final int code;
    private final String msg;

    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
