package com.procurement.controller;

import com.procurement.common.Result;
import com.procurement.entity.Quote;
import com.procurement.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quote")
@CrossOrigin
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @GetMapping("/list/{applicationId}")
    public Result<List<Quote>> listByApplication(@PathVariable String applicationId) {
        return Result.success(quoteService.getQuotesByApplication(applicationId));
    }

    @PostMapping("/create")
    public Result<Quote> create(@RequestBody Quote quote) {
        try {
            return Result.success(quoteService.createQuote(quote));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
