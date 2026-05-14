package com.procurement.controller;

import com.procurement.common.Result;
import com.procurement.entity.Supplier;
import com.procurement.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
@CrossOrigin
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/list")
    public Result<List<Supplier>> list() {
        return Result.success(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public Result<Supplier> getById(@PathVariable String id) {
        return Result.success(supplierService.getById(id));
    }

    @PostMapping("/create")
    public Result<Supplier> create(@RequestBody Supplier supplier) {
        return Result.success(supplierService.createSupplier(supplier));
    }
}
