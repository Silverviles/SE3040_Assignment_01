package com.silverviles.af_assignment.service;

import com.silverviles.af_assignment.common.ServiceException;
import com.silverviles.af_assignment.dao.Category;
import com.silverviles.af_assignment.dao.Expense;
import com.silverviles.af_assignment.dao.Income;
import com.silverviles.af_assignment.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.silverviles.af_assignment.common.ExceptionCode.*;
import static com.silverviles.af_assignment.util.DateUtil.convertStringToDate;

@Service
public class MasterServiceImpl implements MasterService {
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public MasterServiceImpl(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public User findByUsername(String username) throws ServiceException {
        return userService.findByUsername(username);
    }

    @Override
    public User save(User user) throws ServiceException {
        return userService.save(user);
    }

    @Override
    public User update(User user) throws ServiceException {
        return userService.update(user);
    }

    @Override
    public void deleteByUsername(String username) throws ServiceException {
        userService.deleteByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userService.findAll();
    }

    @Override
    public void addIncome(String username, Income income) throws ServiceException {
        User user = userService.findByUsername(username);
        user.addIncome(income);
        userService.save(user);
    }

    @Override
    public void updateIncome(String username, String id, Income income) throws ServiceException {
        User user = userService.findByUsername(username);
        Income savedIncome = user.getIncomes()
                .stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceException(INCOME_NOT_FOUND));
        if (income.getAmount() != null) {
            savedIncome.setAmount(income.getAmount());
        }
        if (income.getDate() != null) {
            savedIncome.setDate(income.getDate());
        }
        userService.save(user);
    }

    @Override
    public void deleteIncome(String username, String id) throws ServiceException {
        User user = userService.findByUsername(username);
        Income income = user.getIncomes()
                .stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceException(INCOME_NOT_FOUND));
        user.removeIncome(income);
        userService.save(user);
    }

    @Override
    public void addExpense(String username, Expense expense) throws ServiceException {
        if (!categoryService.existsCategory(expense.getCategory())) {
            throw new ServiceException(CATEGORY_NOT_FOUND);
        }
        User user = userService.findByUsername(username);
        user.addExpense(expense);
        userService.save(user);
    }

    @Override
    public void updateExpense(String username, String id, Expense expense) throws ServiceException {
        User user = userService.findByUsername(username);
        Expense savedExpense = user.getExpenses()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceException(EXPENSE_NOT_FOUND));
        if (expense.getAmount() != null) {
            savedExpense.setAmount(expense.getAmount());
        }
        if (expense.getDate() != null) {
            savedExpense.setDate(expense.getDate());
        }
        if (expense.getCategory() != null) {
            if (!categoryService.existsCategory(expense.getCategory())) {
                throw new ServiceException(CATEGORY_NOT_FOUND);
            }
            savedExpense.setCategory(expense.getCategory());
        }
        userService.save(user);
    }

    @Override
    public void deleteExpense(String username, String id) throws ServiceException {
        User user = userService.findByUsername(username);
        Expense expense = user.getExpenses()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServiceException(EXPENSE_NOT_FOUND));
        user.removeExpense(expense);
        userService.save(user);
    }

    @Override
    public Double getBudget(String username, String startDate, String endDate) throws ServiceException {
        LocalDate start = convertStringToDate(startDate);
        LocalDate end = convertStringToDate(endDate);
        User user = userService.findByUsername(username);
        return user.getBudget()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().get(start).equals(end))
                .mapToDouble(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new ServiceException(BUDGET_NOT_FOUND));
    }

    @Override
    public void addCategory(Category category) throws ServiceException {
        categoryService.addCategory(category);
    }

    @Override
    public List<Category> getCategories(String username) throws ServiceException {
        return categoryService.getCategories(username);
    }

    @Override
    public void deleteCategory(String categoryId) throws ServiceException {
        categoryService.deleteCategory(categoryId);
    }

    @Override
    public Category updateCategoryName(String categoryId, String newName) throws ServiceException {
        return categoryService.updateCategoryName(categoryId, newName);
    }

    @Override
    public boolean existsCategory(Category category) {
        return categoryService.existsCategory(category);
    }
}
