package com.procurement.controller;

import com.procurement.common.Result;
import com.procurement.entity.ProcurementApplication;
import com.procurement.service.ApplicationService;
import com.procurement.vo.RecommendResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/application")
@CrossOrigin
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/list")
    public Result<List<ProcurementApplication>> list() {
        return Result.success(applicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    public Result<ProcurementApplication> getById(@PathVariable String id) {
        return Result.success(applicationService.getById(id));
    }

    @PostMapping("/create")
    public Result<ProcurementApplication> create(@RequestBody ProcurementApplication app) {
        try {
            return Result.success(applicationService.createApplication(app));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/approve/{id}")
    public Result<ProcurementApplication> approve(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            return Result.success(applicationService.approve(id, body.get("remark")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reject/{id}")
    public Result<ProcurementApplication> reject(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            return Result.success(applicationService.reject(id, body.get("remark")));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/recommend/{id}")
    public Result<String> recommend(@PathVariable String id) {
        return Result.success(applicationService.recommendSupplier(id));
    }

    @GetMapping("/recommend/detail/{id}")
    public Result<RecommendResultVO> recommendDetail(@PathVariable String id) {
        return Result.success(applicationService.getRecommendationDetail(id));
    }
}
