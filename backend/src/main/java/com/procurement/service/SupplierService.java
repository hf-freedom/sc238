package com.procurement.service;

import com.procurement.entity.Supplier;
import com.procurement.storage.DataStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(DataStorage.suppliers.values());
    }

    public Supplier getById(String id) {
        return DataStorage.suppliers.get(id);
    }

    public Supplier createSupplier(Supplier supplier) {
        supplier.setId(DataStorage.generateId());
        supplier.setWinCount(0);
        supplier.setCreateTime(LocalDateTime.now());
        DataStorage.suppliers.put(supplier.getId(), supplier);
        return supplier;
    }
}
