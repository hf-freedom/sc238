package com.procurement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Supplier {
    private String id;
    private String name;
    private String contact;
    private String phone;
    private BigDecimal score;
    private Integer winCount;
    private LocalDateTime createTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    public Integer getWinCount() { return winCount; }
    public void setWinCount(Integer winCount) { this.winCount = winCount; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
