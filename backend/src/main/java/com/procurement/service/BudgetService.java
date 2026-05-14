package com.procurement.service;

import com.procurement.entity.DepartmentBudget;
import com.procurement.storage.DataStorage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {

    public List<DepartmentBudget> getAllBudgets() {
        return new ArrayList<>(DataStorage.budgets.values());
    }

    public DepartmentBudget getByDepartment(String department) {
        return DataStorage.budgets.values().stream()
                .filter(b -> b.getDepartment().equals(department))
                .findFirst()
                .orElse(null);
    }

    public boolean occupyBudget(String department, BigDecimal amount) {
        DepartmentBudget budget = getByDepartment(department);
        if (budget == null) {
            return false;
        }
        if (budget.getAvailableBudget().compareTo(amount) < 0) {
            return false;
        }
        budget.setUsedBudget(budget.getUsedBudget().add(amount));
        return true;
    }

    public boolean releaseBudget(String department, BigDecimal amount) {
        DepartmentBudget budget = getByDepartment(department);
        if (budget == null) {
            return false;
        }
        if (budget.getUsedBudget().compareTo(amount) >= 0) {
            budget.setUsedBudget(budget.getUsedBudget().subtract(amount));
            return true;
        }
        return false;
    }
}
