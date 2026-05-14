package com.procurement.service;

import com.procurement.entity.ProcurementApplication;
import com.procurement.entity.Quote;
import com.procurement.entity.Supplier;
import com.procurement.enums.ApplicationStatus;
import com.procurement.storage.DataStorage;
import com.procurement.vo.RecommendResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private BudgetService budgetService;

    public List<ProcurementApplication> getAllApplications() {
        return new ArrayList<>(DataStorage.applications.values());
    }

    public ProcurementApplication getById(String id) {
        return DataStorage.applications.get(id);
    }

    public ProcurementApplication createApplication(ProcurementApplication app) {
        if (!budgetService.occupyBudget(app.getDepartment(), app.getBudget())) {
            throw new RuntimeException("部门预算不足");
        }
        app.setId(DataStorage.generateId());
        app.setStatus(ApplicationStatus.PENDING);
        app.setCreateTime(LocalDateTime.now());
        app.setUpdateTime(LocalDateTime.now());
        DataStorage.applications.put(app.getId(), app);
        return app;
    }

    public ProcurementApplication approve(String id, String remark) {
        ProcurementApplication app = DataStorage.applications.get(id);
        if (app == null) {
            throw new RuntimeException("申请不存在");
        }
        app.setStatus(ApplicationStatus.APPROVED);
        app.setApproveRemark(remark);
        app.setUpdateTime(LocalDateTime.now());
        return app;
    }

    public ProcurementApplication reject(String id, String remark) {
        ProcurementApplication app = DataStorage.applications.get(id);
        if (app == null) {
            throw new RuntimeException("申请不存在");
        }
        app.setStatus(ApplicationStatus.REJECTED);
        app.setApproveRemark(remark);
        app.setUpdateTime(LocalDateTime.now());
        budgetService.releaseBudget(app.getDepartment(), app.getBudget());
        return app;
    }

    public String recommendSupplier(String applicationId) {
        List<Quote> quoteList = DataStorage.quotes.values().stream()
                .filter(q -> q.getApplicationId().equals(applicationId))
                .collect(Collectors.toList());

        if (quoteList.isEmpty()) {
            return null;
        }

        String bestSupplierId = null;
        BigDecimal bestScore = BigDecimal.valueOf(-1);

        for (Quote quote : quoteList) {
            Supplier supplier = DataStorage.suppliers.get(quote.getSupplierId());
            if (supplier == null) continue;

            BigDecimal priceScore = BigDecimal.valueOf(100).subtract(
                    quote.getPrice().divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP)
            ).max(BigDecimal.ZERO);

            BigDecimal deliveryScore = BigDecimal.valueOf(100).subtract(
                    BigDecimal.valueOf(quote.getDeliveryDays() * 5)
            ).max(BigDecimal.ZERO);

            BigDecimal totalScore = priceScore.multiply(BigDecimal.valueOf(0.5))
                    .add(deliveryScore.multiply(BigDecimal.valueOf(0.3)))
                    .add(supplier.getScore().multiply(BigDecimal.valueOf(0.2)));

            if (totalScore.compareTo(bestScore) > 0) {
                bestScore = totalScore;
                bestSupplierId = quote.getSupplierId();
            }
        }

        ProcurementApplication app = DataStorage.applications.get(applicationId);
        if (app != null) {
            app.setRecommendSupplierId(bestSupplierId);
        }

        return bestSupplierId;
    }

    public RecommendResultVO getRecommendationDetail(String applicationId) {
        List<Quote> quoteList = DataStorage.quotes.values().stream()
                .filter(q -> q.getApplicationId().equals(applicationId))
                .collect(Collectors.toList());

        RecommendResultVO result = new RecommendResultVO();
        if (quoteList.isEmpty()) {
            result.setSupplierScores(Collections.emptyList());
            return result;
        }

        List<RecommendResultVO.SupplierQuoteScoreVO> scoreList = new ArrayList<>();
        String bestSupplierId = null;
        BigDecimal bestScore = BigDecimal.valueOf(-1);
        String bestSupplierName = null;

        for (Quote quote : quoteList) {
            Supplier supplier = DataStorage.suppliers.get(quote.getSupplierId());
            if (supplier == null) continue;

            RecommendResultVO.SupplierQuoteScoreVO vo = new RecommendResultVO.SupplierQuoteScoreVO();
            vo.setSupplierId(supplier.getId());
            vo.setSupplierName(supplier.getName());
            vo.setPrice(quote.getPrice());
            vo.setDeliveryDays(quote.getDeliveryDays());
            vo.setSupplierScore(supplier.getScore());

            BigDecimal priceScore = BigDecimal.valueOf(100).subtract(
                    quote.getPrice().divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP)
            ).max(BigDecimal.ZERO);
            vo.setPriceScore(priceScore);

            BigDecimal deliveryScore = BigDecimal.valueOf(100).subtract(
                    BigDecimal.valueOf(quote.getDeliveryDays() * 5)
            ).max(BigDecimal.ZERO);
            vo.setDeliveryScore(deliveryScore);

            BigDecimal totalScore = priceScore.multiply(BigDecimal.valueOf(0.5))
                    .add(deliveryScore.multiply(BigDecimal.valueOf(0.3)))
                    .add(supplier.getScore().multiply(BigDecimal.valueOf(0.2)));
            vo.setTotalScore(totalScore);

            scoreList.add(vo);

            if (totalScore.compareTo(bestScore) > 0) {
                bestScore = totalScore;
                bestSupplierId = supplier.getId();
                bestSupplierName = supplier.getName();
            }
        }

        scoreList.sort((a, b) -> b.getTotalScore().compareTo(a.getTotalScore()));

        result.setBestSupplierId(bestSupplierId);
        result.setBestSupplierName(bestSupplierName);
        result.setBestTotalScore(bestScore);
        result.setSupplierScores(scoreList);

        ProcurementApplication app = DataStorage.applications.get(applicationId);
        if (app != null) {
            app.setRecommendSupplierId(bestSupplierId);
        }

        return result;
    }
}
