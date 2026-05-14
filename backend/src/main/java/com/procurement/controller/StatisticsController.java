package com.procurement.controller;

import com.procurement.common.Result;
import com.procurement.service.StatisticsService;
import com.procurement.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/summary")
    public Result<StatisticsVO> summary() {
        return Result.success(statisticsService.getStatistics());
    }
}
