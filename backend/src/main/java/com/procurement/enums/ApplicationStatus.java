package com.procurement.enums;

public enum ApplicationStatus {
    PENDING("待审批"),
    APPROVED("已通过"),
    REJECTED("已驳回"),
    ORDERED("已下单");

    private String desc;

    ApplicationStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
