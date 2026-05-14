package com.procurement.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class StatisticsVO {
    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalBudgetUsed;
    private Integer totalApplications;
    private Integer totalOrders;
    private Integer abnormalOrders;
    private BigDecimal abnormalRate;
    private List<Map<String, Object>> supplierWinStats;

    public BigDecimal getTotalPurchaseAmount() { return totalPurchaseAmount; }
    public void setTotalPurchaseAmount(BigDecimal totalPurchaseAmount) { this.totalPurchaseAmount = totalPurchaseAmount; }
    public BigDecimal getTotalBudgetUsed() { return totalBudgetUsed; }
    public void setTotalBudgetUsed(BigDecimal totalBudgetUsed) { this.totalBudgetUsed = totalBudgetUsed; }
    public Integer getTotalApplications() { return totalApplications; }
    public void setTotalApplications(Integer totalApplications) { this.totalApplications = totalApplications; }
    public Integer getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }
    public Integer getAbnormalOrders() { return abnormalOrders; }
    public void setAbnormalOrders(Integer abnormalOrders) { this.abnormalOrders = abnormalOrders; }
    public BigDecimal getAbnormalRate() { return abnormalRate; }
    public void setAbnormalRate(BigDecimal abnormalRate) { this.abnormalRate = abnormalRate; }
    public List<Map<String, Object>> getSupplierWinStats() { return supplierWinStats; }
    public void setSupplierWinStats(List<Map<String, Object>> supplierWinStats) { this.supplierWinStats = supplierWinStats; }
}
