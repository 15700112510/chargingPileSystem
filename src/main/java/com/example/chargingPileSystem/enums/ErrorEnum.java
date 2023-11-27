package com.example.chargingPileSystem.enums;



public enum ErrorEnum {
    USERNAME_NO_EXIST_ERROR(30007, "用户名不存在！"),
    CHARGING_PLIE_ID_NO_EXIST_ERROR(30008, "充电桩不存在！"),

    CHARGING_PILE_OPENING_ERROR(30009,"开启充电失败"),
    CHARGING_PLIE_CLOSING_ERROR(30010, "关闭充电失败"),
    CHARGING_PLIE_APPOINTMENT_ERROR(30011, "预约充电失败");
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
