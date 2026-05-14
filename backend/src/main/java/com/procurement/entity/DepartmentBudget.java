package com.procurement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DepartmentBudget {
    private String id;
    private String department;
    private BigDecimal totalBudget;
    private BigDecimal usedBudget;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public BigDecimal getAvailableBudget() {
        return totalBudget.subtract(usedBudget);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public BigDecimal getTotalBudget() { return totalBudget; }
    public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }
    public BigDecimal getUsedBudget() { return usedBudget; }
    public void setUsedBudget(BigDecimal usedBudget) { this.usedBudget = usedBudget; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
