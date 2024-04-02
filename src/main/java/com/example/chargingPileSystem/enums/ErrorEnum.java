package com.example.chargingPileSystem.enums;



public enum ErrorEnum {


    PASSWORD_EMPTY_ERROR(30001, "密码为空！"),
    NAME_EMPTY_ERROR(30002, "姓名为空！"),
    ROLE_EMPTY_ERROR(30003, "角色类型为空！"),
    Phone_EMPTY_ERROR(30004, "手机号为空！"),
    USERNAME_EMPTY_ERROR(30005, "用户名为空！"),
    NAME_EXIST_ERROR(30006, "姓名已经注册！"),
    USERNAME_NO_EXIST_ERROR(30007, "用户名不存在！"),
    PASSWORD_NO_MATCH_ERROR(30008, "密码不匹配！"),
    USER_ID_EMPTY_ERROR(30009, "用户ID为空！"),
    NAME_NO_EXIST_ERROR(30010, "姓名不存在！"),
    CHARGING_PLIE_ID_NO_EXIST_ERROR(300011, "充电桩不存在！"),
    CHARGING_PILE_OPENING_ERROR(300012,"开启充电失败"),
    CHARGING_PLIE_CLOSING_ERROR(30013, "关闭充电失败"),
    CHARGING_PLIE_APPOINTMENT_ERROR(30014, "预约充电失败"),
    CHARGING_PLIE_ID_NO_CONNECT_ERROR(30015,"充电桩未连接"),
    CHARGING_PLIE_ID_EMPTY_ERROR(30016,"充电桩未绑定"),
    USER_OPEN_ID_EMPTY_ERROR(30017,"充电桩未绑定"),
    MISSING_RETURN_PARAMETER(30018,"缺少返回参数")
    ;



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
