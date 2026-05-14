package com.procurement.enums;

public enum OrderStatus {
    PENDING("待到货"),
    RECEIVED("已到货"),
    ACCEPTED("验收通过"),
    REJECTED("验收异常");

    private String desc;

    OrderStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
