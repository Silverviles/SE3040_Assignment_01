package com.silverviles.af_assignment.controller;

import com.silverviles.af_assignment.common.BaseController;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dto.ExpenseReport;
import com.silverviles.af_assignment.dto.IncomeReport;
import com.silverviles.af_assignment.util.DateUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController extends BaseController {
    @GetMapping("/income")
    public IncomeReport getIncomeReport(
            HttpServletRequest request,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String tag
    ) throws ServiceException {
        String name = extractUsernameFromRequest(request);
        log.info("Generating income report for {} from {} to {}", name, startDate, endDate);
        return masterService.getIncomeReport(name, startDate, endDate, tag);
    }

    @GetMapping("/expense")
    public ExpenseReport getExpenseReport(
            HttpServletRequest request,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String category,
            @RequestParam String tag
    ) throws ServiceException {
        String name = extractUsernameFromRequest(request);
        log.info("Generating expense report for {} from {} to {}", name, startDate, endDate);
        return masterService.getExpenseReport(name, startDate, endDate, category, tag);
    }
}
