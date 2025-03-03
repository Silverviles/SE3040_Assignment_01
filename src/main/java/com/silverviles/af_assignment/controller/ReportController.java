package com.silverviles.af_assignment.controller;

import com.silverviles.af_assignment.common.BaseController;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dto.ExpenseReport;
import com.silverviles.af_assignment.dto.IncomeReport;
import com.silverviles.af_assignment.util.PDFGeneratorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController extends BaseController {
    @GetMapping("/income")
    public IncomeReport getIncomeReport(
            HttpServletRequest request,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String tag
    ) throws ServiceException {
        String name = extractUsernameFromRequest(request);
        log.info("Generating income report for {} from {} to {}", name, startDate, endDate);
        return masterService.getIncomeReport(name, startDate, endDate, tag);
    }

    @GetMapping("/income/pdf")
    public void getIncomeReportPdf(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String tag
    ) throws ServiceException, IOException {
        String name = extractUsernameFromRequest(request);
        log.info("Generating income report PDF for {} from {} to {}", name, startDate, endDate);
        IncomeReport incomeReport = masterService.getIncomeReport(name, startDate, endDate, tag);
        incomeReport.setUsername(name);
        byte[] pdfBytes = PDFGeneratorUtil.generateIncomeReportPdf(incomeReport);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=income_report.pdf");
        response.getOutputStream().write(pdfBytes);
    }

    @GetMapping("/expense")
    public ExpenseReport getExpenseReport(
            HttpServletRequest request,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag
    ) throws ServiceException {
        String name = extractUsernameFromRequest(request);
        log.info("Generating expense report for {} from {} to {}", name, startDate, endDate);
        return masterService.getExpenseReport(name, startDate, endDate, category, tag);
    }

    @GetMapping("/expense/pdf")
    public void getExpenseReportPdf(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag
    ) throws ServiceException, IOException {
        String name = extractUsernameFromRequest(request);
        log.info("Generating expense report PDF for {} from {} to {}", name, startDate, endDate);
        ExpenseReport expenseReport = masterService.getExpenseReport(name, startDate, endDate, category, tag);
        expenseReport.setUsername(name);
        byte[] pdfBytes = PDFGeneratorUtil.generateExpenseReportPdf(expenseReport);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=expense_report.pdf");
        response.getOutputStream().write(pdfBytes);
    }

    @GetMapping("/income-vs-expense/pdf")
    public void getIncomeVsExpenseChart(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String tag
    ) throws ServiceException, IOException {
        String name = extractUsernameFromRequest(request);
        log.info("Generating income vs expense chart for {} from {} to {}", name, startDate, endDate);
        IncomeReport incomeReport = masterService.getIncomeReport(name, startDate, endDate, tag);
        ExpenseReport expenseReport = masterService.getExpenseReport(name, startDate, endDate, null, tag);
        byte[] chartBytes = PDFGeneratorUtil.generateIncomeVsExpenseChart(incomeReport, expenseReport);

        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "attachment; filename=income_vs_expense_chart.png");
        response.getOutputStream().write(chartBytes);
    }
}
