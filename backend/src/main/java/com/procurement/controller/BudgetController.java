package com.procurement.controller;

import com.procurement.common.Result;
import com.procurement.entity.DepartmentBudget;
import com.procurement.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budget")
@CrossOrigin
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/list")
    public Result<List<DepartmentBudget>> list() {
        return Result.success(budgetService.getAllBudgets());
    }

    @GetMapping("/{department}")
    public Result<DepartmentBudget> getByDepartment(@PathVariable String department) {
        return Result.success(budgetService.getByDepartment(department));
    }
}
