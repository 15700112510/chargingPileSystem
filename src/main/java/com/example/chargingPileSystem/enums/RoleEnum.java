package com.example.chargingPileSystem.enums;


public enum RoleEnum {
    SYSTEM(0, "系统管理员"),
    ADMIN(1, "超级管理员"),
    COMMON(2, "普通管理员");

    private int role;
    private String remark;

    // 构造方法
    private RoleEnum(int role, String remark) {
        this.role = role;
        this.remark = remark;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
