package com.procurement.storage;

import com.procurement.entity.*;
import com.procurement.enums.ApplicationStatus;
import com.procurement.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataStorage {
    public static final Map<String, ProcurementApplication> applications = new ConcurrentHashMap<>();
    public static final Map<String, Supplier> suppliers = new ConcurrentHashMap<>();
    public static final Map<String, Quote> quotes = new ConcurrentHashMap<>();
    public static final Map<String, PurchaseOrder> orders = new ConcurrentHashMap<>();
    public static final Map<String, DepartmentBudget> budgets = new ConcurrentHashMap<>();

    private static final Random random = new Random();

    public static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void initData() {
        initBudgets();
        initSuppliers();
        initSampleApplications();
    }

    private static void initBudgets() {
        DepartmentBudget budget1 = new DepartmentBudget();
        budget1.setId(generateId());
        budget1.setDepartment("技术部");
        budget1.setTotalBudget(new BigDecimal("100000"));
        budget1.setUsedBudget(BigDecimal.ZERO);
        budget1.setCreateTime(LocalDateTime.now());
        budget1.setUpdateTime(LocalDateTime.now());
        budgets.put(budget1.getId(), budget1);

        DepartmentBudget budget2 = new DepartmentBudget();
        budget2.setId(generateId());
        budget2.setDepartment("市场部");
        budget2.setTotalBudget(new BigDecimal("80000"));
        budget2.setUsedBudget(BigDecimal.ZERO);
        budget2.setCreateTime(LocalDateTime.now());
        budget2.setUpdateTime(LocalDateTime.now());
        budgets.put(budget2.getId(), budget2);

        DepartmentBudget budget3 = new DepartmentBudget();
        budget3.setId(generateId());
        budget3.setDepartment("行政部");
        budget3.setTotalBudget(new BigDecimal("50000"));
        budget3.setUsedBudget(BigDecimal.ZERO);
        budget3.setCreateTime(LocalDateTime.now());
        budget3.setUpdateTime(LocalDateTime.now());
        budgets.put(budget3.getId(), budget3);
    }

    private static void initSuppliers() {
        String[] names = {"华为技术", "联想集团", "戴尔中国", "惠普中国", "华硕电脑"};
        String[] contacts = {"张三", "李四", "王五", "赵六", "钱七"};
        String[] phones = {"13800138001", "13800138002", "13800138003", "13800138004", "13800138005"};

        for (int i = 0; i < 5; i++) {
            Supplier supplier = new Supplier();
            supplier.setId(generateId());
            supplier.setName(names[i]);
            supplier.setContact(contacts[i]);
            supplier.setPhone(phones[i]);
            supplier.setScore(new BigDecimal(80 + random.nextInt(20)));
            supplier.setWinCount(0);
            supplier.setCreateTime(LocalDateTime.now());
            suppliers.put(supplier.getId(), supplier);
        }
    }

    private static void initSampleApplications() {
        ProcurementApplication app1 = new ProcurementApplication();
        app1.setId(generateId());
        app1.setApplicant("王小明");
        app1.setDepartment("技术部");
        app1.setProductName("办公电脑");
        app1.setQuantity(10);
        app1.setBudget(new BigDecimal("50000"));
        app1.setPurpose("新员工入职采购");
        app1.setStatus(ApplicationStatus.PENDING);
        app1.setCreateTime(LocalDateTime.now());
        app1.setUpdateTime(LocalDateTime.now());
        applications.put(app1.getId(), app1);

        DepartmentBudget techBudget = budgets.values().stream()
                .filter(b -> b.getDepartment().equals("技术部"))
                .findFirst()
                .orElse(null);
        if (techBudget != null) {
            techBudget.setUsedBudget(app1.getBudget());
        }
    }
}
