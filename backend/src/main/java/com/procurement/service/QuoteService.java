package com.procurement.service;

import com.procurement.entity.Quote;
import com.procurement.entity.Supplier;
import com.procurement.storage.DataStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    @Autowired
    private SupplierService supplierService;

    public List<Quote> getQuotesByApplication(String applicationId) {
        return DataStorage.quotes.values().stream()
                .filter(q -> q.getApplicationId().equals(applicationId))
                .collect(Collectors.toList());
    }

    public Quote createQuote(Quote quote) {
        Supplier supplier = supplierService.getById(quote.getSupplierId());
        if (supplier == null) {
            throw new RuntimeException("供应商不存在");
        }
        quote.setId(DataStorage.generateId());
        quote.setSupplierName(supplier.getName());
        quote.setCreateTime(LocalDateTime.now());
        DataStorage.quotes.put(quote.getId(), quote);
        return quote;
    }
}
