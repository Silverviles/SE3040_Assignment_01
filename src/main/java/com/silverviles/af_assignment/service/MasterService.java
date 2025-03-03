package com.silverviles.af_assignment.service;

import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.*;
import com.silverviles.af_assignment.dto.ExpenseReport;
import com.silverviles.af_assignment.dto.IncomeReport;

import java.io.IOException;
import java.util.List;

public interface MasterService {
    User findByUsername(String username) throws ServiceException;

    User save(User user) throws ServiceException;

    User update(User user) throws ServiceException;

    void deleteByUsername(String username) throws ServiceException;

    List<User> findAll();

    void addIncome(String username, Income income) throws ServiceException;

    void updateIncome(String username, String id, Income income) throws ServiceException;

    void deleteIncome(String username, String id) throws ServiceException;

    void addExpense(String username, Expense expense) throws ServiceException, IOException;

    void updateExpense(String username, String id, Expense expense) throws ServiceException;

    void deleteExpense(String username, String id) throws ServiceException;

    void addCategory(Category category) throws ServiceException;

    List<Category> getCategories() throws ServiceException;

    void deleteCategory(String categoryId) throws ServiceException;

    Category updateCategoryName(String categoryId, String newName) throws ServiceException;

    boolean existsCategory(Category category);

    void addBudget(String username, Budget budget) throws ServiceException;

    List<Budget> getBudgets(String username, String date) throws ServiceException;

    IncomeReport getIncomeReport(String name, String start, String end, String tag) throws ServiceException;

    ExpenseReport getExpenseReport(String name, String start, String end, String category, String tag) throws ServiceException;

    void sendEmail(String to, String subject, String body) throws IOException, ServiceException;
}
