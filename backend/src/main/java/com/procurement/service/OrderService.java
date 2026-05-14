package com.procurement.service;

import com.procurement.entity.ProcurementApplication;
import com.procurement.entity.PurchaseOrder;
import com.procurement.entity.Quote;
import com.procurement.entity.Supplier;
import com.procurement.enums.ApplicationStatus;
import com.procurement.enums.OrderStatus;
import com.procurement.storage.DataStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private SupplierService supplierService;

    public List<PurchaseOrder> getAllOrders() {
        return new ArrayList<>(DataStorage.orders.values());
    }

    public PurchaseOrder getById(String id) {
        return DataStorage.orders.get(id);
    }

    public PurchaseOrder createOrder(String applicationId, String supplierId) {
        ProcurementApplication app = applicationService.getById(applicationId);
        if (app == null) {
            throw new RuntimeException("采购申请不存在");
        }
        if (app.getStatus() != ApplicationStatus.APPROVED) {
            throw new RuntimeException("采购申请未通过审批，无法生成订单");
        }

        Supplier supplier = supplierService.getById(supplierId);
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }

        Quote quote = DataStorage.quotes.values().stream()
                .filter(q -> q.getApplicationId().equals(applicationId) && q.getSupplierId().equals(supplierId))
                .findFirst()
                .orElse(null);

        BigDecimal price = quote != null ? quote.getPrice() : app.getBudget().divide(BigDecimal.valueOf(app.getQuantity()), 2, BigDecimal.ROUND_HALF_UP);

        PurchaseOrder order = new PurchaseOrder();
        order.setId(DataStorage.generateId());
        order.setApplicationId(applicationId);
        order.setSupplierId(supplierId);
        order.setSupplierName(supplier.getName());
        order.setProductName(app.getProductName());
        order.setQuantity(app.getQuantity());
        order.setPrice(price);
        order.setTotalAmount(price.multiply(BigDecimal.valueOf(app.getQuantity())));
        order.setStatus(OrderStatus.PENDING);
        order.setCreateTime(LocalDateTime.now());
        DataStorage.orders.put(order.getId(), order);

        supplier.setWinCount(supplier.getWinCount() + 1);
        app.setStatus(ApplicationStatus.ORDERED);

        return order;
    }

    public PurchaseOrder confirmDelivery(String id) {
        PurchaseOrder order = DataStorage.orders.get(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        order.setStatus(OrderStatus.RECEIVED);
        order.setDeliveryTime(LocalDateTime.now());
        return order;
    }

    public PurchaseOrder acceptOrder(String id, String remark) {
        PurchaseOrder order = DataStorage.orders.get(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        order.setStatus(OrderStatus.ACCEPTED);
        order.setAcceptRemark(remark);
        return order;
    }

    public PurchaseOrder rejectOrder(String id, String remark) {
        PurchaseOrder order = DataStorage.orders.get(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        order.setStatus(OrderStatus.REJECTED);
        order.setAcceptRemark(remark);
        return order;
    }
}
