package com.procurement.entity;

import com.procurement.enums.ApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProcurementApplication {
    private String id;
    private String applicant;
    private String department;
    private String productName;
    private Integer quantity;
    private BigDecimal budget;
    private String purpose;
    private ApplicationStatus status;
    private String recommendSupplierId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String approveRemark;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getApplicant() { return applicant; }
    public void setApplicant(String applicant) { this.applicant = applicant; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public String getRecommendSupplierId() { return recommendSupplierId; }
    public void setRecommendSupplierId(String recommendSupplierId) { this.recommendSupplierId = recommendSupplierId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getApproveRemark() { return approveRemark; }
    public void setApproveRemark(String approveRemark) { this.approveRemark = approveRemark; }
}
