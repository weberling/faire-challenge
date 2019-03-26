package com.challenge.faire.controller;

import com.challenge.faire.service.SolutionService;
import com.challenge.faire.statistics.StatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by weberling on 25/03/19.
 */
@RestController
@RequestMapping("orders")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @PostMapping("/consume")
    public void consumeOrders() {
        solutionService.consume();
    }


    @GetMapping("/statistics")
    public StatisticsDto statistics() {
        return solutionService.statistics();
    }

    @PostMapping("/consume-statistics")
    public void consumeAndStatistics() {
        solutionService.consumeAndStatistics();
    }
}
