package com.procurement.vo;

import java.math.BigDecimal;
import java.util.List;

public class RecommendResultVO {
    private String bestSupplierId;
    private String bestSupplierName;
    private BigDecimal bestTotalScore;
    private List<SupplierQuoteScoreVO> supplierScores;

    public String getBestSupplierId() { return bestSupplierId; }
    public void setBestSupplierId(String bestSupplierId) { this.bestSupplierId = bestSupplierId; }
    public String getBestSupplierName() { return bestSupplierName; }
    public void setBestSupplierName(String bestSupplierName) { this.bestSupplierName = bestSupplierName; }
    public BigDecimal getBestTotalScore() { return bestTotalScore; }
    public void setBestTotalScore(BigDecimal bestTotalScore) { this.bestTotalScore = bestTotalScore; }
    public List<SupplierQuoteScoreVO> getSupplierScores() { return supplierScores; }
    public void setSupplierScores(List<SupplierQuoteScoreVO> supplierScores) { this.supplierScores = supplierScores; }

    public static class SupplierQuoteScoreVO {
        private String supplierId;
        private String supplierName;
        private BigDecimal price;
        private Integer deliveryDays;
        private BigDecimal supplierScore;
        private BigDecimal priceScore;
        private BigDecimal deliveryScore;
        private BigDecimal totalScore;

        public String getSupplierId() { return supplierId; }
        public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Integer getDeliveryDays() { return deliveryDays; }
        public void setDeliveryDays(Integer deliveryDays) { this.deliveryDays = deliveryDays; }
        public BigDecimal getSupplierScore() { return supplierScore; }
        public void setSupplierScore(BigDecimal supplierScore) { this.supplierScore = supplierScore; }
        public BigDecimal getPriceScore() { return priceScore; }
        public void setPriceScore(BigDecimal priceScore) { this.priceScore = priceScore; }
        public BigDecimal getDeliveryScore() { return deliveryScore; }
        public void setDeliveryScore(BigDecimal deliveryScore) { this.deliveryScore = deliveryScore; }
        public BigDecimal getTotalScore() { return totalScore; }
        public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }
    }
}
