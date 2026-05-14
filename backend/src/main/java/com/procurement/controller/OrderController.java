package com.procurement.controller;

import com.procurement.common.Result;
import com.procurement.entity.PurchaseOrder;
import com.procurement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Result<List<PurchaseOrder>> list() {
        return Result.success(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public Result<PurchaseOrder> getById(@PathVariable String id) {
        return Result.success(orderService.getById(id));
    }

    @PostMapping("/create")
    public Result<PurchaseOrder> create(@RequestBody Map<String, String> body) {
        try {
            return Result.success(orderService.createOrder(body.get("applicationId"), body.get("supplierId")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/delivery/{id}")
    public Result<PurchaseOrder> delivery(@PathVariable String id) {
        try {
            return Result.success(orderService.confirmDelivery(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/accept/{id}")
    public Result<PurchaseOrder> accept(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            return Result.success(orderService.acceptOrder(id, body.get("remark")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reject/{id}")
    public Result<PurchaseOrder> reject(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            return Result.success(orderService.rejectOrder(id, body.get("remark")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
