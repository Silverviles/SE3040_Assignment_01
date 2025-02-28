package com.silverviles.af_assignment.controller;

import com.silverviles.af_assignment.common.BaseController;
import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Budget;
import com.silverviles.af_assignment.dao.Expense;
import com.silverviles.af_assignment.dao.Income;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/finance")
@Slf4j
public class FinanceController extends BaseController {
    @PostMapping("/income")
    public String addIncome(@NonNull HttpServletRequest request, @RequestBody Income income) throws ServiceException {
        log.info("Adding income: {}", income);
        masterService.addIncome(extractUsernameFromRequest(request), income);
        log.info("Income added successfully");
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
    public String addExpense(@NonNull HttpServletRequest request, @RequestBody Expense expense) throws ServiceException {
        log.info("Adding expense: {}", expense);
        masterService.addExpense(extractUsernameFromRequest(request), expense);
        log.info("Expense added successfully");
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
    public Double getBudget(@NonNull HttpServletRequest request, @RequestParam String date) throws ServiceException {
        log.info("Getting budget for user {} for date : {}", extractUsernameFromRequest(request), date);
        return masterService.getBudget(extractUsernameFromRequest(request), date);
    }

    @PostMapping("/budget")
    public String addBudget(@NonNull HttpServletRequest request, @RequestBody Budget budget) throws ServiceException {
        log.info("Adding budget: {}", budget);
        masterService.addBudget(extractUsernameFromRequest(request), budget);
        log.info("Budget added successfully");
        return HttpStatus.CREATED.getReasonPhrase();
    }
}
