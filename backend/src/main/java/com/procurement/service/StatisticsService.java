package com.procurement.service;

import com.procurement.entity.DepartmentBudget;
import com.procurement.entity.PurchaseOrder;
import com.procurement.entity.Supplier;
import com.procurement.enums.OrderStatus;
import com.procurement.storage.DataStorage;
import com.procurement.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private BudgetService budgetService;

    public StatisticsVO getStatistics() {
        StatisticsVO vo = new StatisticsVO();

        List<PurchaseOrder> orders = new ArrayList<>(DataStorage.orders.values());

        BigDecimal totalAmount = orders.stream()
                .map(PurchaseOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotalPurchaseAmount(totalAmount);

        List<DepartmentBudget> budgets = budgetService.getAllBudgets();
        BigDecimal totalBudgetUsed = budgets.stream()
                .map(DepartmentBudget::getUsedBudget)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTotalBudgetUsed(totalBudgetUsed);

        vo.setTotalApplications(DataStorage.applications.size());
        vo.setTotalOrders(orders.size());

        long abnormalCount = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.REJECTED)
                .count();
        vo.setAbnormalOrders((int) abnormalCount);

        if (!orders.isEmpty()) {
            vo.setAbnormalRate(BigDecimal.valueOf(abnormalCount * 100.0 / orders.size()).setScale(2, RoundingMode.HALF_UP));
        } else {
            vo.setAbnormalRate(BigDecimal.ZERO);
        }

        List<Map<String, Object>> supplierStats = DataStorage.suppliers.values().stream()
                .map(s -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("supplierId", s.getId());
                    map.put("supplierName", s.getName());
                    map.put("winCount", s.getWinCount());
                    map.put("score", s.getScore());
                    return map;
                })
                .sorted((a, b) -> (Integer) b.get("winCount") - (Integer) a.get("winCount"))
                .collect(Collectors.toList());
        vo.setSupplierWinStats(supplierStats);

        return vo;
    }
}
