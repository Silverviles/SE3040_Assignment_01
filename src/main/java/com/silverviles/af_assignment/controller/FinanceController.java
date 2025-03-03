package com.silverviles.af_assignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverviles.af_assignment.common.BaseController;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Budget;
import com.silverviles.af_assignment.dao.Expense;
import com.silverviles.af_assignment.dao.Income;
import com.silverviles.af_assignment.dto.AddExpenseRequest;
import com.silverviles.af_assignment.dto.AddIncomeRequest;
import com.silverviles.af_assignment.scheduler.RecurringExpenseJob;
import com.silverviles.af_assignment.scheduler.RecurringIncomeJob;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/finance")
@Slf4j
public class FinanceController extends BaseController {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/income")
    public String addIncome(
            @NonNull HttpServletRequest request,
            @RequestBody AddIncomeRequest incomeRequest
    ) throws ServiceException {
        log.info("Adding income: {}", incomeRequest);
        masterService.addIncome(extractUsernameFromRequest(request), incomeRequest.getIncome());
        log.info("Income added successfully");

        if (incomeRequest.getRecurring() != null && incomeRequest.getRecurring()) {
            scheduleRecurringIncome(extractUsernameFromRequest(request), incomeRequest);
        }

        return HttpStatus.CREATED.getReasonPhrase();
    }

    @PostMapping("/income/{id}")
    public String updateIncome(@NonNull HttpServletRequest request, @PathVariable String id, @RequestBody Income income) throws ServiceException {
        log.info("Updating income: {}", income);
        masterService.updateIncome(extractUsernameFromRequest(request), id, income);
        log.info("Income updated successfully");
        return HttpStatus.OK.getReasonPhrase();
    }

    @DeleteMapping("/income/{id}")
    public String deleteIncome(@NonNull HttpServletRequest request, @PathVariable String id) throws ServiceException {
        log.info("Deleting income with id: {}", id);
        masterService.deleteIncome(extractUsernameFromRequest(request), id);
        log.info("Income deleted successfully");
        return HttpStatus.OK.getReasonPhrase();
    }

    @GetMapping("/income")
    public List<Income> getIncome(@NonNull HttpServletRequest request) throws ServiceException {
        log.info("Getting incomes for user: {}", extractUsernameFromRequest(request));
        return masterService.findByUsername(extractUsernameFromRequest(request)).getIncomes();
    }

    @PostMapping("/expense")
    public String addExpense(
            @NonNull HttpServletRequest request,
            @RequestBody AddExpenseRequest expenseRequest
    ) throws ServiceException, IOException {
        log.info("Adding expense: {}", expenseRequest);
        String username = extractUsernameFromRequest(request);
        masterService.addExpense(username, expenseRequest.getExpense());
        log.info("Expense added successfully");

        if (expenseRequest.getRecurring() != null && expenseRequest.getRecurring()) {
            scheduleRecurringExpense(username, expenseRequest);
        }

        return HttpStatus.CREATED.getReasonPhrase();
    }

    @PostMapping("/expense/{id}")
    public String updateExpense(@NonNull HttpServletRequest request, @PathVariable String id, @RequestBody Expense expense) throws ServiceException {
        log.info("Updating expense: {}", expense);
        masterService.updateExpense(extractUsernameFromRequest(request), id, expense);
        log.info("Expense updated successfully");
        return HttpStatus.OK.getReasonPhrase();
    }

    @DeleteMapping("/expense/{id}")
    public String deleteExpense(@NonNull HttpServletRequest request, @PathVariable String id) throws ServiceException {
        log.info("Deleting expense with id: {}", id);
        masterService.deleteExpense(extractUsernameFromRequest(request), id);
        log.info("Expense deleted successfully");
        return HttpStatus.OK.getReasonPhrase();
    }

    @GetMapping("/expense")
    public List<Expense> getExpense(@NonNull HttpServletRequest request) throws ServiceException {
        log.info("Getting expenses for user: {}", extractUsernameFromRequest(request));
        return masterService.findByUsername(extractUsernameFromRequest(request)).getExpenses();
    }

    @GetMapping("/budget")
    public List<Budget> getBudget(@NonNull HttpServletRequest request, @RequestParam String date) throws ServiceException {
        log.info("Getting budget for user {} for date : {}", extractUsernameFromRequest(request), date);
        return masterService.getBudgets(extractUsernameFromRequest(request), date);
    }

    @PostMapping("/budget")
    public String addBudget(@NonNull HttpServletRequest request, @RequestBody Budget budget) throws ServiceException {
        log.info("Adding budget: {}", budget);
        masterService.addBudget(extractUsernameFromRequest(request), budget);
        log.info("Budget added successfully");
        return HttpStatus.CREATED.getReasonPhrase();
    }

    private void scheduleRecurringExpense(String username, AddExpenseRequest expenseRequest) {
        String expenseJson = "";
        try {
            expenseJson = objectMapper.writeValueAsString(expenseRequest.getExpense());
        } catch (JsonProcessingException e) {
            log.error("Error serializing expense object", e);
        }

        JobDetail jobDetail = JobBuilder.newJob(RecurringExpenseJob.class)
                .withIdentity("recurringExpenseJob", "expenseGroup")
                .usingJobData("username", username)
                .usingJobData("expense", expenseJson)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("recurringExpenseTrigger", "expenseGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(expenseRequest.getRecurringIntervalInHours())
                        .repeatForever())
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Scheduled recurring expense job");
        } catch (SchedulerException e) {
            log.error("Error scheduling recurring expense job", e);
        }
    }

    private void scheduleRecurringIncome(String username, AddIncomeRequest incomeRequest) {
        String incomeJson;
        try {
            incomeJson = objectMapper.writeValueAsString(incomeRequest.getIncome());
        } catch (JsonProcessingException e) {
            log.error("Error serializing income object", e);
            return;
        }

        JobDetail jobDetail = JobBuilder.newJob(RecurringIncomeJob.class)
                .withIdentity("recurringIncomeJob", "incomeGroup")
                .usingJobData("username", username)
                .usingJobData("income", incomeJson)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("recurringIncomeTrigger", "incomeGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(incomeRequest.getRecurringIntervalInHours())
                        .repeatForever())
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Scheduled recurring income job");
        } catch (SchedulerException e) {
            log.error("Error scheduling recurring income job", e);
        }
    }
}
